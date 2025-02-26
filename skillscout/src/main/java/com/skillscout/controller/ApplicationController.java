package com.skillscout.controller;

import com.skillscout.model.DTO.ApplicationDTO;
import com.skillscout.model.entity.Application;
import com.skillscout.model.enums.ApplicationState;
import com.skillscout.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody ApplicationDTO applicationDTO) {
        return ResponseEntity.ok(applicationService.createApplication(applicationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<String> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationState status,
            @RequestParam Long recruiterId // Should ideally come from authentication (JWT)
    ) {
        applicationService.updateApplicationStatus(applicationId, status, recruiterId);
        return ResponseEntity.ok("Application status updated successfully.");
    }

}

