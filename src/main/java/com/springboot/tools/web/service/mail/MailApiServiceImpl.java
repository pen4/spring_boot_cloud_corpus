package com.springboot.tools.web.service.mail;

import com.google.common.collect.Lists;
import com.springboot.tools.util.ConfigUtil;
import com.springboot.tools.util.SerialNumberUtil;
import com.springboot.tools.web.consts.AppConst;
import com.springboot.tools.web.dao.MailDao;
import com.springboot.tools.web.domain.BizResult;
import com.springboot.tools.web.domain.MailDO;
import com.springboot.tools.web.enums.MailType;
import com.springboot.tools.web.enums.SendPriorityType;
import com.springboot.tools.web.enums.SendStatusType;
import com.springboot.tools.web.vo.BatchSendEmailRequest;
import com.springboot.tools.web.vo.BatchSendEmailResponse;
import com.springboot.tools.web.vo.MailVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class MailApiServiceImpl implements MailApiService {

    @Autowired
    private MailService mailService;

    @Autowired
    private MailDao mailDao;

    /**
     * 发送邮件
     *
     * @param request 请求
     * @return 发送失败的邮件
     **/
    public BatchSendEmailResponse batchSendEmail(BatchSendEmailRequest request) {
        Assert.notNull(request, "请求request为空！");
        BatchSendEmailResponse response = new BatchSendEmailResponse();
        response.setSuccess("");

        if (request.getMailList() == null || request.getMailList().size() == 0) {
            response.setFail("待发送邮件为空");
        }
        if (response.isOk()) {
            return response;
        }
        //构造邮件对象
        List<MailVO> allMails = generateMails(request);
        List<MailVO> failedMails = processSendMail(allMails);
        response.setFailMailList(failedMails);
        response.setSuccess(String.format("发送邮件提交成功，发送失败的记录为：%d", failedMails.size()));
        return response;
    }

    /**
     * 构造待发送邮件 特殊字段赋值
     *
     * @param request 请求
     * @return 发送失败的邮件
     **/
    private List<MailVO> generateMails(BatchSendEmailRequest request) {
        List<MailVO> allMails = Lists.newArrayList();

        for (MailVO mail : request.getMailList()) {
            if (mail == null) {
                continue;
            }
            //默认字段赋值
            mail.setCreateTime(new Date());
            mail.setModifyTime(new Date());
            mail.setRetryCount(0);
            mail.setHasDelete(false);
            mail.setMailNo(SerialNumberUtil.create());
            if (StringUtils.isEmpty(mail.getMailType())) {
                mail.setMailType(MailType.TEXT.toString());
            } else if (Arrays.stream(MailType.values()).filter(x -> x.toString().equalsIgnoreCase(mail.getMailType())).count() == 0) {
                mail.setMailType(MailType.TEXT.toString());
            }
            if (StringUtils.isEmpty(mail.getSendStatus())) {
                mail.setSendStatus(SendStatusType.SendWait.toString());
            } else if (Arrays.stream(SendStatusType.values()).filter(x -> x.toString().equalsIgnoreCase(mail.getSendStatus())).count() == 0) {
                mail.setSendStatus(SendStatusType.SendWait.toString());
            }
            if (StringUtils.isEmpty(mail.getSendPriority())) {
                mail.setSendPriority(SendPriorityType.Normal.toString());
            } else if (Arrays.stream(SendPriorityType.values()).filter(x -> x.toString().equalsIgnoreCase(mail.getSendPriority())).count() == 0) {
                mail.setSendPriority(SendPriorityType.Normal.toString());
            }

            if (StringUtils.isEmpty(mail.getMailId())) {
                mail.setMailId(String.valueOf(SerialNumberUtil.create()));
            }

            if (StringUtils.isEmpty(mail.getFromAddress())) {
                String fromAddr = ConfigUtil.getConfigVal(AppConst.MAIL_SENDER_ADDR);
                mail.setFromAddress(fromAddr);
            }
            allMails.add(mail);
        }
        return allMails;
    }

    /**
     * 处理邮件
     *
     * @param allMails 所有邮件
     * @return 发送失败的邮件
     **/
    private List<MailVO> processSendMail(List<MailVO> allMails) {
        //没有处理成功的邮件
        List<MailVO> failedMails = Lists.newArrayList();
        //待异步处理的邮件
        List<MailVO> asyncMails = Lists.newArrayList();
        for (MailVO mail : allMails) {
            //异步处理
            if (mail.isSync() == false) {
                continue;
            }
            //同步调用
            //发送邮件成功
            BizResult<String> bizResult = safeSendMail(mail);
            if (bizResult.isOk() == true) {
                mail.setSendStatus(SendStatusType.SendSuccess.toString());
                mail.setRemark("同步发送邮件成功");
            } else {
                mail.setSendStatus(SendStatusType.SendFail.toString());
                mail.setRemark(String.format("同步发送邮件失败:%s", bizResult.getMessage()));
                failedMails.add(mail);
            }
        }
        //批量保存邮件至MongoDB
        safeStoreMailList(allMails);
        return failedMails;
    }

    /**
     * 发送邮件
     *
     * @param ent 邮件信息
     * @return
     **/
    private BizResult<String> safeSendMail(MailVO ent) {
        BizResult<String> bizSendResult = null;
        if (MailType.TEXT.toString().equalsIgnoreCase(ent.getMailType())) {
            bizSendResult = mailService.sendSimpleMail(ent);
        } else if (MailType.HTML.toString().equalsIgnoreCase(ent.getMailType())) {
            bizSendResult = mailService.sendHtmlMail(ent);
        }

        if (bizSendResult == null) {
            bizSendResult = new BizResult(false, SendStatusType.SendSuccess, "不支持的邮件类型");
        }

        return bizSendResult;
    }

    /**
     * 批量保存邮件
     *
     * @param entList 邮件信息列表
     * @return
     **/
    private boolean safeStoreMailList(List<MailVO> entList) {
        boolean isOK = storeMailList(entList);

        if (isOK == true) {
            return isOK;
        }

        for (int i = 1; i <= AppConst.MAX_RETRY_COUNT; i++) {
            try {
                Thread.sleep(100 * i);
            } catch (Exception te) {
                te.printStackTrace();
            }

            isOK = storeMailList(entList);

            if (isOK == true) {
                break;
            }
        }

        return isOK;
    }

    /**
     * 存储邮件
     *
     * @param entList 邮件信息列表
     * @return
     **/
    private boolean storeMailList(List<MailVO> entList) {
        boolean isOK = false;

        try {

            List<MailDO> dbEntList = Lists.newArrayList();
            entList.forEach(
                    x -> {
                        MailDO dbEnt = new MailDO();
                        BeanUtils.copyProperties(x, dbEnt);
                        dbEntList.add(dbEnt);
                    });
            mailDao.batchInsert(dbEntList);
            isOK = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOK;
    }
}
