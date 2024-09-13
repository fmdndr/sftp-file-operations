/*
package com.fmd.jschdemo.runner;

import com.fmd.jschdemo.service.SFTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestFileTransfer implements CommandLineRunner {

    @Value("/home/fmd/playground/jschdemo/src/main/resources/tmp")
    private String resource;

    private final SFTPService sftpService;

    @Override
    public void run(String... args) throws Exception {
        log.info("upload file transfer started ");
        boolean isUploaded = sftpService.uploadFile("/home/fmd/Documents/confg-server.txt", "/uploads");
        log.info("is uploaded : {}", isUploaded);

        log.info("download file transfer started");
        boolean isDownloaded = sftpService.downloadFile(resource, "/uploads/confg-server.txt");
        log.info("is downloaded : {}", isDownloaded);
    }
}
*/
