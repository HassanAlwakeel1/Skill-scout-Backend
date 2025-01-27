package com.skillscout.controller;

import com.skillscout.model.DTO.JobDTO;
import com.skillscout.model.entity.Job;
import com.skillscout.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final PostService postService;
    private final ModelMapper modelMapper;

    @Autowired
    public JobController(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    // 1. Create a new job
    @PostMapping
    public ResponseEntity<JobDTO> createJob(@RequestBody JobDTO jobDTO) {
        Job job = modelMapper.map(jobDTO, Job.class); // Map JobDTO to Job entity
        Job createdJob = postService.createJob(job);
        JobDTO responseJobDTO = modelMapper.map(createdJob, JobDTO.class); // Map back to DTO
        return new ResponseEntity<>(responseJobDTO, HttpStatus.CREATED);
    }

    // 2. Get all jobs
    @GetMapping
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        List<Job> jobs = postService.getAllJobs();
        List<JobDTO> jobDTOs = jobs.stream()
                .map(job -> modelMapper.map(job, JobDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(jobDTOs, HttpStatus.OK);
    }

    // 3. Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        Job job = postService.getJobById(id);
        JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
        return new ResponseEntity<>(jobDTO, HttpStatus.OK);
    }

    // 4. Update a job
    /** The path variable is used to specify which job to update.
    The JobDTO is used to send the updated data for that job.
    * */
    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @RequestBody JobDTO updatedJobDTO) {
        Job updatedJob = modelMapper.map(updatedJobDTO, Job.class);
        Job job = postService.updateJob(id, updatedJob);
        JobDTO responseJobDTO = modelMapper.map(job, JobDTO.class);
        return new ResponseEntity<>(responseJobDTO, HttpStatus.OK);
    }

    // 5. Delete a job
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        postService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
