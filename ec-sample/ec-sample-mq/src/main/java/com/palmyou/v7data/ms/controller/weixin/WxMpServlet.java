package com.palmyou.v7data.ms.controller.weixin;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("wxMpServlet")
public class WxMpServlet extends WxBaseController{

	@PostConstruct
	public void init() {

		super.init();

		WxMpMessageHandler hhHandler = new WxMpMessageHandler() {

			@Override
			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
					Map<String, Object> context, WxMpService wxMpService,
					WxSessionManager sessionManager) throws WxErrorException {
				WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT()
						.content("测试加密消息").fromUser(wxMessage.getToUserName())
						.toUser(wxMessage.getFromUserName()).build();
				return m;
			}

		};

		WxMpMessageHandler handler = new WxMpMessageHandler() {

			@Override
			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
					Map<String, Object> context, WxMpService wxMpService,
					WxSessionManager sessionManager) throws WxErrorException {
				WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT()
						.content("默认回复")
						.fromUser(wxMessage.getToUserName())
						.toUser(wxMessage.getFromUserName()).build();
				return m;
			}

		};

		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
		wxMpMessageRouter
			.rule().async(false).content("哈哈")
				// 拦截内容为“哈哈”的消息
				.handler(hhHandler).next()
				
				.rule().async(false).rContent("([/s/S]*)")
				.handler(handler).end();

	}

	String nonce = null;
	String timestamp = null;

	/**
	 * 微信接入的入口URL(get方式)，此URL是开发者用来接收微信消息和事件的接口URL GET请求携带四个参数： signature
	 * 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。 timestamp 时间戳
	 * nonce 随机数 echostr 随机字符串 确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效
	 */
	@RequestMapping(method=RequestMethod.GET)
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		nonce = request.getParameter("nonce");
		timestamp = request.getParameter("timestamp");
		String signature = request.getParameter("signature");

		if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
			// 消息签名不正确，说明不是公众平台发过来的消息
			response.getWriter().println("非法请求");
			return;
		}

		String echostr = request.getParameter("echostr");
		if (StringUtils.isNotBlank(echostr)) {
			// 说明是一个仅仅用来验证的请求，回显echostr
			response.getWriter().println(echostr);
			return;
		}

	}

	/**
	 * 用户每次向公众号发送消息、或者产生自定义菜单点击事件时，开发者填写的服务器配置URL将得到微信服务器推送过来的消息和事件，
	 * 然后开发者可以依据自身业务逻辑进行响应
	 * 公众帐号只有在被绑定到微信开放平台帐号下后，才会获取UnionID），可通过获取用户基本信息中的UnionID来区分用户的唯一性
	 */
	@RequestMapping(method=RequestMethod.POST)
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		@SuppressWarnings("unchecked")
		Map<String, String[]> map = request.getParameterMap();
		for (Entry<String, String[]> iterable_element : map.entrySet()) {
			System.out.println(iterable_element.getKey());
			String[] ss = iterable_element.getValue();
			for (int i = 0; i < ss.length; i++) {
				System.out.println(ss[i]);
			}
		}
		
		String encryptType = StringUtils.isBlank(request
				.getParameter("encrypt_type")) ? "raw" : request
				.getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request
					.getInputStream());
			
			// 取得openid
			System.out.println("FROM OPEN_ID:" + inMessage.getFromUserName());
			System.out.println("MSG_TEPE:" + inMessage.getMsgType());
			System.out.println("getEvent:" + inMessage.getEvent());
			System.out.println("getEventKey:" + inMessage.getEventKey());
			System.out.println("getMsgId:" + inMessage.getMsgId());
			System.out.println("getSentCount:" + inMessage.getSentCount());
			
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			
			response.getWriter().write(outMessage.toXml());
			return;
		}

//		if ("aes".equals(encryptType)) {
//
//			// 是aes加密的消息
//			nonce = request.getParameter("nonce");
//			timestamp = request.getParameter("timestamp");
//			String msgSignature = request.getParameter("msg_signature");
//
//			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
//					request.getInputStream(), wxMpConfigStorage, timestamp,
//					nonce, msgSignature);
//
//			System.out.println("FROM OPEN_ID:" + inMessage.getFromUserName());
//			System.out.println("MSG_TEPE:" + inMessage.getMsgType());
//			
//			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
//			response.getWriter().write(
//					outMessage.toEncryptedXml(wxMpConfigStorage));
//			return;
//		}

		response.getWriter().println("不可识别的加密类型");
		return;
	}

}