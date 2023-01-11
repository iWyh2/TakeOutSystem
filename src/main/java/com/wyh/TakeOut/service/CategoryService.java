package com.wyh.TakeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyh.TakeOut.pojo.Category;

public interface CategoryService extends IService<Category> {
    void deleteById(Long id);
}
