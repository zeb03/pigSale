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

package com.ze.pigSale.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

import static com.ze.pigSale.constants.ExceptionConstants.*;

/**
 * @author: zebii
 * Date: 2023-01-29-16:10
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        if (ex.getMessage().contains(DUPLICATE_ENTRY)) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已经存在";
            return Result.error(msg);
        } else if (ex.getMessage().contains(CANNOT_BE_NULL)) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[1] + "为空";
            return Result.error(msg);
        }
        return Result.error(UNKNOWN_FILE);
    }

    @ExceptionHandler(org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException.class)
    public Result<String> fileSizeLimitExceededException() {
        return Result.error(FILE_SIZE_EXCEED);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public Result<String> fileNotFoundException() {
        return Result.error(FILE_CANNOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> customException(CustomException customException) {
        return Result.error(customException.getMessage());
    }
}
