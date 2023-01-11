package com.wyh.TakeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wyh.TakeOut.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
