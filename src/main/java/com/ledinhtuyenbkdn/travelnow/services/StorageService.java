package com.ledinhtuyenbkdn.travelnow.services;

public interface StorageService {
    void store(String fileClientAddress, String filename);

    void delete(String filename);

    String load(String filename);
}
