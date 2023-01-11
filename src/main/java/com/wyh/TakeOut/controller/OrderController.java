package com.wyh.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyh.TakeOut.common.R;
import com.wyh.TakeOut.dto.OrdersDto;
import com.wyh.TakeOut.dto.SetmealDto;
import com.wyh.TakeOut.pojo.Category;
import com.wyh.TakeOut.pojo.Orders;
import com.wyh.TakeOut.pojo.Setmeal;
import com.wyh.TakeOut.service.OrderService;
import com.wyh.TakeOut.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    //分页查询订单 - 移动端
    @GetMapping("/userPage")
    public R<Page<Orders>> page(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(Orders::getUserId, userId);
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        orderService.page(ordersPage,queryWrapper);
        return R.success(ordersPage);
    }

    //分页查询订单 - 后台
    @GetMapping("/page")
    public R<Page<OrdersDto>> pages(int page, int pageSize, String number) {
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number != null, Orders::getNumber, number);
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        orderService.page(ordersPage,queryWrapper);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage, ordersDtoPage,"records");
        List<Orders> ordersList = ordersPage.getRecords();
        List<OrdersDto> ordersDtoList = ordersList.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            String userName = item.getUserName();
            ordersDto.setUserName(userName);
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }
}