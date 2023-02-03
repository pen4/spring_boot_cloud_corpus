package com.springboot.domain;

import java.util.List;

/**
 * @author: User
 * @date: 2023/1/16
 * @Description:此类用于xxx
 */

public class MyPonit {

    public List<List<Double>> getGps_waypoints() {
        return gps_waypoints;
    }

    public void setGps_waypoints(List<List<Double>> gps_waypoints) {
        this.gps_waypoints = gps_waypoints;
    }

    List<List<Double>> gps_waypoints;

}
