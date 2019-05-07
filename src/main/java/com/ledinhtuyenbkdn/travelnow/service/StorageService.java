package com.ledinhtuyenbkdn.travelnow.service;

public interface StorageService {
    String store(String base64);

    void delete(String fileUrl);

}
