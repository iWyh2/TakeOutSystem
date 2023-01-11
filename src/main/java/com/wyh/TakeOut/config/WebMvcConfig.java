package com.wyh.TakeOut.config;

import com.wyh.TakeOut.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //扩展mvc框架的消息转换器
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器 用jackson将Java转JSON
        converter.setObjectMapper(new JacksonObjectMapper());
        //将设置的转换器追加到mvc框架的转换器集合中 注意要放在第一个 优先使用
        converters.add(0,converter);
    }
}
