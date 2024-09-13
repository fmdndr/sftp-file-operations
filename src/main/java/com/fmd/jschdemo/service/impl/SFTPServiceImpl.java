package com.fmd.jschdemo.service.impl;

import com.fmd.jschdemo.service.SFTPService;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

@Slf4j
@Service
public class SFTPServiceImpl implements SFTPService {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private Integer port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.sessionTimeout}")
    private Integer sessionTimeout;

    @Value("${sftp.channelTimeout}")
    private Integer channelTimeout;


    @Override
    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        ChannelSftp channelSftp = createSftpChannel();
        try {
            channelSftp.put(localFilePath, remoteFilePath);
            return true;
        } catch (SftpException e) {
            log.error(e.getMessage());
        } finally {
            disconnectChannelSftp(channelSftp);
        }
        return false;
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        ChannelSftp channelSftp = createSftpChannel();
        OutputStream outputStream;
        try {
            File workspace = Paths.get(localFilePath).toFile();

            if (!workspace.exists()) {
                workspace.mkdir();
            }
            downloadFromFolder(channelSftp, "/uploads/", localFilePath);

            return true;
        } /*catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        } */ catch (SftpException e) {
            throw new RuntimeException(e);
        } finally {
            disconnectChannelSftp(channelSftp);
        }
    }

    private void downloadFromFolder(ChannelSftp channelSftp, String folder, String localFilePath) throws SftpException {
        Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(folder);
        System.out.println(entries);

        //download all from root folder
        for (ChannelSftp.LsEntry en : entries) {
            if (en.getFilename().equals(".") || en.getFilename().equals("..") || en.getAttrs().isDir()) {
                continue;
            }

            System.out.println(en.getFilename());
            String path = localFilePath + "/" + en.getFilename();
            try {
                channelSftp.get(folder + en.getFilename(), path);
                File file = new File(localFilePath);
                String content = new String(Files.readAllBytes(Paths.get(path)));
                System.out.println(content);
            } catch (SftpException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


    private ChannelSftp createSftpChannel() {
        try {
            JSch jSch = new JSch();
            String hosts = System.getProperty("user.home") + "./ssh/known_hosts";
            jSch.setKnownHosts(hosts);
            Session session = jSch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "password");

            session.setPassword(password);
            session.connect(sessionTimeout);
            Channel channel = session
                    .openChannel("sftp");
            channel.connect(channelTimeout);
            return (ChannelSftp) channel;
        } catch (JSchException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void disconnectChannelSftp(ChannelSftp channelSftp) {
        try {
            if (channelSftp == null) {
                return;
            }
            if (channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (channelSftp.getSession().isConnected()) {
                channelSftp.getSession().disconnect();
            }
        } catch (JSchException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
