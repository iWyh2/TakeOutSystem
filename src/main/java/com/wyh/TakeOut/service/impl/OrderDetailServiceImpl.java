package com.wyh.TakeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.TakeOut.pojo.OrderDetail;
import com.wyh.TakeOut.mapper.OrderDetailMapper;
import com.wyh.TakeOut.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}