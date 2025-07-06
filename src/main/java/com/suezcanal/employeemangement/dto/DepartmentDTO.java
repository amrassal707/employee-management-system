package com.suezcanal.employeemangement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "department name is required")
    @Size(min = 2, max = 50)
    private String name;
}
