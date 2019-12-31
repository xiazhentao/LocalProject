package com.example.localproject.job;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.example.localproject.domain.WeatherDto;
import com.example.localproject.wechatMsg.WeChatUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiazhengtao
 * @date 2019-12-30 14:00
 */
@Component
public class WeatherJob {

    Logger logger = LoggerFactory.getLogger(WeatherJob.class);

    private static final String weatherUrl = "http://wthrcdn.etouch.cn/weather_mini?city=";

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    public static final String taiCangName = "太仓";

    public static final String shanghaiCityName = "上海";

    @PostConstruct
    @Scheduled(cron = "0 30 6 * * ? *")
    public void init() {

        String msg = getWeather(taiCangName);

        msg += getWeather(shanghaiCityName);


        logger.info(msg);

    }

    public String getWeather(String cityName) {
        String status = "";
        String url = weatherUrl;
        try {
            String body = restTemplate.exchange(url + cityName, HttpMethod.GET, null, String.class).getBody();
            JSONObject jsonObject = JSON.parseObject(body);
            if ("1000".equals(jsonObject.getString("status"))) {
                StringBuilder stringBuilder = new StringBuilder();
                String data = jsonObject.getString("data");
                JSONObject jsonObject1 = JSON.parseObject(data);
                String forecast = jsonObject1.getString("forecast");
                List<WeatherDto> weatherDtos = JSONArray.parseArray(forecast, WeatherDto.class);
                if (weatherDtos != null && weatherDtos.size() > 0) {
                    WeatherDto weatherDto = weatherDtos.get(0);
                    String fengli = weatherDto.getFengli();
                    stringBuilder.append("城市:").append(cityName).append("\n").append("当前温度:").append(jsonObject1.getString("wendu")).append("\n").
                            append("今日").append(weatherDto.getDate()).append("\n").
                            append("天气:").append(weatherDto.getType()).append("\n").
                            append("温度:").append(weatherDto.getLow()).append("  -  ").append(weatherDto.getHigh()).append("\n").
                            append("风向:").append(weatherDto.getFengxiang()).append(",  风力:").append(fengli.substring(fengli.indexOf("[") + 7, fengli.indexOf("]"))).append("\n").
                            append("感冒指数:").append(jsonObject1.getString("ganmao")).append("\n\n");

                    WeatherDto weatherDto1 = weatherDtos.get(1);
                    String fengli1 = weatherDto1.getFengli();
                    stringBuilder.append("明日").append(weatherDto1.getDate()).append("\n").
                            append("天气:").append(weatherDto1.getType()).append("\n").
                            append("温度:").append(weatherDto1.getLow()).append("  -  ").append(weatherDto1.getHigh()).append("\n").
                            append("风向:").append(weatherDto.getFengxiang()).append(",  风力:").append(fengli1.substring(fengli1.indexOf("[") + 7, fengli1.indexOf("]"))).append("\n");

                }
                logger.info(stringBuilder.toString());
                WeChatUtils.send_Msg(stringBuilder.toString());
                status = "OK \n";
            }
        } catch (RestClientException e) {
            status = "error \n";
            e.printStackTrace();
        }
        return status;

    }

    public static void main(String[] args) {
        String url = "http://wthrcdn.etouch.cn/weather_mini?city=太仓";

        HttpClientBuilder builder = HttpClientBuilder.create();
        // http请求连接池
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(3000);
        // 每个路由最大并发量
        connectionManager.setDefaultMaxPerRoute(3000);

        // 失败重试处理
        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(2, true);

        // 请求头设置
        List<BasicHeader> defaultHeaders = new ArrayList<BasicHeader>();
        defaultHeaders.add(new BasicHeader("Content-Type", "text/html;charset=UTF-8"));
        defaultHeaders.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        defaultHeaders.add(new BasicHeader("Accept-Language", "zh-CN"));

        builder.setConnectionManager(connectionManager);
        builder.setRetryHandler(retryHandler);
        builder.setDefaultHeaders(defaultHeaders);

        // httpClient对象
        HttpClient httpClient = builder.build();
        // 请求连接工厂类
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(200);
        requestFactory.setReadTimeout(1000);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        stringHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        messageConverters.add(stringHttpMessageConverter);
        messageConverters.add(new FastJsonHttpMessageConverter());
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());
//		messageConverters.add(new MappingJackson2XmlHttpMessageConverter());

        restTemplate.setMessageConverters(messageConverters);


        String body = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();


        JSONObject jsonObject = JSON.parseObject(body);
        if ("1000".equals(jsonObject.getString("status"))) {
            String data = jsonObject.getString("data");
            JSONObject jsonObject1 = JSON.parseObject(data);
            String forecast = jsonObject1.getString("forecast");
            List<WeatherDto> weatherDtos = JSONArray.parseArray(forecast, WeatherDto.class);
            StringBuilder stringBuilder = new StringBuilder();
            if (weatherDtos != null && weatherDtos.size() > 0) {
                WeatherDto weatherDto = weatherDtos.get(0);
                String fengli = weatherDto.getFengli();
                stringBuilder.append("城市:").append("上海\n").append("当前温度:").append(jsonObject1.getString("wendu")).append("\n").
                        append("今日").append(weatherDto.getDate()).append("\n").
                        append("天气:").append(weatherDto.getType()).append("\n").
                        append("温度:").append(weatherDto.getLow()).append("  -  ").append(weatherDto.getHigh()).append("\n").
                        append("风向:").append(weatherDto.getFengxiang()).append(",  风力:").append(fengli.substring(fengli.indexOf("[") + 7, fengli.indexOf("]"))).append("\n").
                        append("感冒指数:").append(jsonObject1.getString("ganmao")).append("\n\n");
                WeatherDto weatherDto1 = weatherDtos.get(1);
                String fengli1 = weatherDto1.getFengli();

                stringBuilder.append("明日").append(weatherDto1.getDate()).append("\n").
                        append("天气:").append(weatherDto1.getType()).append("\n").
                        append("温度:").append(weatherDto1.getLow()).append("  -  ").append(weatherDto1.getHigh()).append("\n").
                        append("风向:").append(weatherDto.getFengxiang()).append(",  风力:").append(fengli1.substring(fengli1.indexOf("[") + 7, fengli1.indexOf("]"))).append("\n");

            }
//            WeChatUtils.send_Msg(stringBuilder.toString());
            System.out.println(stringBuilder.toString());
        }

    }
}
