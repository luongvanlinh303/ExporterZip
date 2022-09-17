package com.vanlinh.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FileForRequest {
    @NotBlank(message = "file json is not blank")
    String fileJson;

    @NotBlank(message = "file image is not blank")
    String fileImage;
}
