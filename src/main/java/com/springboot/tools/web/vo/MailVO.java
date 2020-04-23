package com.springboot.tools.web.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class MailVO {

    private String mailId;

    private Long mailNo;

    /**
     * 邮件类型 如：Text表示纯文本、HTML等
     */
    private String mailType;

    /**
     * 邮件发送人
     */
    private String fromAddress;

    /**
     * 邮件接收人
     */
    private String toAddress;

    /**
     * CC邮件接收人
     */
    private String ccAddress;

    /**
     * BC邮件接收人
     */
    private String bccAddress;

    /**
     * 邮件标题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String mailBody;

    /**
     * 发送优先级 如：Normal表示普通
     */
    private String sendPriority;

    /**
     * 处理状态 如：SendWait表示等待发送
     */
    private String sendStatus;

    /**
     * 是否有附件
     */
    private boolean hasAttatchment;

    /**
     * 附件保存的绝对地址，如fastdfs返回的url
     */
    private String[] attatchmentUrls;

    /**
     * 客户端应用编号或名称  如：CRM、订单、财务、运营等
     */
    private String clientAppId;

    /**
     * 是否删除
     */
    private boolean hasDelete;

    /**
     * 发送次数
     */
    private int retryCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 更新人
     */
    private String modifyUser;

    /**
     * 备注
     */
    private String remark;

    /**
     * 扩展信息
     */
    private String extendInfo;

    private boolean sync;
}
