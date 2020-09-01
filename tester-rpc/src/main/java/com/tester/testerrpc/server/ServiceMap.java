package com.tester.testerrpc.server;

import com.tester.testercommon.util.CommonUtil;
import com.tester.testercommon.util.classscanner.ClazzScannerWithinSpring;
import com.tester.testerrpc.annotation.RpcServiceMe;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 抽出包扫描工具类
 * https://www.cnblogs.com/zeng1994/p/59313120e95e2074981870c787cd69d9.html
 *
 * @author Wen, Changying
 * 2019年8月28日
 */
@Component("serviceMap")
public class ServiceMap implements ApplicationContextAware {

    private static Map<String, Method> serviceMap = new HashMap<String, Method>();

    private static Map<String, ServiceNode> serviceNodesMap = new HashMap<String, ServiceNode>();

    public static Map<String, Method> getServiceMap() {
        return serviceMap;
    }

    public static Map<String, ServiceNode> getServiceNodesMap() {
        return serviceNodesMap;
    }
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 类加载时初始化serviceNodesMap
     */
//	static {
//		try {
//			// 初始化map，annotation方式
//			initServiceMap("com.spc.other.rpcAnetty.simulateDubbo",true);
//			// 初始化map，hardcode方式
//			Method m = com.spc.other.rpcAnetty.ServiceImpl.class.getMethod("sayHello", String.class);
//			serviceMap.put("sayHello", m);
//			Method bye = com.spc.other.rpcAnetty.ServiceImpl.class.getMethod("sayBye");
//			serviceMap.put("sayBye", bye);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    /**
     * 根据特定包下的注解初始化serviceNodesMap，提供service。
     * <p><b>只 保 存 public 方 法</b></p>
     *
     * @throws Exception
     * @author Wen, Changying
     * @date 2019年8月29日
     */
    public void initServiceMap(String packageName, boolean recursive) throws Exception {
        // 拿到特定包路径下面的所有class
        //List<Class<?>> listCla = clazzScanner.getAllClass(packageName, recursive);
        List<Class<?>> listCla = ClazzScannerWithinSpring.loadAllClass(new String[]{packageName});
        // 遍历，拿到所有method
        Iterator<Class<?>> ite = listCla.iterator();
        Class<?> currentItem;
        while (ite.hasNext()) {
            // 如果当前class是interface或原始类型，或者没有RpcServiceMe注解，跳过。
            if ((currentItem = ite.next()).isInterface() ||
                    currentItem.isPrimitive() ||
                    currentItem.getAnnotation(RpcServiceMe.class) == null) {
                System.out.println(currentItem + "--- skip, this is an Interface or Premitive or have on @RpcServiceMe.");
                continue;
            }
            System.out.println(currentItem.getSimpleName());
            try {
                generateMethod(currentItem,
                        applicationContext.getBean(CommonUtil.toLowerCaseFirstOne(currentItem.getSimpleName())));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * <p><b>只 保 存 public 方 法</b></p>
     * 构建serviceNodesMap。
     *
     * @param clz
     * @param o
     * @author Wen, Changying
     * @date 2019年8月29日
     */
    private void generateMethod(Class<?> clz, Object o) {
        Method[] ms = clz.getDeclaredMethods();
        for (Method m : ms) {
            // 如果是public方法，放入map，否则跳过。
            if (m.getModifiers() == 1) {
                serviceNodesMap.put(clz.getSimpleName() + "_" + m.getName(), new ServiceNode(m, o));// 所以，serviceKey是GreetService_sayHello。
            }
        }
    }

    /**
     * ServiceNode，用来保存Method和Method对应的实例对象。<br/>
     * 只有get，没有set
     *
     * @author Wen, Changying
     * 2019年8月29日
     */
    static class ServiceNode {
        private Method m;
        private Object o;

        ServiceNode(Method m, Object o) {
            this.m = m;
            this.o = o;
        }

        public Method getMethod() {
            return this.m;
        }

        public Object getSerivceImpl() {
            return this.o;
        }
    }

    /**
     * Spring容器启动后，会把 applicationContext 给自动注入进来，这里把 applicationContext 赋值到静态变量中，方便后续拿到容器对象
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        ServiceMap.applicationContext = applicationContext;

    }

//	public static List<Class<?>> getAllClass(List<String> classFullPath){
//		List<Class<?>> result = new LinkedList<Class<?>>();
//		Iterator<String> ite = classFullPath.iterator();
//		while(ite.hasNext()) {
//			try {
//				result.add(Class.forName(ite.next()));
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
//	/**
//	 * 查找包下的所有类的名字
//	 * @author Wen, Changying
//	 * @param packageName
//	 * @param recursive
//	 * @return
//	 * @date 2019年8月28日
//	 */
//	public static List<String> getClazzName(String packageName, boolean recursive) {
//		List<String> result = new LinkedList<String>();
//		String packageDirName = packageName.replaceAll("\\.", "/");
//		Enumeration<URL> dirs;
//		try {
//			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
//			while (dirs.hasMoreElements()){
//				URL url = dirs.nextElement();
//				String protocol = url.getProtocol();
//				if ("file".equals(protocol)) {
//					// System.out.println("syso -- file类型");
//					result.addAll(getAllClassNameByFile(new File(url.getPath()), recursive));
//				}else if("jar".equals(protocol)){
//					JarFile jarFile = null;
//					try{
//						jarFile = ((java.net.JarURLConnection) url.openConnection()).getJarFile();
//					} catch(Exception e){
//						e.printStackTrace();
//					}
//					if(jarFile != null) {
//						result.addAll(getAllClassNameByJar(jarFile, packageName, recursive));
//					}
//				}
//			}
//		}catch(Exception e) {
//			
//		}
//		return result;
//	}
//	/**
//	 * 递归获取所有class文件的名字
//	 * @author Wen, Changying
//	 * @param file
//	 * @param recursive
//	 * @return
//	 * @date 2019年8月28日
//	 */
//	public static List<String> getAllClassNameByFile(File file, boolean recursive) {
//		List<String> result =  new LinkedList<>();
//		if(!file.exists()) return result;
//		if(file.isFile()) {
//			String path = file.getPath();
//			if(path.endsWith(".class")) {
//				path = path.replace(".class", "");
//				String class_file_prefix = File.separator + "classes"  + File.separator;
//				String clazzName = path.substring(path.indexOf(class_file_prefix) + class_file_prefix.length())
//						.replace(File.separator, ".");
//				if(-1 == clazzName.indexOf("$")) result.add(clazzName);
//			}
//			return result;
//		}else {
//			File[] listFiles = file.listFiles();
//			if(listFiles != null && listFiles.length > 0) {
//				for (File f : listFiles) {
//					if(recursive) {
//						result.addAll(getAllClassNameByFile(f, recursive));
//					}else {
//						if(f.isFile()){
//							String path = f.getPath();
//							if(path.endsWith(".class")) {
//								path = path.replace(".class", "");
//								String class_file_prefix = File.separator + "classes"  + File.separator;
//								String clazzName = path.substring(path.indexOf(class_file_prefix) + class_file_prefix.length())
//										.replace(File.separator, ".");
//								if(-1 == clazzName.indexOf("$")) result.add(clazzName);
//							}
//						}
//					}
//					
//				}
//			}
//		}
//		return result;
//	}
//	
//	/**
//	 * 递归获取jar所有class文件的名字
//	 * @author Wen, Changying
//	 * @param jarFile
//	 * @param packageName
//	 * @param flag
//	 * @return
//	 * @date 2019年8月28日
//	 */
//	private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag) {
//		List<String> result =  new LinkedList<>();
//		Enumeration<JarEntry> entries = jarFile.entries();
//		while(entries.hasMoreElements()) {
//			JarEntry jarEntry = entries.nextElement();
//			String name = jarEntry.getName();
//			// 判断是不是class文件
//			if(name.endsWith(".class")) {
//				name = name.replace(".class", "").replace("/", ".");
//				if(flag) {
//					// 如果要子包的文件,那么就只要开头相同且不是内部类就ok
//					if(name.startsWith(packageName) && -1 == name.indexOf("$")) {
//						result.add(name);
//					}
//				}else {
//					// 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
//					if(packageName.equals(name.substring(0, name.lastIndexOf("."))) && -1 == name.indexOf("$")) {
//						result.add(name);
//					}
//				}
//			}
//		}
//		return result;
//	}
}
