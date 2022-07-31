package com.raven.api.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static File multipartToFile(MultipartFile multipartFile) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);
        return convFile;
    }
    
}
