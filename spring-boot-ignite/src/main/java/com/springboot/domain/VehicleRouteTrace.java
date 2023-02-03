package com.springboot.domain;

import org.locationtech.jts.geom.Geometry;

import java.sql.Timestamp;

/**
 * @author: User
 * @date: 2023/1/12
 * @Description:此类用于xxx
 */

public class VehicleRouteTrace {

    private String vin;

    private Geometry trace;

    private Timestamp ctTime;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Geometry getTrace() {
        return trace;
    }

    public void setTrace(Geometry trace) {
        this.trace = trace;
    }

    public Timestamp getCtTime() {
        return ctTime;
    }

    public void setCtTime(Timestamp ctTime) {
        this.ctTime = ctTime;
    }
}
