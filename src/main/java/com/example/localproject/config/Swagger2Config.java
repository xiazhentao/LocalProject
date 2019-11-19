package com.example.localproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Profile({"local", "uat","release"})
@Configuration
@EnableSwagger2
public class Swagger2Config {
	//swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
	@Bean
	public Docket createRestApi() {


		//添加head参数start

//		.name("pdaDeviceNo").description("设备号").modelRef(new ModelRef("string")).parameterType("header").required(false)
//				.name("pdaVersionNo").description("版本号").modelRef(new ModelRef("string")).parameterType("header").required(false)

//		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<>();
		pars.add(new ParameterBuilder().name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build());
		pars.add(new ParameterBuilder().name("pdaDeviceNo").description("设备号").modelRef(new ModelRef("string")).parameterType("header").required(false).build());
		pars.add(new ParameterBuilder().name("pdaVersionNo").description("版本号").modelRef(new ModelRef("string")).parameterType("header").required(false).build());
		//添加head参数end

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				//为当前包路径
				.apis(RequestHandlerSelectors.basePackage("com.example.localproject.controller"))
				.paths(PathSelectors.any())
				.build().globalOperationParameters(pars);
	}
	//构建 api文档的详细信息函数,注意这里的注解引用的是哪个
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				//页面标题
				.title("localProject RESTful API")
				//创建人
				.contact(new Contact("xiazhengtao", "http://www.xxxx.com", "xiazhengtao@yto.net.cn"))
				//版本号
				.version("1.0")
				//描述
				.description("localProject API")
				.build();
	}


}