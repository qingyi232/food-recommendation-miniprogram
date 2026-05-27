package com.campus.food.controller;

import com.campus.food.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping
    public Result<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        String dirPath = System.getProperty("user.dir") + uploadPath;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        file.transferTo(new File(dirPath + fileName));
        return Result.success("/upload/" + fileName);
    }
}
