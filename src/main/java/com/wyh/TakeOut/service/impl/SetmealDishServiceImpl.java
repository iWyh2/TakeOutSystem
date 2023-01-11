package com.wyh.TakeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.TakeOut.dto.SetmealDto;
import com.wyh.TakeOut.mapper.SetmealDishMapper;
import com.wyh.TakeOut.pojo.SetmealDish;
import com.wyh.TakeOut.service.SetmealDishService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
