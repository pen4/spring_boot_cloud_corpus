package com.springboot.service;

import com.springboot.domain.VehicleRouteTrace;
import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author: User
 * @date: 2023/1/12
 * @Description:此类用于车辆规划轨迹存储
 */
@RepositoryConfig(cacheName = "VehicleRouteTrace" ,autoCreateCache = true)
public interface VehicleRouteTraceRepository extends IgniteRepository<VehicleRouteTrace,String > {

}
