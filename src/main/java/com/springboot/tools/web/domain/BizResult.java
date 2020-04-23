package com.springboot.tools.web.domain;

import com.springboot.tools.web.enums.SendStatusType;
import lombok.Data;

@Data
public class BizResult<T> {
    private SendStatusType message;
    private boolean ok;
    private T data;
    private String fail;

    private String success;

    public BizResult(boolean ok, SendStatusType message, T data) {
        this.ok = ok;
        this.message = message;
        this.data = data;
    }
}
