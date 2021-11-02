package com.aws.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class SignedUrlService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    private String generateUrl(String fileName, HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
//        calendar.add(Calendar.DATE, 1); // valid for 24 hours

//        cal.set(Calendar.HOUR_OF_DAY,17);
//        cal.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,100);
//        cal.set(Calendar.MILLISECOND,0);

        return s3Client.generatePresignedUrl(bucketName, fileName, calendar.getTime(), httpMethod).toString();
    }


    @Async
    public String findByName(String fileName) {
        if (!s3Client.doesObjectExist(bucketName, fileName))
            return "File does not exist";
        log.info("Generating signed URL for file name {}", fileName);
        return generateUrl(fileName, HttpMethod.GET);
    }

    @Async
    public String save(String extension) {
        String fileName = UUID.randomUUID().toString() + extension;
        return generateUrl(fileName, HttpMethod.PUT);
    }

    public String uploadFile(MultipartFile file) {
        //covert multipart file to File
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
        return generateUrl(fileName, HttpMethod.PUT);
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
