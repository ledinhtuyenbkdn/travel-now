package com.ledinhtuyenbkdn.travelnow.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void store(MultipartFile fileClientAddress, String filename);

    void delete(String filename);

    String load(String filename);
}
