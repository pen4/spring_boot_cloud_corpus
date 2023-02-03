package com.springboot.vo;

import java.util.List;

/**
 * @author: User
 * @date: 2023/1/16
 * @Description:此类用于xxx
 */
public class VehicleRouteTraceVO {
    private String vin;

    List<PointVO> trace;

    public List<PointVO> getTrace() {
        return trace;
    }

    public void setTrace(List<PointVO> trace) {
        this.trace = trace;
    }
}
