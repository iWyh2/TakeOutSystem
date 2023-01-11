package com.wyh.TakeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.TakeOut.mapper.UserMapper;
import com.wyh.TakeOut.pojo.User;
import com.wyh.TakeOut.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
