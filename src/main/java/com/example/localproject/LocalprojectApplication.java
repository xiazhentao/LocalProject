package com.example.localproject;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LocalprojectApplication {

    @Value("${http.port}")
    private int httpPort;


    public static void main(String[] args) {
        SpringApplication.run(LocalprojectApplication.class, args);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        return connector;
    }

    //////////////////////// undertow start

    /**
     * 采用Undertow作为服务器。
     * Undertow是一个用java编写的、灵活的、高性能的Web服务器，提供基于NIO的阻塞和非阻塞API，特点：
     * 非常轻量级，Undertow核心瓶子在1Mb以下。它在运行时也是轻量级的，有一个简单的嵌入式服务器使用少于4Mb的堆空间。
     * 支持HTTP升级，允许多个协议通过HTTP端口进行多路复用。
     * 提供对Web套接字的全面支持，包括JSR-356支持。
     * 提供对Servlet 3.1的支持，包括对嵌入式servlet的支持。还可以在同一部署中混合Servlet和本机Undertow非阻塞处理程序。
     * 可以嵌入在应用程序中或独立运行，只需几行代码。
     * 通过将处理程序链接在一起来配置Undertow服务器。它可以对各种功能进行配置，方便灵活。
     */
//    @Bean
//    public ServletWebServerFactory undertowFactory() {
//        UndertowServletWebServerFactory undertowFactory = new UndertowServletWebServerFactory();
//
//        undertowFactory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
//            @Override
//            public void customize(Undertow.Builder builder) {
//
//                builder.addHttpListener(httpPort, "0.0.0.0");
////				// 开启HTTP2
//                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
//            }
//        });
//
//        return undertowFactory;
//    }

//    @Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        HttpMessageConverter<?> converter = fastConverter;
//        return new HttpMessageConverters(converter);
//    }

}
