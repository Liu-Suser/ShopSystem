/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service.impl;

import com.shiroyk.shopsystem.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private final Path imageLocation;

    @Autowired
    public FileServiceImpl() {
        this.imageLocation = Paths.get("./image").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.imageLocation);
            log.info("Image path: {}", imageLocation);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String storeImage(MultipartFile file) {
        String fileName;
        try {
            fileName = DigestUtils.md5DigestAsHex(file.getInputStream());
            Path targetLocation = this.imageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return fileName;
    }

    public Resource loadImageAsResource(String fileName) {
        Path filePath = this.imageLocation.resolve(fileName).normalize();
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
