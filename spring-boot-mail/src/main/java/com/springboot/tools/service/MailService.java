package com.springboot.tools.service;


import cn.hutool.json.JSONUtil;
import com.google.common.base.Stopwatch;
import com.mongodb.util.JSON;
import com.springboot.tools.dao.MailDO;
import com.springboot.tools.dao.MailDao;
import com.springboot.tools.enums.MailType;
import com.springboot.tools.enums.SendStatusType;
import com.springboot.tools.util.consts.AppConst;
import com.springboot.tools.vo.MailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MailService {

    private MailDao mailDao;
    private JavaMailSender mailSender;

    @Autowired
    public MailService(MailDao mailDao, JavaMailSender mailSender) {
        this.mailDao = mailDao;
        this.mailSender = mailSender;
    }

    /**
     * 发送简单文本邮件
     *
     * @param ent 邮件信息
     **/
    public BizResult<String> sendSimpleMail(MailVO ent) {
        BizResult<String> bizResult = new BizResult<>(true, SendStatusType.SendSuccess, "");
        try {
            if (ent == null) {
                bizResult.setFail("邮件信息为空");
                return bizResult;
            }
            if (StringUtils.isEmpty(ent.getToAddress())) {
                bizResult.setFail("简单邮件，接收人邮箱为空");
                return bizResult;
            }

            //默认发件人设置
            if (StringUtils.isEmpty(ent.getFromAddress())) {
                ent.setFromAddress(ent.getFromAddress());
            }
            //spring SimpleMailMessage
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(ent.getFromAddress());
            message.setTo(ent.getToAddress());
            message.setCc(ent.getCcAddress());
            message.setBcc(ent.getBccAddress());
            message.setSubject(ent.getSubject());
            message.setText(ent.getMailBody());
            message.setSentDate(new Date());
            mailSender.send(message);
            bizResult.setSuccess("简单邮件已经发送");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送简单邮件时发生异常", e);
            bizResult.setFail(String.format("发送简单邮件时发生异常:%s", e));
        } finally {
            log.info("简单邮件，发送结果：{}", (bizResult));
        }
        return bizResult;
    }

    /**
     * 发送HTML邮件
     *
     * @param ent 邮件信息
     **/
    public BizResult<String> sendHtmlMail(MailVO ent) {
        BizResult<String> bizResult = new BizResult<>(true, SendStatusType.SendSuccess, "");
        try {
            if (ent == null) {
                bizResult.setFail("邮件信息为空");
                return bizResult;
            }
            if (StringUtils.isEmpty(ent.getToAddress())) {
                bizResult.setFail("HTML邮件，接收人邮箱为空");
                return bizResult;
            }
            //默认发件人设置
            if (StringUtils.isEmpty(ent.getFromAddress())) {
                ent.setFromAddress(AppConst.DEFAULT_SENDER);
            }

            MimeMessage message = mailSender.createMimeMessage();

            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(ent.getFromAddress());
            helper.setTo(ent.getToAddress());
            helper.setCc(ent.getCcAddress());
            helper.setBcc(ent.getBccAddress());
            helper.setSubject(ent.getSubject());
            //true表示是html邮件
            helper.setText(ent.getMailBody(), true);
            helper.setSentDate(new Date());

            //判断有无附件 循环添加附件
            if (ent.isHasAttatchment() && ent.getAttatchmentUrls() != null) {
                for (String filePath : ent.getAttatchmentUrls()) {
                    FileSystemResource file = new FileSystemResource(new File(filePath));
                    String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
                    helper.addAttachment(fileName, file);
                }
            }
            mailSender.send(message);
            bizResult.setSuccess("HTML邮件已经发送");
        } catch (Exception e) {
            log.error("发送HTML邮件时发生异常:", e);
            bizResult.setFail(String.format("发送HTML邮件时发生异常:%s", e));
        } finally {
            log.info(String.format("HTML邮件，发送结果：%s", (bizResult)));
        }
        return bizResult;
    }

    /**
     * 自动查询并发送邮件
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     **/
    public void autoSend(Date startTime, Date endTime) {
        Stopwatch stopWatch = Stopwatch.createStarted();
        List<MailDO> mailDOList = mailDao.findToSendList(startTime, endTime);

        for (MailDO dbEnt : mailDOList) {
            MailVO ent = new MailVO();
            BeanUtils.copyProperties(dbEnt, ent);
            BizResult<String> bizSendResult = null;

            if (MailType.TEXT.toString().equalsIgnoreCase(ent.getMailType())) {
                bizSendResult = sendSimpleMail(ent);
            } else if (MailType.HTML.toString().equalsIgnoreCase(ent.getMailType())) {
                bizSendResult = sendHtmlMail(ent);
            }
            if (bizSendResult == null) {
                bizSendResult = new BizResult<>(false, SendStatusType.SendSuccess, "不支持的邮件类型");
            }
            if (bizSendResult.isOk()) {
                dbEnt.setSendStatus(SendStatusType.SendSuccess.toString());
            } else {
                dbEnt.setSendStatus(SendStatusType.SendFail.toString());
            }
            //重试次数+1
            dbEnt.setRetryCount(dbEnt.getRetryCount() + 1);
            dbEnt.setRemark(JSONUtil.toJsonStr(bizSendResult));
            dbEnt.setModifyTime(new Date());
            dbEnt.setModifyUser("QuartMailTask");
            mailDao.update(dbEnt);
        }
        stopWatch.stop();
        log.info("本次共处理记录数：{}，总耗时：{}", mailDOList.size(), stopWatch.elapsed(TimeUnit.MILLISECONDS));

    }
}
