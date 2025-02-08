package com.hanjiang.etm.common.config;//package com.hanjiang.etm.common.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
///**
// * 描述：序列化器
// *
// * @author huxuehao
// **/
//@Configuration
//@EnableWebMvc
//public class WebDataConvertConfig implements WebMvcConfigurer {
//    //@Bean
//    //public ObjectMapper objectMapper() {
//    //    ObjectMapper objectMapper = new ObjectMapper();
//    //
//    //    // 创建一个 SimpleModule
//    //    SimpleModule module = new SimpleModule();
//    //
//    //    // 注册 Long 类型的序列化器，将 Long 转换为 String
//    //    module.addSerializer(Long.class, ToStringSerializer.instance);
//    //    module.addSerializer(Long.TYPE, ToStringSerializer.instance);
//    //
//    //    // 注册模块
//    //    objectMapper.registerModule(module);
//    //
//    //    return objectMapper;
//    //}
//
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        //序列换成json时,将所有的long变成string
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
//        converters.add(jackson2HttpMessageConverter);
//    }
//}
