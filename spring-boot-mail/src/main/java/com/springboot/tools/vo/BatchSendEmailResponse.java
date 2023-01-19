package com.springboot.tools.vo;

import lombok.Data;

import java.util.List;

@Data
public class BatchSendEmailResponse {
    private String success;
    private String fail;
    private boolean ok;

   private List<MailVO> failMailList;

}
