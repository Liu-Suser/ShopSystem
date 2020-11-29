/*
 * Copyright (c) 2020 All right reserved.
 * Created by shiroyk, https://github.com/shiroyk
 */

package com.shiroyk.shopsystem.service;

import com.shiroyk.shopsystem.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {
    private final Path imageLocation;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public FileService() {
        this.imageLocation = Paths.get("./image").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.imageLocation);
            logger.info("Image path: "+imageLocation.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String storeImage(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(fileName.contains("..")) {
            return null;
        }
        Path targetLocation = this.imageLocation.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return fileName;
    }

    public Resource loadImageAsResource(String fileName) throws IOException {
        Path filePath = this.imageLocation.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if(resource.exists()) {
            return resource;
        } else {
            return null;
        }
    }
}
