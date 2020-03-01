package com.xurent.live.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class MvcConfig  implements WebMvcConfigurer {


    static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };

    @Value(value = "${liveDance.file.uploadpath}")
    private  String tempPath;//文件开放


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ///创建静态资源路径
        String urlPath=System.getProperty("user.dir");
        String temp;
        //Linux下转换
        if(urlPath.equals(File.separator)){
            temp=tempPath;
        }else{
            temp=urlPath+tempPath;
        }
        File fp=new File(urlPath);
        if(!fp.exists()){
            System.out.println(fp.mkdirs()); ;
        }
        registry.addResourceHandler("/liveDance/**").addResourceLocations("file:"+temp);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        System.out.println("---->"+temp);
    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Content-Type","X-Requested-With","accept,Origin","Access-Control-Request-Method","Access-Control-Request-Headers","token")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowCredentials(true);
    }


}
