package com.tester.testerwebapp;

import com.tester.testercommon.util.file.MyFileReaderWriter;
import com.tester.testercommon.util.jwt.JwtDataModel;
import com.tester.testercommon.util.jwt.JwtHelper;
import org.junit.Test;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.stream.Collectors;

public class NormalTest_WebApp {


    public static void main(String[] args) {
        System.out.println("hello,world");
        byte[] placeholder=new byte[64*1024*1024];
        System.gc();
    }


    @Test
    public void test_str(){
        String str = "1";
        String str1 = getStr(str);
        System.out.println(str1);
        str1 = getStr(null);
        System.out.println(str1);
    }

    private String getStr(String code){
        Assert.hasLength(code,"code should not empty");
        switch (code){
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
    public void test_set(){
        HashSet<String> set = new HashSet<>();
        set.add("key");

        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("ji");
        treeSet.remove("ji");

        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("jdi");

    }


    @Test
    public void test_array(){
        Integer[] ints = {1,2,4,5};
        Integer[] arr = new Integer[5];
        Integer[] addedArr = new Integer[]{1,2};
        int endPoint = arr.length;
        // 扩容
        arr = Arrays.copyOf(arr, endPoint+addedArr.length);
        System.out.println("arr.length: "+arr.length);
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
    public void test_list(){

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
        }catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
        System.out.println(strs);
    }

    @Test
    public void test_AtomicStampedReference(){
        int start = 0;
        AtomicStampedReference<Integer> nodeNum = new AtomicStampedReference(start,1);
        System.out.println(nodeNum.getReference());
        nodeNum.compareAndSet(nodeNum.getReference(), nodeNum.getReference()+1, nodeNum.getStamp(), nodeNum.getStamp() + 1);
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
    public void test_date(){
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
    public void test_linkedHashMap(){
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
        map.put("jo","jo");
        System.out.println(map);
    }
    @Test
    public void test_num_shift(){
        long time = new Date().getTime();
        System.out.println(time);
        String str = "89374294893248392";
        System.out.println(str.length());
        String substring = str.substring(str.length() - 10);
        System.out.println(substring);
        System.out.println(substring.length());
        System.out.println((-1L<<5));
    }


    @Test
    public void test_value(){
        int i = 9883210%6;
        System.out.println(i);
    }
    @Test
    public void test_order_no(){
        Long memberId = 9883210L;
        Long aLong = generateOrderNo(10L, memberId, 1);
        System.out.println(aLong);
        String suffix = getSuffixdb("order_item", "order_no", aLong%1000);
        System.out.println(suffix);

        String suffix1 = getSuffixtb("order_item", "order_no", aLong % 1000);
        System.out.println(suffix1);

    }
    public String getSuffixdb(String logicTableName, String columnName, Long shardValue) {
        Integer dbNum = 3;
        Integer tableShardNum = 2;
        Long suffix;
        suffix = shardValue/10;
        return "_"+(suffix<10?"0"+suffix:suffix);
    }
    public String getSuffixtb(String logicTableName, String columnName, Long shardValue) {
        Integer dbNum = 3;
        Integer tableShardNum = 2;
        Long suffix;
        suffix = (shardValue / 10) * tableShardNum + shardValue % 10;
        return "_"+(suffix<10?"0"+suffix:suffix);
    }
    public Long generateOrderNo(Long serialNo, Long memberId, Integer orderType){
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
    public void test_equal(){
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
        System.out.println("有效人员："+collect.size());
        System.out.println("无效人员："+collect2.size());
        for (String e:collect2) {
            System.out.println(e);
        }
    }

    /**
     * person in person
     * @return
     */
    private String[] getPp() throws IOException {
        File file = new File("C:\\Users\\wenc\\Desktop\\ppa.txt");
        String[] text = MyFileReaderWriter.txt2String(file,";").split(";");
        System.out.println("pp.size:"+text.length);
        return text;
    }
    /**
     * person in station
     * @return
     */
    private String[] getPis() throws IOException {
        File file = new File("C:\\Users\\wenc\\Desktop\\pisa.txt");
        String[] text = MyFileReaderWriter.txt2String(file,";").split(";");
        System.out.println("pis.size:"+text.length);
        return text;
    }

}
