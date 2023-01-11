package com.wyh.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wyh.TakeOut.common.R;
import com.wyh.TakeOut.pojo.User;
import com.wyh.TakeOut.service.UserService;
import com.wyh.TakeOut.utils.SendSms;
import com.wyh.TakeOut.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //发送验证码
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) {
        //获取手机号
        String phoneNumber = user.getPhone();
        //生成随机四位验证码
        if (phoneNumber != null) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            System.out.println("生成的验证码: "+code);
            //保存生成的验证码到Session
            httpSession.setAttribute(phoneNumber,code);
            //调用短信服务API发送短信验证码
            //SendSms.sendMessage(phoneNumber,code);

            return R.success("短信发送成功");
        }

        return R.error("短信发送失败");
    }

    //登录
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,String> map, HttpSession session) {
        //获取手机号
        String phone = map.get("phone");
        //获取验证码
        String code = map.get("code");
        //从Session中获取保存的验证码
        String SessionCode = (String)session.getAttribute(phone);
        //进行验证码比对
        if (SessionCode != null && SessionCode.equals(code)) {
            //比对成功则为登录成功
            //判断当前手机号的用户是否为新用户 新用户则自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}

