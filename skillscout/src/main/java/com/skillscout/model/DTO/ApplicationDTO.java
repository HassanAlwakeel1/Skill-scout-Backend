package com.skillscout.model.DTO;

import com.skillscout.model.enums.ApplicationState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
    private Long id;
    private String body;
    private ApplicationState applicationState;
    private Long jobId;
    private Long userId;
}
