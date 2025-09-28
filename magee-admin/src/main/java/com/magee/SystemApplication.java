package com.magee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableCaching
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SystemApplication  extends SpringBootServletInitializer {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(SystemApplication.class, args);

        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        String ip = InetAddress.getLocalHost().getHostAddress();
        String path =  env.getProperty("server.servlet.context-path");
        log.info("\n(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "swagger: http://" + ip + ":" + port + path + "/doc.html");

    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return  builder.sources(SystemApplication.class);
    }
}
