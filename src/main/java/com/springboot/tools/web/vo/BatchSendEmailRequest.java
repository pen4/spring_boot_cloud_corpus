package com.springboot.tools.web.vo;

import lombok.Data;

import java.util.List;

@Data
public class BatchSendEmailRequest {

    private List<MailVO> mailList;
}
