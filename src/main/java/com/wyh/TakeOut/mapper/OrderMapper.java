package com.wyh.TakeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyh.TakeOut.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}