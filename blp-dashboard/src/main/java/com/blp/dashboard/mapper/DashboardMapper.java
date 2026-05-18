package com.blp.dashboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface DashboardMapper {

    @Select("SELECT COALESCE(COUNT(*), 0) FROM oms_order WHERE DATE(create_time) = CURDATE()")
    Long todayOrderCount();

    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM oms_order WHERE DATE(create_time) = CURDATE()")
    BigDecimal todayRevenue();

    @Select("SELECT COUNT(*) FROM pms_product WHERE status = 1")
    Long productCount();

    @Select("SELECT COUNT(*) FROM sys_user WHERE status = 1")
    Long userCount();
}
