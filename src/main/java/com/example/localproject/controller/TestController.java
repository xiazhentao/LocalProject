package com.example.localproject.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.example.localproject.domain.AppResponse;
import com.example.localproject.domain.DemoDto;
import com.example.localproject.domain.Student;
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
import java.util.*;
import java.util.stream.Collectors;

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

    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1L, "zhangsan"));
        list.add(new Student(2L, "lisi"));

        Map<Integer, String> result = list.stream().filter(student -> student.getStuId()>1).collect(Collectors.toMap(student -> (int)student.getStuId(),Student::getStuName));

        result.forEach((key,value)-> System.out.println(key+"======="+value));


        String s = "null";
        Student student = null;
        String test = Optional.ofNullable(s).orElse("test");
        System.out.println(test);

        Student wangwu = Optional.ofNullable(student).filter(student1 -> student1.getStuId() > 0).orElse(new Student(3L, "wangwu"));
        System.out.println(wangwu);

        Integer rowNum = 2001;
        Integer integer = Optional.ofNullable(rowNum).filter(row -> row > 2000).orElse(2000);
        System.out.println(integer);



        Map<String, Object> result1 = new LinkedHashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("content", "{'orgCode':'210045'}"); //json字符串,写入前置库数据
        params.put("bizType", "taking"); //业务类型 taking handon signature...
        params.put("appId",   "zunzhe");  //在前置库申请的应用名称
        params.put("charset", "UTF-8"); //验签参数编码字符集
        params.put("signType", "RSA"); //目前只支持RSA的验签方式
        params.put("timestamp", DateUtil.now()); //当前时间戳格式 yyyy-MM-dd HH:mm:ss.SSS
        params.put("version", "1.0"); //所有服务器端用户版本号1.0

        params.entrySet().stream()
                .filter(e -> e.getValue() != null && !"".equals(e.getValue()) && !"sign".equals(e.getKey()))
                .sorted(Map.Entry.<String, Object>comparingByKey())
                .forEachOrdered(e -> result1.put(e.getKey(), e.getValue()));

        StringBuilder signStr = new StringBuilder("");
        result1.forEach((key, value) -> signStr.append(key).append("=").append(value).append("&"));
        signStr.deleteCharAt(signStr.length()-1);

    }

}

