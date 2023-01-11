package com.wyh.TakeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyh.TakeOut.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
