package com.springboot.tools.dao;

import java.util.Date;
import java.util.List;

public interface MailDao {

    /**
     * 批量创建对象
     *
     * @param entList
     */
    void batchInsert(List<MailDO> entList);

    /**
     * 创建对象
     *
     * @param ent
     */
    void insert(MailDO ent);

    /**
     * 根据ID查询对象
     *
     * @param mailId
     * @return
     */
    MailDO findByMailId(Long mailId);

    /**
     * 查询一段时间范围内待发送的邮件
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<MailDO> findToSendList(Date startTime, Date endTime);

    /**
     * 更新
     *
     * @param ent
     */
    void update(MailDO ent);

    /**
     * 删除
     *
     * @param mailId
     */
    void delete(Long mailId);
}
