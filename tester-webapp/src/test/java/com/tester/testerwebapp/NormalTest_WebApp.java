package com.tester.testerwebapp;

import com.alibaba.fastjson.JSON;
import com.tester.base.dto.model.request.IdAndNameRequest;
import com.tester.base.dto.model.request.TextRequest;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.PasswordUtil;
import com.tester.testercommon.util.file.MyFileReaderWriter;
import com.tester.testercommon.util.file.TxtWrite;
import com.tester.testercommon.util.jwt.JwtDataModel;
import com.tester.testercommon.util.jwt.JwtHelper;
import io.undertow.server.session.SecureRandomSessionIdGenerator;
import lombok.Data;
import lombok.SneakyThrows;
import net.openhft.affinity.AffinityLock;
import org.junit.Test;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NormalTest_WebApp {

    Pattern isPic = Pattern.compile(".*?\\.(jpg|gif|jpeg)+$");

    private final ScheduledExecutorService singleScheduledExecutor = Executors.newSingleThreadScheduledExecutor();


    private static Pattern p_tag = Pattern.compile("\\+ export docker_image=(.*?)\r\n");


    public static void main(String[] args) {

//        String consoleOutputText = "+ export docker_image=jireowjiof：jfioew\r\n";
//        // tag
//        Matcher matcherTag = p_tag.matcher(consoleOutputText);
//        String tag = null;
//        if (matcherTag.find()) {
//            tag = matcherTag.group(1);
//        }
//        System.out.println("tag = " + tag);

        Date date = new Date(1652081100000L);
        System.out.println(date);
        test_cup100();

    }


    @Test
    public void test_shuffle(){
        List<String> list = Arrays.asList("1","2","41","11","4","6","21");
        processPicByType(list);
        list.stream().forEach(System.out::println);
    }

    public static void processPicByType(List<String> list){
        if(CollectionUtils.isEmpty(list) || list.size() < 2){
            return;
        }
                Collections.shuffle(list);
    }

    @Test
    public void test_printAnd() throws IOException {
        String storeAuthStr = TxtWrite.file2String(new File("D:\\desktop\\store_auth.txt"));
        String storeAllStr = TxtWrite.file2String(new File("D:\\desktop\\store_all.txt"));
        String[] split = storeAllStr.split("\r\n");
        Set<String> collect = Stream.of(split).collect(Collectors.toSet());

        String[] auths = storeAuthStr.split("\r\n");
        Stream.of(auths).filter(e -> collect.contains(e)).forEach(System.out::println);

    }
    @Test
    public void test_printStoreCode() throws IOException {
        String s = TxtWrite.file2String(new File("D:\\desktop\\test.txt"));
        String[] split = s.split("\r\n");
        for (String s1 : split) {
            if(s1.indexOf("code") >= 0  && s1.indexOf("\t\t\t\t\t\t\t") >= 0 ){
                String trim = s1.split(":")[1].replaceAll("\"", "").replace(",", "").trim();
                if(trim.length() < 10){
                    continue;
                }
                System.out.println(trim);
            }
        }
    }

    @Test
    public void test_printStoreCode1() throws IOException {
        String s = TxtWrite.file2String(new File("D:\\desktop\\test1.txt"));
        String[] split = s.split("\r\n");
        for (String s1 : split) {
            if(s1.indexOf("storeCode") >= 0){
                String trim = s1.split(":")[1].replaceAll("\"", "").replace(",", "").trim();
                if(trim.length() < 10){
                    continue;
                }
                System.out.println(trim);
            }
        }
    }
    @Test
    public void test_printApollo() throws IOException {
        String s = TxtWrite.file2String(new File("D:\\desktop\\txt.txt"));
        String[] split = s.split("\r\n");
        Set<String> appId = new HashSet<>();
        Set<String> cluster = new HashSet<>();
        Set<String> property = new HashSet<>();

        String appIdStart = "";
        String clusterStart = "";
        String propertyStart = "";
        for (String s1 : split) {
            String[] split1 = s1.split(":::");
            appIdStart = (split1[0]);
            clusterStart = (split1[1]);
            propertyStart = (split1[2]);
        }
    }


    @Test
    public void test_es_data(){
        String str = "<br/>github：https://github.com/trending/java?since=monthly；<br/><br/>\r\nstackoverflow（问题为主）：https://stackoverflow.com/questions/tagged/java<br/><br/>\r\nIBM开发者：https://developer.ibm.com/languages/java/articles/<br/><br/>\r\ninfoworld：https://www.infoworld.com/category/java/<br/><br/>\r\n博客园：https://www.cnblogs.com/cate/java/<br/><br/>\r\n掘金：https://juejin.cn/backend<br/><br/>\r\nCSDN：https://blog.csdn.net/nav/java<br/><br/>\r\ninfoQ：https://www.infoq.cn/topic/programing-languages\r\n\r\n";
        System.out.println(str);
    }

    public static void test_cup100(){
        ScheduledExecutorService e = Executors.newScheduledThreadPool(0);
        e.schedule(() -> System.out.println(1),40,TimeUnit.SECONDS);
        e.shutdown();
    }

    @Test
    public void test_attach_specific_cpu(){
        ArrayBlockingQueue<Runnable> workQueue =
                new ArrayBlockingQueue<>(100);
        //绑定到 6 号 CPU 上执行
        try (AffinityLock affinityLock = AffinityLock.acquireLock(6)) {
            for (; ; ) {
                try {
                    Runnable r = workQueue.poll(0, TimeUnit.NANOSECONDS);
                    if (r != null)
                        break;
                } catch (InterruptedException retry) {
                }
            }
        }
    }


    @Test
    public void test_ascii(){
        String s = "123,34,95,99,108,97,115,115,34,58,34,104,117,100,115,111,110,46,109,111,100,101,108,46,72,117,100,115,111,110,34,44,34,97,115,115,105,103,110,101,100,76,97,98,101,108,115,34,58,91,123,34,110,97,109,101,34,58,34,109,97,115,116,101,114,34,125,93,44,34,109,111,100,101,34,58,34,78,79,82,77,65,76,34,44,34,110,111,100,101,68,101,115,99,114,105,112,116,105,111,110,34,58,34,116,104,101,32,109,97,115,116,101,114,32,74,101,110,107,105,110,115,32,110,111,100,101,34,44,34,110,111,100,101,78,97,109,101,34,58,34,34,44,34,110,117,109,69,120,101,99,117,116,111,114,115,34,58,49,54,44,34,100,101,115,99,114,105,112,116,105,111,110,34,58,110,117,108,108,44,34,106,111,98,115,34,58,91,93,44,34,111,118,101,114,97,108,108,76,111,97,100,34,58,123,125,44,34,112,114,105,109,97,114,121,86,105,101,119,34,58,123,34,95,99,108,97,115,115,34,58,34,104,117,100,115,111,110,46,109,111,100,101,108,46,65,108,108,86,105,101,119,34,44,34,110,97,109,101,34,58,34,97,108,108,34,44,34,117,114,108,34,58,34,104,116,116,112,58,47,47,106,101,110,107,105,110,115,46,97,101,111,110,98,117,121,46,99,111,109,47,34,125,44,34,113,117,105,101,116,68,111,119,110,82,101,97,115,111,110,34,58,110,117,108,108,44,34,113,117,105,101,116,105,110,103,68,111,119,110,34,58,102,97,108,115,101,44,34,115,108,97,118,101,65,103,101,110,116,80,111,114,116,34,58,45,49,44,34,117,110,108,97,98,101,108,101,100,76,111,97,100,34,58,123,34,95,99,108,97,115,115,34,58,34,106,101,110,107,105,110,115,46,109,111,100,101,108,46,85,110,108,97,98,101,108,101,100,76,111,97,100,83,116,97,116,105,115,116,105,99,115,34,125,44,34,117,114,108,34,58,34,104,116,116,112,58,47,47,106,101,110,107,105,110,115,46,97,101,111,110,98,117,121,46,99,111,109,47,34,44,34,117,115,101,67,114,117,109,98,115,34,58,116,114,117,101,44,34,117,115,101,83,101,99,117,114,105,116,121,34,58,116,114,117,101,44,34,118,105,101,119,115,34,58,91,93,125";
        String[] split = s.split(",");
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            Integer integer = Integer.valueOf(str);
            System.out.print((char)integer.intValue());
        }
    }

    @Test
    public void test_regex_merge(){
        String consoleOutputText = "+ echo 开始合并master分支到hk-feature-20221205分支\n" +
                "开始合并master分支到hk-feature-20221205分支\n" +
                "+ git checkout hk-feature-20221205\n" +
                "Switched to a new branch 'hk-feature-20221205'\n" +
                "Branch hk-feature-20221205 set up to track remote branch hk-feature-20221205 from origin.\n" +
                "+ git pull origin hk-feature-20221205 -f\n" +
                "From http://dev-gitlab.aeonbuy.com/backend/be-middleend/be-center-promotion\n" +
                " * branch            hk-feature-20221205 -> FETCH_HEAD\n" +
                "Already up-to-date.\n" +
                "+ git merge master\n" +
                "Already up-to-date.\n" +
                "+ echo 合并master分支到hk-feature-20221205分支成功.\n" +
                "合并master分支到hk-feature-20221205分支成功.\n" +
                "+ echo 开始复制分支hk-feature-20221205_20221205172736";

        Pattern p_merge_master = Pattern.compile("(.*)合并(.*?)master分支到(.*?)分支成功(.*)\n");
        Matcher matcherMergeMaster = p_merge_master.matcher(consoleOutputText);
        if (matcherMergeMaster.find()) {
            System.out.println("done");
            String group = matcherMergeMaster.group(3);
            System.out.println("group = " + group);
        }
    }

    @Test
    public void test_singleThread() throws Exception {
        for (int i = 0; i < 40; i++) {
            int tempi = i;
            Runnable runnable = newRun(i);
            singleScheduledExecutor.submit(runnable);
        }
        TimeUnit.SECONDS.sleep(40);
    }

    @SneakyThrows
    public Runnable newRun(int tempi) {
        Integer k = null;
        return () -> {
            try {
                if (tempi % 4 == 0) {
//                    throw new BusinessException(500L);
                    k.toString();
                }
                System.out.println("the i is:" + tempi);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        };
    }


    @Test
    public void test_sleep() throws InterruptedException {
        Thread thread = new Thread(() -> sleep111());
        thread.start();
        Thread thread2 = new Thread(() -> sleep111());
        thread2.start();
        TimeUnit.SECONDS.sleep(5   );
        synchronized (this) {
            notify();
            notify();
        }
        TimeUnit.SECONDS.sleep(7   );
    }

    public synchronized void  sleep111()  {
        System.out.println("start. time:"+ DateUtil.dateFormat(new Date()));
        try {
            wait();
            TimeUnit.SECONDS.sleep(3   );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end. time:"+ DateUtil.dateFormat(new Date()));
    }


    @Test
    public void test_read() throws IOException {
        String string = TxtWrite.file2String(new File("C:\\Users\\Admin\\Desktop\\order_text.txt"));
        String[] split = string.split(",");
        System.out.println(split.length);
        StringBuffer sb=new StringBuffer();
        for (String s : split) {
            String[] split1 = s.split(":");
            Long memberId = Long.valueOf(split1[1]);
            Long num = memberId%64;
            String businessNo = split1[0];
            sb.append("select * from store_coupon_give_record_").append(num<10?"0"+num:num).append(" t where t.business_no = '").append(businessNo).append("' and t.member_id=").append(Long.valueOf(memberId)).append("").append("\r\n");
            sb.append("union all").append("\r\n");
        }
        System.out.println(sb);
    }



    @Test
    public void test_Spel(){
        ExpressionParser parser = new SpelExpressionParser();
//        Expression exp = parser.parseExpression("'hello world'");
//        String message = (String) exp.getValue();
//        System.out.println("message = " + message);

//        Expression exp1 = parser.parseExpression("'Hello World'.bytes");
//        byte[] bytes = (byte[]) exp1.getValue();
//
//        System.out.println("bytes = " + bytes);


        IdAndNameRequest name = new IdAndNameRequest();
        name.setName("wenc");
        EvaluationContext builder = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        builder.setVariable("name",name);
        ExpressionParser parser1 = new SpelExpressionParser();
        Object value = parser1.parseExpression("#name.name").getValue(builder);
        System.out.println("value = " + value);


    }


    @Test
    public void test_generateSessionId(){
        // undertow JSESSIONID
        SecureRandomSessionIdGenerator secureRandomSessionIdGenerator = new SecureRandomSessionIdGenerator();
        String sessionId = secureRandomSessionIdGenerator.createSessionId();
        System.out.println("sessionId = " + sessionId);
    }

//    @Test
//    public void generateSessionId(){
//        // tomcat JSESSIONID
//        StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
//        String s = standardSessionIdGenerator.generateSessionId("");
//        // 1574FEDE7AFA975BAFC97DBA9A6DB4E7
//        // 9886F5679A7B794C4F2DA90B03D2270A
//        System.out.println(s);
//    }

    @Test
    public void test_regex(){
        Matcher matcher = isPic.matcher("0001.mp4");
        boolean matches = matcher.matches();
        System.out.println("matches = " + matches);
    }

    @Test
    public void test_prop(){
        TextRequest request = new TextRequest();
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(request);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        System.out.println("xx");

        int l = 10_000;
        System.out.println(l);
    }

    @Test
    public void test_app() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:conf/**/application.yml");
    }


    @Test
    public void test_pwd_rule() {
        String pwd = "qs1wssssww$";
        boolean b = PasswordUtil.checkPwd(pwd);
        boolean b2 = PasswordUtil.checkPwd2(pwd);
        System.out.println(b);
        System.out.println(b2);
    }

    @Test
    public void test_str() {
        String str = "1";
        String str1 = getStr(str);
        System.out.println(str1);
        str1 = getStr(null);
        System.out.println(str1);
    }

    private String getStr(String code) {
        Assert.hasLength(code, "code should not empty");
        switch (code) {
            case "1":
                return "one";
            case "2":
                break;
            case "3":
                break;
            default:
                break;
        }
        return code;
    }

    @Test
    public void test_set() {
        HashSet<String> set = new HashSet<>();
        set.add("key");

        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("ji");
        treeSet.remove("ji");

        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("jdi");

    }


    @Test
    public void test_array() {
        Integer[] ints = {1, 2, 4, 5};
        Integer[] arr = new Integer[5];
        Integer[] addedArr = new Integer[]{1, 2};
        int endPoint = arr.length;
        // 扩容
        arr = Arrays.copyOf(arr, endPoint + addedArr.length);
        System.out.println("arr.length: " + arr.length);
        for (Integer integer : arr) {
            System.out.println(integer);
        }
        // 拼接
        System.arraycopy(addedArr, 0, arr, endPoint, addedArr.length);
        for (Integer integer : arr) {
            System.out.println(integer);
        }
    }

    @Test
    public void test_list() {

        List<String> strs = new LinkedList<>();
        strs.add("a");
        strs.add("b");
        strs.add("c");
        strs.add("d");
        strs.get(9);
        try {
            for (String str : strs) {
                strs.remove(2);
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
        System.out.println(strs);
    }

    @Test
    public void test_AtomicStampedReference() {
        int start = 0;
        AtomicStampedReference<Integer> nodeNum = new AtomicStampedReference(start, 1);
        System.out.println(nodeNum.getReference());
        nodeNum.compareAndSet(nodeNum.getReference(), nodeNum.getReference() + 1, nodeNum.getStamp(), nodeNum.getStamp() + 1);
        System.out.println(nodeNum.getReference());
    }

    @Test
    public void test_jwt() throws Exception {
        String s = JwtHelper.generateJwtToken(1, 9900L);
        System.out.println(s);
        JwtDataModel jwtDataId = JwtHelper.getJwtDataId(s);
        System.out.println(jwtDataId);
    }

    @Test
    public void test_date() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        int second = now.getSecond();
        System.out.println(second);
        System.out.println(now.getNano());
        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(format);
        System.out.println();
        System.out.println();
        Date date = new Date(1288834974657L);
        long time = new Date().getTime();
        System.out.println(time);
        System.out.println(date);
        long i = 1598431351362L - 1288834974657L;
        System.out.println(i);
        long res = (1 << 22) |
                (89 << 12) |
                1;
        System.out.println(res);
        // 1001001110000010000011000010100000010001011001000000000000
        // 1001000000101010110000111001110100000010001011001000000000000
    }

    @Test
    public void test_linkedHashMap() {
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
        map.put("jo", "jo");
        System.out.println(map);
    }

    @Test
    public void test_num_shift() {
        long time = new Date().getTime();
        System.out.println(time);
        String str = "89374294893248392";
        System.out.println(str.length());
        String substring = str.substring(str.length() - 10);
        System.out.println(substring);
        System.out.println(substring.length());
        System.out.println((-1L << 5));
    }


    @Test
    public void test_value() {
        int i = 9883210 % 6;
        System.out.println(i);
    }

    @Test
    public void test_order_no() {
        Long memberId = 9883210L;
        Long aLong = generateOrderNo(10L, memberId, 1);
        System.out.println(aLong);
        String suffix = getSuffixdb("order_item", "order_no", aLong % 1000);
        System.out.println(suffix);

        String suffix1 = getSuffixtb("order_item", "order_no", aLong % 1000);
        System.out.println(suffix1);

    }

    public String getSuffixdb(String logicTableName, String columnName, Long shardValue) {
        Integer dbNum = 3;
        Integer tableShardNum = 2;
        Long suffix;
        suffix = shardValue / 10;
        return "_" + (suffix < 10 ? "0" + suffix : suffix);
    }

    public String getSuffixtb(String logicTableName, String columnName, Long shardValue) {
        Integer dbNum = 3;
        Integer tableShardNum = 2;
        Long suffix;
        suffix = (shardValue / 10) * tableShardNum + shardValue % 10;
        return "_" + (suffix < 10 ? "0" + suffix : suffix);
    }

    public Long generateOrderNo(Long serialNo, Long memberId, Integer orderType) {
        Integer tableIndex = getTableIndex(memberId);
        Integer dbIndex = getDbIndex(tableIndex);
        Integer tableIndexInDb = getTableIndexInDb(tableIndex);
        String orderNo = serialNo + orderType.toString() + (dbIndex < 10 ? "0" + dbIndex : dbIndex) + tableIndexInDb;
        return Long.valueOf(orderNo);
    }

    private Integer getTableIndex(Long memberId) {
        Integer dbNum = 3;
        Integer tableShardNum = 2;
        return (int) (memberId % (dbNum * tableShardNum));
    }

    private Integer getDbIndex(Integer tableIndex) {
        Integer tableShardNum = 2;
        return tableIndex / tableShardNum;
    }

    private Integer getTableIndexInDb(Integer tableIndex) {
        Integer tableShardNum = 2;
        return tableIndex - (tableIndex / tableShardNum) * tableShardNum;
    }


    @Test
    public void test_equal() {
        int a = 125577;
        Integer b = new Integer(125577);
        System.out.println(b.equals(a));
    }

    @Test
    public void test_readTxt() throws IOException {
        Set<String> ppSet = new HashSet<>(Arrays.asList(getPp()));
        System.out.println(ppSet.size());
        Set<String> pisSet = new HashSet<>(Arrays.asList(getPis()));
        System.out.println(pisSet.size());
        System.out.println(pisSet.contains("02000355"));
        List<String> collect = pisSet.stream().filter(e -> ppSet.contains(e)).collect(Collectors.toList());
        List<String> collect2 = pisSet.stream().filter(e -> !ppSet.contains(e)).collect(Collectors.toList());
        System.out.println("有效人员：" + collect.size());
        System.out.println("无效人员：" + collect2.size());
        for (String e : collect2) {
            System.out.println(e);
        }
    }

    /**
     * person in person
     *
     * @return
     */
    private String[] getPp() throws IOException {
        File file = new File("C:\\Users\\wenc\\Desktop\\ppa.txt");
        String[] text = MyFileReaderWriter.txt2String(file, ";").split(";");
        System.out.println("pp.size:" + text.length);
        return text;
    }

    /**
     * person in station
     *
     * @return
     */
    private String[] getPis() throws IOException {
        File file = new File("C:\\Users\\wenc\\Desktop\\pisa.txt");
        String[] text = MyFileReaderWriter.txt2String(file, ";").split(";");
        System.out.println("pis.size:" + text.length);
        return text;
    }

}
