package com.wyh.TakeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.TakeOut.mapper.DishFlavorMapper;
import com.wyh.TakeOut.pojo.DishFlavor;
import com.wyh.TakeOut.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
