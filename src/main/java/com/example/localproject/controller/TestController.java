package com.example.localproject.controller;

import com.alibaba.fastjson.JSON;
import com.example.localproject.domain.AppResponse;
import com.example.localproject.domain.DemoDto;
import com.example.localproject.reqdomain.TestReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author xiazhengtao
 * @date 2019-07-25 20:40
 */
@RestController
@RequestMapping("appInfo")
public class TestController {

    Logger logger = LoggerFactory.getLogger(TestController.class);


    @Value("${name}")
    private String name;

    @RequestMapping(value = "test",method = {RequestMethod.POST})
    public AppResponse getSystemTime(HttpServletRequest request, HttpServletResponse response, @RequestBody TestReq testReq){
        AppResponse<DemoDto> appResponse = new AppResponse<>();
        logger.info("测试接口请求参数:{}", JSON.toJSONString(testReq));
        DemoDto demoDto = new DemoDto();
        demoDto.setId(testReq.getId());
        demoDto.setName(name);
        demoDto.setDate(new Date());
        appResponse.setData(demoDto);
        appResponse.setMessage("success");
        logger.info("end------,the result:{}",JSON.toJSONString(appResponse));
        return appResponse;
    }

    @RequestMapping(value = "index", method = {RequestMethod.GET})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", name);
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
