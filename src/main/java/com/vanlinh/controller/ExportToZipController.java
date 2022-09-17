package com.vanlinh.controller;

import com.vanlinh.model.FileForRequest;
import com.vanlinh.service.ZipExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ExportToZipController {
    @Autowired
    ZipExporter zipExporter;

    @PostMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Void> export(@RequestBody @Validated FileForRequest fileForRequest) throws Exception {
        zipExporter.export(fileForRequest.getFileJson(), fileForRequest.getFileImage());
        return ResponseEntity.ok().build();
    }
}
