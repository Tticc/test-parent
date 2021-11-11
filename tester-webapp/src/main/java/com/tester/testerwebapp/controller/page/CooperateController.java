package com.tester.testerwebapp.controller.page;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.constant.ConstantList;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/coor")
public class CooperateController {
	private static final String COOPCONTENT = "coopContent";
	private StringBuffer sb = new StringBuffer("init");

	@GetMapping("/init")
	public String getInit(HttpServletRequest req) {
		String initContent = COOPCONTENT;
		this.sb.replace(0, this.sb.length(), initContent);
		req.setAttribute("content", initContent);
		req.setAttribute("someAttr","here is the someAttr");
		return "coor/init";
	}

	@GetMapping("/ope")
	public String getOpe(HttpServletRequest req) {
		return "coor/ope";
	}


	/**
	 * 文件上传。然后前端建立socket。socket启动类：
	 * com.tester.testerrpc.server.socket.MyWebSocketServerProvider。
	 * 前端根据traceid和ip:port建立socket，接收socket信息，实现实时刷新
	 * @Date 18:19 2021/11/11
	 * @Author 温昌营
	 * todo
	 **/
	@ResponseBody
	@RequestMapping(value = "/parseApolloItems", method = RequestMethod.POST)
	public String parseApolloItems(@RequestParam("sheetName") String sheetName, @RequestParam("files") MultipartFile[] files) throws Exception {
		setTraceId();
		if (StringUtils.isEmpty(sheetName)) {
			return "error. empty worksheetName";
		}
		String tractId = MDC.get(ConstantList.MDC_TRACE_ID_KEY);
		log.info("开始处理发布任务。tractId:{}", tractId);
		log.info("工作表名称:{}", sheetName);


		// 开始任务
		log.info("异步处理任务开始");
//		startTask(files[0], sheetName, tractId);

		// 构建返回内容
		InetAddress addr = InetAddress.getLocalHost();
		String hostAddress = addr.getHostAddress();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("traceId", tractId);
		jsonObject.put("ip", hostAddress+":8000");
		return jsonObject.toString();
	}


	private void setTraceId(){
		// 生成并设置traceId
		String tractId = UUID.randomUUID().toString().replaceAll("-", "");
		MDC.put(ConstantList.MDC_TRACE_ID_KEY, tractId);
	}

}
