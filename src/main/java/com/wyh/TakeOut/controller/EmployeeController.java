package com.wyh.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wyh.TakeOut.common.R;
import com.wyh.TakeOut.pojo.Employee;
import com.wyh.TakeOut.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * @description 员工登录功能
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        //1. 将页面提交过来的密码进行MD5加密处理
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        //2. 将提交过来的用户名拿去查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(queryWrapper);
        //3. 没找到返回失败消息 找到了就对比密码
        if (one == null) {
            return R.error("用户不存在");
        }

        if (!one.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        //4. 查看员工状态 防止老六
        if (one.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //5. 登陆成功 返回成功消息 并将员工id存入session
        request.getSession().setAttribute("employee", one.getId());
        return R.success(one);
    }

    /**
     * @description 员工退出功能
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //消除session中存储的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * @description 新增员工功能
     */
    @PostMapping
    public R<String> add(@RequestBody Employee employee) {
        //设置初始密码 并进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //调用Service保存
        employeeService.save(employee);

        return R.success("新增员工成功");
    }


    /**
     * @description 分页查询员工信息
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Employee> employeePage = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //如果有条件就添加条件过滤 应该为模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(employeePage,queryWrapper);

        return R.success(employeePage);
    }


    /**
     * @description 编辑员工信息
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
        //执行更新操作
        employeeService.updateById(employee);

        return R.success("修改成功");
    }

    /**
     * @description 查询单个员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> find(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
