package com.skillscout.service.impl;

import com.skillscout.Repository.ApplicationRepository;
import com.skillscout.Repository.JobRepository;
import com.skillscout.Repository.UserRepository;
import com.skillscout.model.DTO.ApplicationDTO;
import com.skillscout.model.entity.Application;
import com.skillscout.model.entity.Job;
import com.skillscout.model.entity.User;
import com.skillscout.model.enums.ApplicationState;
import com.skillscout.service.ApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    @Override
    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDTO getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    @Override
    @Transactional // Ensures database operations occur within a single transaction
    public Application createApplication(ApplicationDTO applicationDTO) {
        User user = userRepository.findById(applicationDTO.getUserId())
                .orElseThrow(()-> new RuntimeException("User not found"));
        Job job = jobRepository.findById(applicationDTO.getJobId())
                .orElseThrow(()-> new RuntimeException());
        Application application = new Application();
        application.setBody(applicationDTO.getBody());
        application.setApplicationState(ApplicationState.PENDING);
        application.setUser(user);
        application.setJob(job);
        return applicationRepository.save(application);
    }

    @Override
    @Transactional
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    @Override
    public void updateApplicationStatus(Long applicationId, ApplicationState newState, Long recruiterId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        User recurter = userRepository.findById(recruiterId)
                .orElseThrow(()-> new RuntimeException("User not found."));
        application.setApplicationState(newState);
        applicationRepository.save(application);
    }

    @Override
    public List<ApplicationDTO> getApplicationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Application> applications = applicationRepository.findByUser(user);

        return applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDTO> getApplicationsByJobId(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        List<Application> applications = applicationRepository.findByJob(job);

        return applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ApplicationDTO convertToDTO(Application application) {
        return new ApplicationDTO(
                application.getId(),
                application.getBody(),
                application.getApplicationState(),
                application.getJob() != null ? application.getJob().getId() : null,
                application.getUser() != null ? application.getUser().getId() : null
        );
    }

    private Application convertToEntity(ApplicationDTO applicationDTO) {
        Application application;

        if (applicationDTO.getId() != null) { // If ID exists, fetch from DB
            application = applicationRepository.findById(applicationDTO.getId())
                    .orElse(new Application()); // Use existing entity or create a new one
        } else {
            application = new Application();
        }

        application.setBody(applicationDTO.getBody());
        application.setApplicationState(applicationDTO.getApplicationState());

        return application;
    }
}
