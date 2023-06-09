package com.my.plus.gen.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.my.plus.gen.entity.GenTableColumn;
import com.my.plus.gen.service.BaseMapperPlus;


import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author geovis
 */
@InterceptorIgnore(dataPermission = "true")
public interface GenTableColumnMapper extends BaseMapperPlus<GenTableColumnMapper, GenTableColumn, GenTableColumn> {
    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    List<GenTableColumn> selectDbTableColumnsByName(String tableName);

}
