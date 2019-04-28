package com.ledinhtuyenbkdn.travelnow.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
public class StorageServiceImpl implements StorageService {
    private Cloudinary cloudinary;

    public StorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String store(String base64) {
        String fileId = "";
        try {
            Map<String, String> result = cloudinary.uploader().upload(base64, ObjectUtils.emptyMap());
            fileId = result.get("public_id");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cloudinary.url().generate(fileId);
    }

    @Override
    public void delete(String fileUrl) {
        String[] splittedUrl = fileUrl.split("/");
        try {
            cloudinary.uploader().destroy(splittedUrl[splittedUrl.length - 1], ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
