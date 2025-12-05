package com.hexaware.vulfixed.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexaware.vulfixed.ai.AIClient;
import com.hexaware.vulfixed.model.Archive;
import com.hexaware.vulfixed.repository.ArchiveRepository;
import com.hexaware.vulfixed.service.dto.FixRequest;
import com.hexaware.vulfixed.service.dto.FixResponse;


@Service
public class VulnFixService {

    private final AIClient aiClient;
    private final ArchiveRepository archiveRepository;

    public VulnFixService(AIClient aiClient, ArchiveRepository archiveRepository) {
        this.aiClient = aiClient;
        this.archiveRepository = archiveRepository;
    }

    @Transactional
    public FixResponse processRepository(FixRequest request) throws Exception {
        String gitUrl = request.getGitUrl();
        String branch = request.getBranch() == null ? "main" : request.getBranch();
        Path tmp = Files.createTempDirectory("clone-");
        Git git = null;
        try {
            git = Git.cloneRepository()
                    .setURI(gitUrl)
                    .setDirectory(tmp.toFile())
                    .setBranch(branch)
                    .call();

            List<Path> javaFiles = new ArrayList<>();
            Files.walk(tmp)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(javaFiles::add);

            Archive archive = new Archive();
            archive.setGitUrl(gitUrl);
            archive.setCreatedAt(Instant.now());
            List<Archive.FileDiff> diffs = new ArrayList<>();

            for (Path p : javaFiles) {
                String original = Files.readString(p);
                String fixed = aiClient.suggestFix(original, p.toString());
                Archive.FileDiff fd = new Archive.FileDiff(p.toString(), original, fixed);
                diffs.add(fd);
            }

            archive.setFileDiffs(diffs);
            archive = archiveRepository.save(archive);

            // generate PDF summary
            byte[] pdfBytes = generatePdf(archive);
            archive.setPdfBytes(pdfBytes);
            archiveRepository.save(archive);

            return new FixResponse(archive.getId(), "Processed " + javaFiles.size() + " java files");
        } finally {
            if (git != null) {
                git.close();
            }
            // cleanup
            try {
                Files.walk(tmp).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] getPdfBytes(Long archiveId) {
        return archiveRepository.findById(archiveId)
                .map(Archive::getPdfBytes)
                .orElse(new byte[0]);
    }

    private byte[] generatePdf(Archive archive) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(doc, page);
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
            cs.newLineAtOffset(50, 750);
            cs.showText("Vulnerability Fix Archive: ID " + archive.getId());
            cs.newLineAtOffset(0, -20);
            cs.setFont(PDType1Font.HELVETICA, 10);
            cs.showText("Git URL: " + archive.getGitUrl());
            cs.newLineAtOffset(0, -20);
            cs.showText("Created At: " + archive.getCreatedAt().toString());
            cs.newLineAtOffset(0, -20);
            cs.showText("Files processed: " + (archive.getFileDiffs()==null?0:archive.getFileDiffs().size()));
            cs.newLineAtOffset(0, -20);
            cs.showText("---- Files summary ----");
            cs.newLineAtOffset(0, -20);

            int line = 0;
            if (archive.getFileDiffs()!=null) {
                for (Archive.FileDiff fd : archive.getFileDiffs()) {
                    String s = fd.getPath() + " | IssuesSummary: " + (fd.getIssuesSummary()==null? "n/a" : fd.getIssuesSummary());
                    cs.showText(s.length() > 100 ? s.substring(0,100)+"..." : s);
                    cs.newLineAtOffset(0, -12);
                    line++;
                    if (line>25) break;
                }
            }
            cs.endText();
            cs.close();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();
        }
    }
}