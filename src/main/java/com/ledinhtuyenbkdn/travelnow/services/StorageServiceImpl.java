package com.ledinhtuyenbkdn.travelnow.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        return fileId;
    }

    @Override
    public void delete(String fileId) {
        try {
            cloudinary.uploader().destroy(fileId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String load(String fileId) {
        return cloudinary.url().generate(fileId);
    }
}
