package com.ledinhtuyenbkdn.travelnow.services;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class StorageServiceImpl implements StorageService {
    private DbxClientV2 dbxClientV2;

    @Autowired
    public StorageServiceImpl(DbxClientV2 dbxClientV2) {
        this.dbxClientV2 = dbxClientV2;
    }

    @Override
    public void store(MultipartFile file, String filename) {
        try (InputStream in = file.getInputStream()) {
            FileMetadata metadata = dbxClientV2.files().uploadBuilder("/" + filename)
                    .uploadAndFinish(in);
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String filename) {
        try {
            dbxClientV2.files().deleteV2("/" + filename);
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String load(String filename) {
        try {
            return dbxClientV2.files().getTemporaryLink("/" + filename).getLink();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
