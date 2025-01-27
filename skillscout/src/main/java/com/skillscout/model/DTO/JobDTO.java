package com.skillscout.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private Long userId; // ID of the recruiter who created the job
}
