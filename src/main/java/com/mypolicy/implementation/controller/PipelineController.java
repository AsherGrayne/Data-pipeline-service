package com.mypolicy.implementation.controller;

import com.mypolicy.implementation.service.PipelineOrchestratorService;
import com.mypolicy.implementation.service.StitchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/pipeline")
public class PipelineController {

    private final PipelineOrchestratorService pipelineOrchestrator;

    public PipelineController(PipelineOrchestratorService pipelineOrchestrator) {
        this.pipelineOrchestrator = pipelineOrchestrator;
    }

    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runPipeline() {
        StitchingService.StitchingResult result = pipelineOrchestrator.runFullPipeline();
        return ResponseEntity.ok(Map.of(
                "totalProcessed", result.totalProcessed(),
                "matched", result.matched(),
                "unmatched", result.unmatched()
        ));
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam("collectionName") String collectionName) throws IOException {
        StitchingService.StitchingResult result = pipelineOrchestrator.uploadAndProcess(file, collectionName);
        return ResponseEntity.ok(Map.of(
                "message", "File uploaded and pipeline executed",
                "collectionName", collectionName,
                "totalProcessed", result.totalProcessed(),
                "matched", result.matched(),
                "unmatched", result.unmatched()
        ));
    }
}
