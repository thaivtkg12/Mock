package com.java.config;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class UploadImage {
//Upload hinh anh
    @Value("${upload.path}")
    private String pathUploadImage;



   public void saveImage(MultipartFile file) {
        try {
            File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {



       }
    }
}