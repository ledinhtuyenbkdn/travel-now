package com.ledinhtuyenbkdn.travelnow.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    void store(MultipartFile file);

    void delete(String filename);

    Path load(String filename);
}
