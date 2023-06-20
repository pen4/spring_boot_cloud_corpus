package com.conn.tool.websockets;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimestampAndNumber {
    public static void main(String[] args) {
        //关于1686636944423和1686633388.719968的区别
        Number number = 1686633388.719968;
        Timestamp ts = toTimestamp(number);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String str = formatter.format(ts.toLocalDateTime());
        String str2 = formatter.format(LocalDateTime.now());
        Double a = 2d;
        Double b = a * 1000;
        System.out.println(str);
        System.out.println(str2);
        System.out.println(new Date().getTime());

    }

    /**
     * 这里丢失毫秒是因为直接使用了 timestamp longValue
     * @param timestamp
     * @return
     */

    public static Timestamp toTimestamp(Number timestamp) {
        System.out.println("数字1:"+timestamp.longValue());
        System.out.println("数字2:"+timestamp.doubleValue());
        Double tmp = timestamp.doubleValue() * 1000;
        System.out.println("数字3:"+tmp);
        return timestamp == null ? null : new Timestamp(tmp.longValue());
    }

}
