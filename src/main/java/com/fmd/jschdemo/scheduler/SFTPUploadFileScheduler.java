package com.fmd.jschdemo.scheduler;


import com.fmd.jschdemo.service.SFTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class SFTPUploadFileScheduler {

    @Value("/home/fmd/Documents")
    private String resource;

    private final SFTPService sftpService;


 //   @Scheduled(fixedDelay = 65000L)
    private void uploadFile() {
        log.info("upload file transfer started ");
        Set<String> fileList = getFileList();
        fileList
                .forEach(file -> {
            boolean isUploaded = sftpService
                    .uploadFile("/home/fmd/Documents/" + file, "/uploads");
            log.info("is uploaded : {}", isUploaded);
        });

    }

    private Set<String> getFileList() {
        try {
            Set<String> fileList = new HashSet<>();
            Files.walkFileTree(Paths.get(resource), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (Files.exists(file) || !Files.isDirectory(file) || !attrs.isDirectory()) {
                        fileList.add(file.getFileName().toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            return fileList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}