package com.wyh;

import com.wyh.TakeOut.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement//开启事务管理功能
@EnableCaching//开启注解缓存功能
public class TakeOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(TakeOutApplication.class,args);
    }
}
