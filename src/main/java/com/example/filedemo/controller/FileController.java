package com.example.filedemo.controller;
import com.example.filedemo.payload.UploadFileResponse;
import com.example.filedemo.property.FileStorageProperties;
import com.example.filedemo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    //make private
    private String server = "ftp.davidcano.ch";
    private int port = 21;
    private String user = "davidcano.ch";
    private String pass = "Nocturna1.";

    FTPClient ftpClient = new FTPClient();

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String firstRemoteFile = fileName;
            //pass multipartFile to file
            File convFile = new File(file.getOriginalFilename());
            convFile.createNewFile();

            //get file
            InputStream inputStream = new FileInputStream(convFile);

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully in : " + inputStream);
            }


        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void downloadFile(){
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // using retrieveFile(String, OutputStream)
            String remoteFile1 = "/pom.xml";
            File downloadFile1 = new File("C:/pom.xml");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();

            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void listFiles(){
        FTPClient ftpClient = new FTPClient();
        try {
            // Establish a connection with the FTP URL
            ftpClient.connect("ftp.davidcano.ch");
            // Enter user details : user name and password
            boolean isSuccess = ftpClient.login("davidcano.ch", "Nocturna1.");

            if (isSuccess) {
                // Fetch the list of names of the files. In case of no files an
                // empty array is returned
                String[] filesFTP = ftpClient.listNames();
                int count = 1;
                // Iterate on the returned list to obtain name of each file
                for (String file : filesFTP) {
                    System.out.println("File " + count + " :" + file);
                    count++;
                }
            }

            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
