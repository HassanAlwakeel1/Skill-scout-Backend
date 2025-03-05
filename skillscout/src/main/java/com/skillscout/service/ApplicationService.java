package com.skillscout.service;

import com.skillscout.model.DTO.ApplicationDTO;
import com.skillscout.model.entity.Application;
import com.skillscout.model.enums.ApplicationState;

import java.util.List;

public interface ApplicationService {
    List<ApplicationDTO> getAllApplications();
    ApplicationDTO getApplicationById(Long id);
    Application createApplication(ApplicationDTO applicationDTO);
    void deleteApplication(Long id);
    void updateApplicationStatus(Long applicationId, ApplicationState newState, Long recruiterId);

    List<ApplicationDTO> getApplicationsByUserId(Long userId);

    List<ApplicationDTO> getApplicationsByJobId(Long jobId);


}
