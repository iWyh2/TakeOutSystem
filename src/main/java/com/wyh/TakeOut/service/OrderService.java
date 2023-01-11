package com.wyh.TakeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wyh.TakeOut.pojo.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     */
    public void submit(Orders orders);
}
