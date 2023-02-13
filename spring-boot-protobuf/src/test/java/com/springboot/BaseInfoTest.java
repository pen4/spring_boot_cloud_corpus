package com.springboot;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.springboot.learnprotobuf.ProtostuffUtil;
import com.springboot.learnprotobuf.entity.MsgBody;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class BaseInfoTest {
    @Test
    void test() throws JsonFormat.ParseException, InvalidProtocolBufferException {

        MsgBody msgBody = MsgBody.newBuilder().setMsgInfo("abcd").setChannelId("12").build();
        //使用buf自带方式parse
        byte[] ser2 = msgBody.toByteArray();
        MsgBody msgBody2 = MsgBody.parseFrom(ser2);

        //使用stuff方式
        byte[] ser = ProtostuffUtil.serialize(msgBody);
        MsgBody de = ProtostuffUtil.deserialize(ser, MsgBody.class);

        //proto to json
        String json = JsonFormat.printToString(msgBody);
        MsgBody.Builder message = MsgBody.newBuilder();
        JsonFormat.merge(json, message);
    }
}
