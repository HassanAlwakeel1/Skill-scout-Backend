package com.skillscout.service.impl;

import com.skillscout.Repository.JobRepository;
import com.skillscout.model.entity.Job;
import com.skillscout.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final JobRepository jobRepository;

    @Autowired
    public PostServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job createJob(Job job) {
        // Save the job to the database
        return jobRepository.save(job);
    }

    @Override
    public List<Job> getAllJobs() {
        // Retrieve all jobs from the database
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        // Retrieve job by ID or throw an exception if not found
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));
    }

    @Override
    public Job updateJob(Long id, Job updatedJob) {
        // Check if the job exists
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + id));

        // Update job details
        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setRequirements(updatedJob.getRequirements());

        // Save updated job to the database
        return jobRepository.save(existingJob);
    }

    @Override
    public void deleteJob(Long id) {
        // Check if the job exists
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found with ID: " + id);
        }

        // Delete the job
        jobRepository.deleteById(id);
    }
}
