package com.wyh.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyh.TakeOut.common.R;
import com.wyh.TakeOut.dto.DishDto;
import com.wyh.TakeOut.pojo.Category;
import com.wyh.TakeOut.pojo.Dish;
import com.wyh.TakeOut.pojo.DishFlavor;
import com.wyh.TakeOut.service.CategoryService;
import com.wyh.TakeOut.service.DishFlavorService;
import com.wyh.TakeOut.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    private final DishFlavorService dishFlavorService;
    private final DishService dishService;
    private final CategoryService categoryService;
    private final RedisTemplate redisTemplate;

    @Autowired
    public DishController(DishFlavorService dishFlavorService, DishService dishService, CategoryService categoryService, RedisTemplate redisTemplate) {
        this.dishFlavorService = dishFlavorService;
        this.dishService = dishService;
        this.categoryService = categoryService;
        this.redisTemplate = redisTemplate;
    }

    //新增菜品
    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        //缓存清理solution 1 - clean all
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //缓存清理solution 2 - 精确清理当前分类的缓存
        String key = "dish_" + dishDto.getCategoryId() + dishDto.getStatus();
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    //分页查询菜品
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> dishPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页
        dishService.page(dishPage,queryWrapper);
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDtoPage,"records");
        List<Dish> dishList = dishPage.getRecords();
        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    //根据id查询菜品和口味信息
    @GetMapping("/{id}")
    public R<DishDto> findById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    //新增菜品
    @PutMapping
    public R<String> edit(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        //缓存清理solution 1 - clean all
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //缓存清理solution 2 - 精确清理当前分类的缓存
        String key = "dish_" + dishDto.getCategoryId() + dishDto.getStatus();
        redisTemplate.delete(key);
        return R.success("修改菜品成功");
    }

    //根据条件查询菜品数据
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //设置变量存储菜品信息
        List<DishDto> dishDtoList;
        //以分类进行缓存 先获取分类id 动态构造key
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //先从redis中获取缓存数据
        dishDtoList = (List<DishDto>)redisTemplate.opsForValue().get(key);
        //存在就直接返回
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }
        //没有就执行查询 并缓存到Redis
        //构造查询条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(lambdaQueryWrapper);
        dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, id);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        //缓存到Redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
}
