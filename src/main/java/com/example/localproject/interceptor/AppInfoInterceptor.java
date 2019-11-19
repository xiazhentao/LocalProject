package com.example.localproject.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AppInfoInterceptor implements HandlerInterceptor {
	Logger log = LoggerFactory.getLogger(AppInfoInterceptor.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-startTimed");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			long timed = System.currentTimeMillis();
			startTimeThreadLocal.set(timed);
			String requestURL = request.getRequestURI();
			log.info("start 当前请求  URL：【 {} 】",requestURL);
		} catch (Exception e) {
			log.error("API业务处理-拦截器异常：", e);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

		try {
			int tooLong = 0;
			String requestURL = request.getRequestURI();

			long cost = System.currentTimeMillis() - startTimeThreadLocal.get();
			if(cost > 3 * 1000) {
				log.info("======================================== API耗时过长：【 {} 】, URL：【 {} 】", cost ,requestURL);
				tooLong = 3;
			}
			response.setHeader("costTime",Long.toString(cost));
			log.info("end 处理完成 耗时：【 {} 】，请求url:【{}】", cost ,requestURL);
			if (ex != null) {
//				info("API返回请求发生异常：", ex.getMessage());
//				log.error("异常信息如下requestID:  " +requestID+ ", error ：", ex);
				log.error(String.format("requestURL: %s, error ：", requestURL), ex);
			}

		} catch (Exception e) {
			log.error("API完成返回-拦截器异常：", e);
		}

	}
}
