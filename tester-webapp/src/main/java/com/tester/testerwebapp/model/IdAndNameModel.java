package com.tester.testerwebapp.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class IdAndNameModel {
    private Long id;
    private String name;
//    @Email(message = "invalid email",regexp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
    @Email(message = "invalid email",regexp = "^(\\w+?)@(\\w+?)\\.([a-zA-Z]{2,})$")
    private String email;
}
