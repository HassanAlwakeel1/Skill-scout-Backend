package com.skillscout.service;

import com.skillscout.model.entity.Job;

import java.util.List;

public interface PostService {
    // 1. Post a Job
    Job createJob(Job job);

    // 2. Get All Jobs
    List<Job> getAllJobs();

    // 3. Get Job Details by ID
    Job getJobById(Long id);

    // 4. Update Job
    Job updateJob(Long id, Job updatedJob);

    // 5. Delete Job
    void deleteJob(Long id);
}
