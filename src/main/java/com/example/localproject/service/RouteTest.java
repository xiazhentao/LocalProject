package com.example.localproject.service;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Maps;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RouteTest {
    public static void main(String[] args) {
        String url = "http://10.1.5.120:7092/pdaDownload/route/routeDownload";
        final CountDownLatch begin = new CountDownLatch(1);
        final  CountDownLatch countDownLatch = new CountDownLatch(20);
        RestTemplate restTemplate = restTemplate();
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(60);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20 * 3, 100, 10000, TimeUnit.MILLISECONDS, arrayBlockingQueue);
//        ExecutorService executorService = Executors.newFixedThreadPool(20*3);
        AtomicInteger atomicInteger = new AtomicInteger(1);
        while(atomicInteger.intValue()<20) {
            for (int i = 0; i < 20; i++) {
                threadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(Thread.currentThread().getName() + "========准备就绪");
                            begin.await();
                            queryRoute(url, restTemplate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                });

            }
            try {
                begin.countDown();
                System.out.println("all Thread start=========");
                countDownLatch.await();
                System.out.println("all Thread end=========");

                Thread.sleep(10000);
                System.out.println("Thread sleeping======");
                int count = atomicInteger.incrementAndGet();
                System.out.println("第"+count+"调用结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadPoolExecutor.shutdown();

    }


    public static void queryRoute(String url, RestTemplate restTemplate) {
        Map<String, Object> map = Maps.newHashMap();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token","cd9aa743d6da7b552859ff899e6360cc");
        map.put("downloadCount", 150);
        map.put("flag", 1);
        map.put("site",6);
        map.put("orgCode", 510901);

        HttpEntity<Object> requestHttpEntity = new HttpEntity<>(map, headers);
        long currentTimeMillis = System.currentTimeMillis();
        String stringResponse = restTemplate.exchange(url, HttpMethod.POST, requestHttpEntity,String.class).getBody();
        long l = System.currentTimeMillis() - currentTimeMillis;
        System.out.println("当前线程："+Thread.currentThread().getName()+"[queryRoute result]=============  耗时："+l);

    }
    public static RestTemplate restTemplate() {
/*
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		// HttpClient相关配置
		HttpClient httpClient = httpClientBuilder.build();
		// 使用自定义HttpClientRequestFactory
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new YtoHttpClientRequestFactory(httpClient);
		clientHttpRequestFactory.setConnectTimeout(5000);
		// 即为 SocketTimeout
		clientHttpRequestFactory.setReadTimeout(30000);
		clientHttpRequestFactory.setConnectionRequestTimeout(5000);
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
*/
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
        requestFactory.setConnectTimeout(30000);
        requestFactory.setReadTimeout(30000);

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

        return restTemplate;
    }

}
