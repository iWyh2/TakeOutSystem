package com.wyh.TakeOut.dto;

import com.wyh.TakeOut.pojo.Dish;
import com.wyh.TakeOut.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * @description DTO (Data Transfer Object) - 数据传输对象
 *              用于展示层和服务层之间的数据传输
 */

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
