package com.example.localproject.wechatMsg;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WeChatUtils {

//	public static boolean debug = true;
	private static boolean debug = false;

	private static Logger log = LoggerFactory.getLogger("");

	static WeChatMsgSend swx = new WeChatMsgSend();

	static String corp_id = "ww0d2f266930fe0fd7";
	static int application_id;
	static String application_secret;

	static String token;

	static {
		if (debug) {//测试应用
			application_id = 1000002;
			application_secret = "qOd1PZ_Bj2PyE0NQdRhd6vC1c3H6RX5QbnZXwtsWy4M";
		} else {
			application_id = 1000002;
			application_secret = "qOd1PZ_Bj2PyE0NQdRhd6vC1c3H6RX5QbnZXwtsWy4M";
		}
		getToken();
	}

	private static synchronized void getToken() {
		try {
			token = swx.getToken(corp_id, application_secret);
		} catch (IOException e) {
			log.error("获取token错误 ", e);
		}
	}


	public static void main(String[] args) {
		send_Msg("这是一条测试的消息");
	}

	/**
	 * 发送到 消息应用
	 * @param msg
	 */
	public static synchronized void send_Msg(String msg) {
		try {
			getToken();
			String content = ""
//					+ "消息:\n"
					+ msg;
			String postdata = swx.createpostdata("@all", "text", application_id,"content", content);
			String resp = swx.post("utf-8", WeChatMsgSend.CONTENT_TYPE, (new WeChatUrlData()).getSendMessage_Url(), postdata, token);
			log.info("发送微信的响应数据: {}" , resp);
		} catch (Exception e) {
			log.error("zz_monitor_send 发送消息出错 ", e);
		}
	}


	/**
	 * 获取token
	 * @param corpsecret
	 */
	public static synchronized String getToken(String corpsecret) {
		String token = null;
		try {
			token = swx.getToken(corp_id, corpsecret);
			log.info("获取token  corpsecret：{} ", corpsecret);
		} catch (Exception e) {
			log.error("getToken 出错 ", e);
		}
		return token;
	}

	/**
	 * 发送错误消息到 监控应用
	 * @param msg
	 */
	public static synchronized void zz_monitor_send(String msg, String token, int appId) {
		try {
//			String content = "尊者应用监控消息:\n" + msg;
			String content = "" + msg;
			String postdata = swx.createpostdata("@all", "text", appId,"content", content);
			String resp = swx.post("utf-8", WeChatMsgSend.CONTENT_TYPE, (new WeChatUrlData()).getSendMessage_Url(), postdata, token);
			log.info("发送微信的响应数据: {}" , resp);
		} catch (Exception e) {
			log.error("zz_monitor_send 发送消息出错 ", e);
		}
	}


	private static synchronized void qiye_app_monitor() {
		WeChatMsgSend swx = new WeChatMsgSend();
		try {
			String token = swx.getToken("ww35d00ef1f5f3b386", "Qwf45DXqnVYlxR36NdV5GPXShU7ejM3IWuOXfYtsKWs");
			String postdata = swx.createpostdata("@all", "text", 1000002,"content",
					"这是sdfdsf一条测试信息");
			String resp = swx.post("utf-8", WeChatMsgSend.CONTENT_TYPE, (new WeChatUrlData()).getSendMessage_Url(), postdata, token);
			System.out.println("获取到的token======>" + token);
			System.out.println("请求数据======>" + postdata);
			System.out.println("发送微信的响应数据======>" + resp);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}