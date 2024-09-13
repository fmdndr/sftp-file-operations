package com.fmd.jschdemo.service;

public interface SFTPService {

    boolean uploadFile(String localFilePath, String remoteFilePath);

    boolean downloadFile(String localFilePath, String remoteFilePath);
}
