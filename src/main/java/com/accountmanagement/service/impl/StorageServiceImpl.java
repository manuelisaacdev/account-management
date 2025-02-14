package com.accountmanagement.service.impl;

import com.accountmanagement.config.StorageProperties;
import com.accountmanagement.exception.EmptyFileException;
import com.accountmanagement.exception.StorageException;
import com.accountmanagement.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageProperties storageProperties;

    @Override
    public void init() {
        try {
            if (Files.notExists(storageProperties.getRootLocation())) {
                Files.createDirectories(storageProperties.getRootLocation());
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void delete(String filename) {
        if (Objects.isNull(filename)) return;
        try {
            Files.deleteIfExists(this.storageProperties.getRootLocation().resolve(filename));
        } catch (IOException e) {
            log.error("Could not delete file: {}", filename, e);
        }
    }

    @Override
    public Path store(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new EmptyFileException("Empty file.");
        }

        Path destination = buildDestinationFile(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, destination);
            return destination;
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    private Path buildDestinationFile(MultipartFile file) {
        return storageProperties.getRootLocation().resolve(this.storageProperties.getRootLocation().resolve(String.format(
        "%s.%s",
        RandomStringUtils.secure().nextAlphanumeric(64),
        Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()))
        ))).normalize().toAbsolutePath();
    }
}
