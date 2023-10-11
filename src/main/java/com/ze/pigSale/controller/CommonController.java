/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ze.pigSale.controller;

import cn.hutool.core.io.FileUtil;
import com.ze.pigSale.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * author: zebii
 * Date: 2023-01-30-20:59
 *
 * @author ze
 */
@CrossOrigin
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    // @Value("${server.ip}")
    // private String serverIp;
    // @Value("${server.port}")
    // private String serverPort;
    // 写在配置文件
    @Value("${pigSale.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result<String> upload(@RequestPart("file") MultipartFile file) {
        log.info("{}", file);
        try {
            if (file.isEmpty()) {
                return Result.error("文件为空");
            }
            // 生成文件名 uuid
            String uuid = UUID.randomUUID().toString();
            // 获取文件后缀名
            String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            // 获取文件名
            String fileName = uuid + suffixName;
            log.info("生成的文件名：" + fileName);

            // 设置文件存储路径
            String path = basePath + fileName;

            File dest = new File(path);
            log.info("文件路径：" + dest);
            // 检测是否存在该目录
            if (!dest.exists()) {
                dest.mkdirs();
            }
            // 写入文件
            file.transferTo(dest);
            // String url = "http://" + serverIp + ":" + serverPort + "/common/views?filename=" + fileName;
            String url = fileName;
            return Result.success(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.error("上传失败");
    }

    @GetMapping("/download")
    public void download(String filename, HttpServletResponse response) throws IOException {
        log.info("basePath: " + basePath);
        String[] split = filename.split("/");
        String name = split[split.length - 1];

        File file = new File(basePath + filename);
        if (file.exists()) {
            // log.info("下载图片文件名：" + basePath + filename);
            ServletOutputStream outputStream = response.getOutputStream();

            // 设置输出流格式
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));

            // 任意类型的二进制流数据
            response.setContentType("application/octet-stream");
            // 读取文件字节流
            outputStream.write(FileUtil.readBytes(file));
            outputStream.flush();
            outputStream.close();
        }
    }
}
