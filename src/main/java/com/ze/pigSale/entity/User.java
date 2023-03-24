package com.ze.pigSale.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @TableName user
 */
@Data
public class User implements Serializable {
    private Long userId;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String name;

    private String gender;

    private LocalDate birthday;

    private Integer role;

    private String image;

}