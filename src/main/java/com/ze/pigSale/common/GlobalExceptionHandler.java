package com.ze.pigSale.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * author: zebii
 * Date: 2023-01-29-16:10
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已经存在";
            return Result.error(msg);
        }else if (ex.getMessage().contains("cannot be null")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[1] + "为空";
            return Result.error(msg);
        }
        return Result.error("未知错误");
    }

    @ExceptionHandler(FileNotFoundException.class)
    public Result<String> fileNotFoundException(){
        return Result.error("找不到指定文件");
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> CustomException(CustomException customException){
        return Result.error(customException.getMessage());
    }
}
