package com.example.vulfixed.controller;

import com.example.vulfixed.service.VulnFixService;
import com.example.vulfixed.service.dto.FixRequest;
import com.example.vulfixed.service.dto.FixResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
