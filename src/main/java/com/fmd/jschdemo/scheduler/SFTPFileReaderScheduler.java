package com.fmd.jschdemo.scheduler;


import com.fmd.jschdemo.service.SFTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SFTPFileReaderScheduler {

    @Value("/home/fmd/playground/jschdemo/src/main/resources/tmp")
    private String resource;


    private final SFTPService sftpService;


    @Scheduled(fixedDelay = 30000L)
    private void bankTransferReaderJob() {
        log.info("download file transfer started");

        boolean isDownloaded = sftpService.downloadFile(resource, "/uploads");
        log.info("is downloaded : {}", isDownloaded);
    }

}
