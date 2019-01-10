package com.springboot.tools.controller;

import lombok.Builder;
import lombok.Data;

/**
 * @author kxd
 * @date 2018/12/27 14:59
 * description:
 */
@Data
@Builder
public class ResponseInfo {

    public String code;
    public Object mesg;
}
