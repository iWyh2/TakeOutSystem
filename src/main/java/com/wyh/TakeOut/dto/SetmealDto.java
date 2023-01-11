package com.wyh.TakeOut.dto;

import com.wyh.TakeOut.pojo.Setmeal;
import com.wyh.TakeOut.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
