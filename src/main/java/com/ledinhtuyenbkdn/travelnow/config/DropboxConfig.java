package com.ledinhtuyenbkdn.travelnow.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxConfig {
    private String ACCESS_TOKEN = "_HmmBdMsN5AAAAAAAAAAD3sncoOidKO5ITd3vVnlbtvpPBew7Atf1SEFxHlZXb7f";

    @Bean
    public DbxClientV2 dbxClientV2() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("Apps/travel-now").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        return client;
    }
}
