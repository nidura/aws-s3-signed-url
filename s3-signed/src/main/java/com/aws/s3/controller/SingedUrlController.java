package com.aws.s3.controller;

import com.aws.s3.service.SignedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/s3/signed")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SingedUrlController {

    private static final String FILE_NAME = "fileName";

    @Autowired
    private SignedUrlService signedUrlService;

    /*
    example for passing file name as string
     */
    @GetMapping
    public ResponseEntity<Object> findByName(@RequestParam("fileName") String fileName) {
        return new ResponseEntity<>(signedUrlService.findByName(fileName), HttpStatus.OK);
    }

    /*
    Requesting a pre-signed URL to upload a file
     */
    @PostMapping("/request")
    public ResponseEntity<Object> saveFile(@RequestParam("extension") String extension) {
        return new ResponseEntity<>(signedUrlService.save(extension), HttpStatus.OK);
    }

     /*
    example for passing file name by multipart file
     */
     /*
    Requesting a pre-signed URL to upload a file
     */
    @PostMapping("/request2")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(signedUrlService.uploadFile(file), HttpStatus.OK);
    }
}
