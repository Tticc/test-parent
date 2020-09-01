package com.tester.testerrpc.config;

import com.tester.testerrpc.client.RpcServiceClient;
import com.tester.testerrpc.server.RpcServiceProvider;
import com.tester.testerrpc.server.ServiceMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RpcServiceConfig {

	@Value("${tester.rpc.server.class.path:com.tester.testerrpc.service}")
	private String dubboServicePath;
	@Value("${tester.rpc.server.port:8000}")
	private int dubboServerPort;
	@Value("${tester.rpc.server.host:127.0.0.1}")
	private String dubboServerHost;
	@Value("${tester.rpc.server.provider.number:1}")
	private int providerNumber;

	@Autowired
	private ServiceMap serviceMap;

	// ***************************************** server ***********************************************
	@Bean(name="rpcServiceProvider")
	public RpcServiceProvider rpcServiceProvider() throws Exception {
		RpcServiceProvider dsp = new RpcServiceProvider();
		try {
			// 初始化服务列表
			serviceMap.initServiceMap(dubboServicePath, true);
			// 启动 dubbo 服务器
			RpcServiceProvider.doProvide(providerNumber, dubboServerPort);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw e;
		}
		return dsp;
	}
	// ***************************************** server ***********************************************
	
	
	// ***************************************** client ***********************************************
	@Bean(name="dubboServiceClient")
	public RpcServiceClient dubboServiceClient() {
		System.out.println("\r\n\r\n");
		System.out.println("nettyRpc will connect to:"+dubboServerHost+":"+dubboServerPort);
		System.out.println("\r\n\r\n");
		RpcServiceClient.setHost(dubboServerHost);
		RpcServiceClient.setPort(dubboServerPort);
		return RpcServiceClient.getInstance();
	}
	
	/**
	 * MyFutureTask 应该是 DubboServiceClient 的一部分，不应该在外部初始化。所以注释掉。
	 * @author Wen, Changying
	 * @return
	 * @date 2019年8月31日
	 */
//	@Bean(name="myFutureTask")
//	public MyFutureTask<Object> myFutureTask() {
//		return new MyFutureTask<Object>();
//	}
	// ***************************************** client ***********************************************
}
