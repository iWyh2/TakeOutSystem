package com.wyh.TakeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyh.TakeOut.dto.SetmealDto;
import com.wyh.TakeOut.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐，同时保存套餐和菜品的关系
    void saveWithDish(SetmealDto setmealDto);
    //删除套餐 同时删除套餐和菜品的关联信息
    void removeWithDish(List<Long> ids);
}
