package com.springboot.tools.controller;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author kxd
 * @date 2018/12/21 20:03
 * description:
 */
@Data
public class FileMessage {
    @NotNull
    String filePath;
    String fileName;
}
