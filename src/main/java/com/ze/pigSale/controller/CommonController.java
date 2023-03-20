package com.ze.pigSale.controller;


import com.ze.pigSale.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * author: zebii
 * Date: 2023-01-30-20:59
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${pigSale.path}")
    private String basePath;

    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            //获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名：" + fileName);
            //获取文件后缀名
            assert fileName != null;
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("文件后缀名：" + suffixName);
            //设置文件存储路径
            String filePath = basePath;
            String path = filePath + fileName;
            File dest = new File(path);
            //检测是否存在该目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            //写入文件
            file.transferTo(dest);
            return "上传成功！";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    @GetMapping("/download")
    public Result<String> download(String filename, HttpServletResponse response) {
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        File file = new File(basePath + filename);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(basePath + filename);
                outputStream = response.getOutputStream();

                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + filename);// 设置文件名
                int len = 0;
                byte[] bytes = new byte[1024];
                while ((len = fileInputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                    outputStream.flush();
                }
                return Result.success("图片下载成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        return Result.error("图片下载失败");
    }

}
