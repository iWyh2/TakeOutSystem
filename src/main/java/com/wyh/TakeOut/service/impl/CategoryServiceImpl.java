package com.wyh.TakeOut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.TakeOut.common.CustomException;
import com.wyh.TakeOut.mapper.CategoryMapper;
import com.wyh.TakeOut.pojo.Category;
import com.wyh.TakeOut.pojo.Dish;
import com.wyh.TakeOut.pojo.Setmeal;
import com.wyh.TakeOut.service.CategoryService;
import com.wyh.TakeOut.service.DishService;
import com.wyh.TakeOut.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    private final DishService dishService;
    private final SetmealService setmealService;

    @Autowired
    public CategoryServiceImpl(DishService dishService, SetmealService setmealService) {
        this.dishService = dishService;
        this.setmealService = setmealService;
    }

    @Override
    public void deleteById(Long id) {
        //查询当前分类是否包含了菜品
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("当前分类关联了菜品，无法删除");
        }
        //查询分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        count = setmealService.count(setmealLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("当前分类关联了套餐，无法删除");
        }
        //都没有就直接删除
        super.removeById(id);
    }
}
