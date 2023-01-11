package com.wyh.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyh.TakeOut.common.R;
import com.wyh.TakeOut.pojo.Category;
import com.wyh.TakeOut.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//分类管理
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //新增分类
    @PostMapping
    public R<String> add(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增成功");
    }

    /**
     * @description 分页查询分类信息
     */
    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize) {
        //构造分页构造器
        Page<Category> categoryPage = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Category::getSort);
        //执行查询
        categoryService.page(categoryPage,queryWrapper);

        return R.success(categoryPage);
    }

    //删除分类
    @DeleteMapping
    public R<String> delete(Long ids) {
        categoryService.deleteById(ids);
        return R.success("删除成功");
    }

    //修改分类
    @PutMapping
    public R<String> edit(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    //根据条件查询分类
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //查询
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
