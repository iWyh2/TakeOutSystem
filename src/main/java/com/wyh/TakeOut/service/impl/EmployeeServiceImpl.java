package com.wyh.TakeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.TakeOut.mapper.EmployeeMapper;
import com.wyh.TakeOut.pojo.Employee;
import com.wyh.TakeOut.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
