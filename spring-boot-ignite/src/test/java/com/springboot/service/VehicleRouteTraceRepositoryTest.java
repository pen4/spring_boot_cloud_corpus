package com.springboot.service;


import cn.hutool.json.JSONUtil;
import com.springboot.domain.MyPonit;
import com.springboot.domain.VehicleRouteTrace;
import com.springboot.service.VehicleRouteTraceRepository;
import com.springboot.vo.PointVO;
import com.springboot.vo.VehicleRouteTraceVO;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleRouteTraceRepositoryTest {

    @Resource
    VehicleRouteTraceRepository vehicleRouteTraceRepository;

    @Test
    public void test01() {
        VehicleRouteTrace routeTrace = new VehicleRouteTrace();
        String json = "{\n" + "    \"gps_waypoints\": [\n" + "        [1.10015269535667, 39.65384400273988],\n" + "        [116.09975980622085, 39.653835341941566],\n" + "        [116.09964537959492, 39.653831357784576],\n" + "        [116.10032784319255, 39.65667667322912]\n" + "    ]\n" + "}";
        MyPonit myPoint = JSONUtil.toBean(json, MyPonit.class);
        List<Coordinate> coordinateList = new ArrayList<>();
        for (List<Double> point : myPoint.getGps_waypoints()) {
            Coordinate coordinate = new Coordinate(point.get(0), point.get(1));
            coordinateList.add(coordinate);
        }
        CoordinateSequence coordinateSequence = CoordinateArraySequenceFactory.instance().create(CoordinateArrays.toCoordinateArray(coordinateList));
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry lineString = geometryFactory.createLineString(coordinateSequence);
        routeTrace.setVin("tset");
        routeTrace.setCtTime(Timestamp.from(Instant.now()));
        lineString.getBoundary();
        routeTrace.setTrace(lineString);
        vehicleRouteTraceRepository.save(routeTrace.getVin(), routeTrace);
    }


    @Test
    public void test02() {
        Iterable<VehicleRouteTrace> res = vehicleRouteTraceRepository.findAll();
        for (VehicleRouteTrace v : res) {
            Geometry geo = v.getTrace();
            Coordinate[] cos = geo.getCoordinates();
            VehicleRouteTraceVO traceVO = new VehicleRouteTraceVO();
            List<PointVO> vos = Lists.newArrayList();
            for (Coordinate c : cos) {
                PointVO pvo = new PointVO();
                pvo.setX(c.getX());
                pvo.setY(c.getY());
                vos.add(pvo);
            }
            traceVO.setTrace(vos);
            System.out.println(geo);
            System.out.println(JSONUtil.toJsonStr(traceVO));
            String vin = v.getVin();
            System.out.println(vin);
            Timestamp ts = v.getCtTime();
            System.out.println(ts.toLocalDateTime().format(DateTimeFormatter.ISO_DATE));
        }

    }

}