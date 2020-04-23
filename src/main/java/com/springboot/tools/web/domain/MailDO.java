package com.springboot.tools.web.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "mailinfo")
public class MailDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  唯一主键
     */

    @Id
    @Field("mail_id")
    private String mailId;

    @Field("mail_no")
    private Long mailNo;

    /**
     * 邮件类型 如：Text表示纯文本、HTML等
     */
    @Field("mail_type")
    private String mailType;

    /**
     * 邮件发送人
     */
    @Field("from_address")
    private String fromAddress;

    /**
     * 邮件接收人
     */
    @Field("to_address")
    private String toAddress;

    /**
     * CC邮件接收人
     */
    @Field("cc_address")
    private String ccAddress;

    /**
     * BC邮件接收人
     */
    @Field("bcc_address")
    private String bccAddress;

    /**
     * 邮件标题
     */
    @Field("subject")
    private String subject;

    /**
     * 邮件内容
     */
    @Field("mail_body")
    private String mailBody;

    /**
     * 发送优先级 如：Normal表示普通
     */
    @Field("send_priority")
    private String sendPriority;

    /**
     * 处理状态 如：SendWait表示等待发送
     */
    @Field("send_status")
    private String sendStatus;

    /**
     * 是否有附件
     */
    @Field("has_attachment")
    private boolean hasAttatchment;

    /**
     * 附件保存的绝对地址，如fastdfs返回的url
     */
    @Field("attatchment_urls")
    private String[] attatchmentUrls;

    /**
     * 客户端应用编号或名称  如：CRM、订单、财务、运营等
     */
    @Field("client_appid")
    private String clientAppId;

    /**
     * 是否删除
     */
    @Field("has_delete")
    private boolean hasDelete;

    /**
     * 发送次数
     */
    @Field("retry_count")
    private int retryCount;

    /**
     * 创建时间
     */
    @Field("create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @Field("create_user")
    private String createUser;

    /**
     * 更新时间
     */
    @Field("modify_time")
    private Date modifyTime;

    /**
     * 更新人
     */
    @Field("modify_user")
    private String modifyUser;

    /**
     * 备注
     */
    @Field("remark")
    private String remark;

    /**
     * 扩展信息
     */
    @Field("extend_info")
    private String extendInfo;
}
