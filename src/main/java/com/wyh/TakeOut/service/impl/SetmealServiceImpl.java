package com.wyh.TakeOut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.TakeOut.common.CustomException;
import com.wyh.TakeOut.dto.SetmealDto;
import com.wyh.TakeOut.mapper.SetmealMapper;
import com.wyh.TakeOut.pojo.Setmeal;
import com.wyh.TakeOut.pojo.SetmealDish;
import com.wyh.TakeOut.service.SetmealDishService;
import com.wyh.TakeOut.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    private final SetmealDishService setmealDishService;

    @Autowired
    public SetmealServiceImpl(SetmealDishService setmealDishService) {
        this.setmealDishService = setmealDishService;
    }

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存菜品基本信息
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存菜品和套餐的关联关系
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态 是否为停售
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        //不可以删除 则抛出异常
        if (count > 0) {
            throw new CustomException("有正在售卖套餐，无法删除");
        }
        //可以删除 则删除套餐
        this.removeByIds(ids);
        //删除关联数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }
}
