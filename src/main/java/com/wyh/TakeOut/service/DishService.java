package com.wyh.TakeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyh.TakeOut.dto.DishDto;
import com.wyh.TakeOut.pojo.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品 同时新增菜品的口味 同时操作两张表
    void saveWithFlavor(DishDto dishDto);
    //根据id查询菜品和口味信息
    DishDto getByIdWithFlavor(Long id);
    void updateWithFlavor(DishDto dishDto);
}
