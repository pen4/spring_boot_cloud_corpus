package com.springboot.tools.dao;

import com.google.common.collect.Lists;
import com.springboot.tools.enums.SendStatusType;
import com.springboot.tools.util.consts.AppConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MailDaoImpl implements MailDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void batchInsert(List<MailDO> entList) {
        //分组批量多次插入 每次2000条
        List<List<MailDO>> groupList = Lists.partition(entList, AppConst.BATCH_RECORD_COUNT);
        for (List<MailDO> list : groupList) {
            mongoTemplate.insert(list, MailDO.class);
        }
    }

    @Override
    public void insert(MailDO ent) {
        mongoTemplate.save(ent);

    }

    @Override
    public MailDO findByMailId(Long mailId) {
        Query query = new Query(Criteria.where("mailId").is(mailId));
        MailDO ent = mongoTemplate.findOne(query, MailDO.class);
        return ent;
    }

    /**
     * 查询一段时间范围内待发送的邮件
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @Override
    public List<MailDO> findToSendList(Date startTime, Date endTime) {
        Query query = new Query(Criteria.where("create_time").gte(startTime).lt(endTime)
                .and("has_delete").is(Boolean.FALSE)
                .and("send_status").ne(SendStatusType.SendSuccess.toString())
                //重试次数小于3的记录
                .and("retry_count").lt(AppConst.MAX_RETRY_COUNT))
                //每次取20条
                .limit(AppConst.RECORD_COUNT);

        List<MailDO> entList = mongoTemplate.find(query, MailDO.class);
        return entList;
    }

    @Override
    public void update(MailDO ent) {
        Query query = new Query(Criteria.where("_id").is(ent.getMailId()));
        Update update = new Update()
                .set("send_status", ent.getSendStatus())
                .set("retry_count", ent.getRetryCount())
                .set("remark", ent.getRemark())
                .set("modify_time", ent.getModifyTime())
                .set("modify_user", ent.getModifyUser());
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, MailDO.class);
    }

    @Override
    public void delete(Long mailId) {
        Query query = new Query(Criteria.where("_id").is(mailId));
        mongoTemplate.remove(query, MailDO.class);
    }
}
