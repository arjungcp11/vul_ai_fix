package com.hexaware.vulfixed.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.vulfixed.service.VulnFixService;
import com.hexaware.vulfixed.service.dto.FixRequest;
import com.hexaware.vulfixed.service.dto.FixResponse;

@RestController
@RequestMapping("/api")
public class FixController {

    private final VulnFixService vulnFixService;

    public FixController(VulnFixService vulnFixService) {
        this.vulnFixService = vulnFixService;
    }

    @PostMapping("/fix")
    public ResponseEntity<FixResponse> fixRepo(@RequestBody FixRequest request) throws Exception {
        FixResponse resp = vulnFixService.processRepository(request);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/download/{archiveId}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long archiveId) throws Exception {
        byte[] pdf = vulnFixService.getPdfBytes(archiveId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=fix-archive-"+archiveId+".pdf")
                .body(pdf);
    }
}