package com.accountmanagement.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void init();
    boolean delete(String filename);
    Path store(MultipartFile multipartFile);
}
