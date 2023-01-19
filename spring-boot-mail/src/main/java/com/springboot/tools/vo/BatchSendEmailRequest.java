package com.springboot.tools.vo;

import lombok.Data;

import java.util.List;

@Data
public class BatchSendEmailRequest {

    private List<MailVO> mailList;
}
