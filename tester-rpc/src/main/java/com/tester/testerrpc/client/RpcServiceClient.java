package com.tester.testerrpc.client;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.controller.RestResult;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Map;
import java.util.concurrent.*;

/**
 * singleton。在需要的地方调用 {@code DubboServiceClient.getInstance();} 获得单例对象<br/>
 * 这可以是一个独立于容器之外的工具。只用来调用远程服务。<br/>
 * 为了方便配置dubboService的address和port，所以交给spring容器来管理。
 * @author Wen, Changying
 * 2019年8月31日
 */
public class RpcServiceClient {
	public static void main(String[] args) throws InterruptedException {
		// 初始化并启动client。
		RpcServiceClient dsa = RpcServiceClient.getInstance();
		Thread.sleep(5000);

		// 调用 dubbo 服务

		JSONObject params = new JSONObject();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 单例测试 成功
				RpcServiceClient ds = RpcServiceClient.getInstance();
				System.out.println(ds.toString());
				System.out.println("Tticc ****************************************************");
				params.put("name", "Tticc");
				System.out.println("output in thread:"+ds.getDubboService("HelloManager_getVO", params));
				System.out.println("done!");
			}
		}, "Tticc").start();

/*		new Thread(new Runnable() {
			@Override
			public void run() {
				// 单例测试 成功
				JSONObject par = new JSONObject();
				DubboService ds = DubboService.getInstance();
				System.out.println(ds.toString());
				System.out.println("Tticc ****************************************************");
				System.out.println("output in thread:"+ds.getDubboService("sayBye", par));
			}
		}, "Tticc").start();*/

/*		new Thread(new Runnable() {
			@Override
			public void run() {
				DubboService ds = DubboService.getInstance();
				System.out.println(ds.toString());
				System.out.println("Tticc1 ****************************************************");
				params.put("name", "Tticc1");
				System.out.println("output in thread:"+ds.getDubboService("sayHello", params));
			}
		},"Tticc1").start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				DubboService ds = DubboService.getInstance();
				System.out.println(ds.toString());
				System.out.println("Tticc2 ****************************************************");
				params.put("name", "Tticc2");
				System.out.println("output in thread:"+ds.getDubboService("sayHello", params));
			}
		}, "Tticc2").start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				DubboService ds = DubboService.getInstance();
				System.out.println(ds.toString());
				System.out.println("Tticc3 ****************************************************");
				params.put("name", "Tticc3");
				System.out.println("output in thread:"+ds.getDubboService("sayHello", params));
			}
		}, "Tticc3").start();*/

//		System.out.println("output in main:" + params);
//		Thread.sleep(4000);
//		params.put("name", "tticc, please go into");
//		params = (JSONObject) ds.getDubboService("sayHello", params);
//		System.out.println("output in main 2:" + params);


	}

	// 初始化 mft。
	private MyFutureTask<Object> mft = new MyFutureTask<Object>();
	//private final String HOST = "127.0.0.1";
	private static String host = "192.168.99.1";
	private static int port = 8000;
	public RpcServiceClient setMft(MyFutureTask<Object> mft) {
		this.mft = mft;
		return this;
	}
	public static void setHost(String host) {
		RpcServiceClient.host = host;
	}
	public static void setPort(int port) {
		RpcServiceClient.port = port;
	}

	private ExecutorService threadPool = Executors.newFixedThreadPool(1);

	LinkedBlockingQueue<String> lbq = new LinkedBlockingQueue<String>(1000*1000);

	// 用来保存正在等待返回数据的线程
	//private static Map<Long, Thread> waitThreads = new ConcurrentHashMap<Long, Thread>();

	/**
	 * 初始化实例时，使用线程池初始化client。<br/>
	 * <p>有一个问题是，如果client的线程因为某些异常挂了，那么该如何恢复呢？也就是如何监控当前线程是否在执行任务？
	 * 如果不能监控，那么只能将许多个初始化任务添加到线程池等待队列中。
	 * 这样，一旦久的线程死去，线程池会创建新线程，重新拿到任务进行client初始化。
	 * </p>
	 */
	private RpcServiceClient() {
		threadPool.execute(() -> initClientO());
	}
	public static final RpcServiceClient getInstance() {
		return SingletonCreater.INSTANCE;
	}
	/**
	 * 使用内部静态类实现 线程安全的 单例模式。<br/>
	 * 这是使用JVM本身机制保证了线程安全。
	 * jvm在加载类时只会加载一次，不管有多少个线程同时进入这里，内部静态类都只会加载一次，也就只有一个instance
	 * <ol>
	 * <li>调用外部类的静态变量和静态方法可以让外部类被加载到内存中，不过被调用的外部类的内部类（不论是否静态）不会被加载。</li>
	 * <li>加载静态内部类之前会先加载外部类，静态内部类或非静态内部类在使用它们的成员时才加载。</li>
	 * <li>内部类可以随意使用外部类的成员对象和方法（即使私有），而不用生成外部类对象</li>
	 * </ol>
	 * @author Wen, Changying
	 * 2019年8月17日
	 */
	private static class SingletonCreater{
		private static final RpcServiceClient INSTANCE = new RpcServiceClient();
	}

	public Object getDubboService(String serviceKey,JSONObject params){
		String errMsg;
		try {
			long threadID = Thread.currentThread().getId();
			mft.putMySelf();
			JSONObject requestJson = new JSONObject();
			requestJson.put("threadID", String.valueOf(threadID));
			requestJson.put("serviceKey", serviceKey);
			requestJson.put("params", params);
			lbq.add(requestJson.toString());

			// 这里为什么无法写入呢？因为channel已经在finally里被close了
			//lastWriteFuture = this.ch.writeAndFlush(requestJson.toString());
			/*if (lastWriteFuture != null) {
				lastWriteFuture.sync();
			}*/

			//阻塞 60s 等待返回
			return mft.myGet(50,TimeUnit.SECONDS);
		}catch(TimeoutException te) {
			te.printStackTrace();
			errMsg = "调用超时！";
		}catch (Exception e) {
			e.printStackTrace();
			errMsg = e.getMessage();
		}
		// 非正常返回
		return RestResult.fail(errMsg);
	}

	/**
	 * 由于以String的方式传输数据会出问题，所以改成Object传输。使用jdk的对象序列。<br/>
	 * @author Wen, Changying
	 * @date 2019年8月24日
	 */
	private void initClientO() {
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(worker);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                            ClassResolvers.cacheDisabled(null)));
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) {
							RestResult result = null;
							Map<String,Object> resultMap = null;
							RestResult requestVO = null;
							JSONObject requestJSON = null;
							Long threadID = null;
							RestResult returnVO = null;
							try {
								result = (RestResult)msg; // 如果对象转换失败，抛异常ClassCastException，然后丢弃。

								// 如果result为空，或result的data为null，或requestData的VO为null，或requestData的请求JSON为null,
								// 或请求JSON中的threadID为null，丢弃
								if(result == null ||
										(resultMap = (Map<String,Object>)result.getData()) == null ||
										(requestVO = (RestResult)resultMap.get("requestData")) == null ||
										(requestJSON = JSONObject.parseObject(String.valueOf(requestVO.getData()))) == null ||
										(threadID = requestJSON.getLong("threadID"))==null)
									return;

								// 成功的返回
								//mft.set(threadID, result);
								//mft.set(threadID, resultMap.get("responseData"));
								if(result.getCode() == 200) {
									returnVO = RestResult.success(resultMap.get("responseData"));
								}else {
									returnVO = RestResult.fail(result.getMessage(),resultMap.get("responseData"));
								}
								mft.set(threadID, returnVO);
							}catch(ClassCastException cce) {
								cce.printStackTrace();
							}catch(com.alibaba.fastjson.JSONException je) {
								je.printStackTrace();
							}catch(Exception e) {
								e.printStackTrace();
							}
						}
						@Override
						public void channelActive(ChannelHandlerContext ctx) {
							//when client connect to server, say hello.
							System.out.println(RestResult.success("channel active!"));
						}
					});
				}
			});
			Channel ch = b.connect(host, port).sync().channel();

			ChannelFuture lastWriteFuture = null;
			
			// 使用LinkedBlockingQueue 来实现channel复用
			// 整个client仍然是单线程的？所以才会因为LinkedBlockingQueue阻塞住？但是为什么调试的时候没事？
			// 为什么有这样的入参：
			// input: {"threadID":"19","serviceKey":"sayHello","params":{"name":"Tticc3"}}{"threadID":"17","serviceKey":"sayHello","params":{"name":"Tticc3"}}
			////// 这全都是因为 netty 读取数据的原因。服务端会从块中读取数据。由于client端在一瞬间就writeAndFlush了4条数据
			////// server端一次性读了这四条数据，导致数据解析失败。那么有两种解决方案
			////// 1.server读取定长的数据
			////// 2.server读取Object
			////// 当然，实际上还有第3种，设置client端发送数据间隔。测试可用。
			// 又发现了一个问题：为什么被关闭了？ java.io.IOException: 远程主机强迫关闭了一个现有的连接。两台电脑不会出现这个问题。
			// 为什么参数全部都是 Tticc3?
			for(;;) {
				String requestStr = "";
				requestStr = lbq.take();
				// 每隔 5s 与server通信一次
				//requestStr = lbq.poll(5, TimeUnit.SECONDS);
				
				System.out.println("requestStr is:"+requestStr);
				if(null == requestStr) {
					requestStr = "geek";
				}
				lastWriteFuture = ch.writeAndFlush(RestResult.success(requestStr));
				if (lastWriteFuture != null) {
					lastWriteFuture.sync();
				}
				//Thread.sleep(2500);
				if ("SHUTDOWNNOW".equals(requestStr)) {
					 ch.closeFuture().sync();
					 break;
				}
			}
		}catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
		}finally {
			worker.shutdownGracefully();
		}
	}

	/**
	 * 以String的方式传输数据。<br/>
	 * 但这会出现一个问题。Server 端在读数据的时候，可能会一次读取了多个请求的数据，造成错误。这时因为Server是以块的方式读取数据。
	 * 如果多个请求同时发生，那么Server就可能将多个请求的数据当成一个请求的读取了。
	 * 所以改用对象传输。
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void initClient() {
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(worker);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new StringEncoder());
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) {
							System.out.println("result: "+msg.toString());
							JSONObject result = (JSONObject) JSONObject.parse(msg.toString());
							long threadID = result.getLongValue("threadID");
							System.out.println("**************//////////output"+result);
							mft.set(threadID, msg);
						}
						@Override
						public void channelActive(ChannelHandlerContext ctx) {
						}
					});
				}
			});
			Channel ch = b.connect(host, port).sync().channel();

			ChannelFuture lastWriteFuture = null;
			for(;;) {
				String requestStr = "";
				//requestStr = lbq.take();
				requestStr = lbq.poll(5, TimeUnit.SECONDS);
				if(null == requestStr) {
					requestStr = "geek";
				}
				lastWriteFuture = ch.writeAndFlush(requestStr);
				System.out.println("|||||||||||||||||||||||||||||||||||output is:"+requestStr);
				if (lastWriteFuture != null) {
					lastWriteFuture.sync();
				}
				Thread.sleep(2500);
				if ("SHUTDOWNNOW".equals(requestStr)) {
					 ch.closeFuture().sync();
					 break;
				}
			}
		}catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
		}finally {
			worker.shutdownGracefully();
		}
	}
}
