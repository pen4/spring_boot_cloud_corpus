package com.springboot.tools.dao.mapper;

import com.springboot.tools.model.Vehicle;
import com.springboot.tools.service.inter.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class VehicleMapper implements RowMapper<Vehicle> {


    @Override
    public Vehicle mapRow(Result result, int rowNum) throws Exception {
        Vehicle vehicle = new Vehicle();
        byte[] val = result.getValue(Bytes.toBytes("0"), Bytes.toBytes("f1"));
        byte[] val2 = result.getValue(Bytes.toBytes("0"), Bytes.toBytes("f2"));
        vehicle.setSim(Bytes.toString(val));
        vehicle.setVin(Bytes.toString(val2));
        byte[] row = result.getRow();
        vehicle.setRowKey(Bytes.toString(row));
        return vehicle;
    }
}
