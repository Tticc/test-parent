package com.tester.testerwebapp;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author 温昌营
 * @Date 2020-7-15
 */
public class SqlGenerator {
    public static void main(String[] args){
        new SqlGenerator().test_parseSQL();
//        long a=9223372036854775807L;
//        System.out.println(a);
    }



    /**
     * 将pdman的代码转成mapper.xml的代码
     * @return void
     * @author 温昌营
     * @date 2019/11/11
     */
    @Test
    public void test_parseSQL(){
        List<String> SQLList = new ArrayList<String>(Arrays.asList(getSQL().split("\n")));
        List<String> columnList = SQLList.stream()
                .map(item -> item.replace("    ","").replace("(","").replace(")",""))
                .map(str -> str.substring(0,str.indexOf(" ")))
                .filter(item -> StringUtils.hasText(item))
                .filter(item -> !(Objects.equals("CREATE", item) || Objects.equals("PRIMARY", item) || Objects.equals("id", item)))
//                .map(item -> underlineToCamel(item))
                .collect(Collectors.toList());
        toResultMapper(columnList);
        System.out.println("\r\n\r\n");
        toBaseSelectSql(columnList);
        System.out.println("\r\n\r\n");
        toInsertValue(columnList);
        System.out.println("\r\n\r\n");
        toBatchInsertValue(columnList);
        System.out.println("\r\n\r\n");
        toTestIf(columnList);
        System.out.println("\r\n\r\n");
        toUpdateSQL(columnList);
    }


    private String getSQL(){
        String sql = "CREATE TABLE card_order_item(\n" +
                "    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'id' ,\n" +
                "    member_id BIGINT    COMMENT '会员ID' ,\n" +
                "    order_no VARCHAR(32)    COMMENT 'order_no' ,\n" +
                "    corporation_code VARCHAR(32)    COMMENT '区域总部编码' ,\n" +
                "    order_item_no INT    COMMENT '订单商品行号 商品明细序号' ,\n" +
                "    product_id VARCHAR(32)    COMMENT '商品ID' ,\n" +
                "    sku_id VARCHAR(32)    COMMENT 'SKUID' ,\n" +
                "    sku_name VARCHAR(32)    COMMENT 'SKU名称150' ,\n" +
                "    sku_image VARCHAR(128)    COMMENT 'SKU图片256' ,\n" +
                "    sku_property VARCHAR(128)    COMMENT 'SKU属性256' ,\n" +
                "    item_code VARCHAR(32)    COMMENT '店内码 货号16' ,\n" +
                "    barcode VARCHAR(32)    COMMENT '条形码' ,\n" +
                "    market_price DECIMAL(32,8)    COMMENT '市场价' ,\n" +
                "    sale_qty DECIMAL(32,8)    COMMENT '数量' ,\n" +
                "    sale_price DECIMAL(32,8)    COMMENT '销售单价' ,\n" +
                "    promo_price DECIMAL(32,8)    COMMENT '单品促销价 -不需要' ,\n" +
                "    promo_discount DECIMAL(32,8)    COMMENT '促销活动折扣 -不需要' ,\n" +
                "    coupon_discount DECIMAL(32,8)    COMMENT '促销券折扣 -不需要' ,\n" +
                "    pay_amount DECIMAL(32,8)    COMMENT '折后价 = 商品总价-商品促销折扣' ,\n" +
                "    coupon_amount DECIMAL(32,8)    COMMENT '券分摊-不需要' ,\n" +
                "    point_amount DECIMAL(32,8)    COMMENT '积分支付分摊（金额）-不需要' ,\n" +
                "    pay_discount DECIMAL(32,8)    COMMENT '支付平台优惠' ,\n" +
                "    merchant_discount DECIMAL(32,8)    COMMENT '商户支付折扣' ,\n" +
                "    give_point DECIMAL(32,8)    COMMENT '赠送积分 基础积分 -不需要' ,\n" +
                "    weight DECIMAL(32,8)    COMMENT '商品重量（单） 单位kg -不需要' ,\n" +
                "    sale_unit VARCHAR(32)    COMMENT '销售单位8' ,\n" +
                "    item_type INT    COMMENT '商品行类型 0=普通商品，1=组合商品' ,\n" +
                "    sale_type INT    COMMENT '销售类型 0=普通销售，1=赠品' ,\n" +
                "    erp_category VARCHAR(64)    COMMENT 'ERP分类 编码' ,\n" +
                "    seller_no VARCHAR(32)    COMMENT '销售员' ,\n" +
                "    created_at DATETIME    COMMENT '创建时间' ,\n" +
                "    updated_at DATETIME    COMMENT '更新时间' ,\n" +
                "    tips VARCHAR(128)    COMMENT '商品行文案100' ,\n" +
                "    PRIMARY KEY (id)\n" +
                ") COMMENT = '商品明细表 商品明细表';/*SQL@Run*/";
        return sql;
    }

    private void toUpdateSQL(List<String> columnList){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<set>").append("\r\n");
        for (int i = 0; i < columnList.size(); i++) {
            String fieldName = underlineToCamel(columnList.get(i));
            // <if test="postId != null">and post_id = #{postId}</if>
            stringBuilder.append("<if test=\"").append(fieldName).append(" != null\">, ").append(columnList.get(i)).append(" = #{").append(fieldName).append("}</if>").append("\r\n");
        }
        stringBuilder.append("</set>").append("\r\n");
        System.out.println(stringBuilder);
    }
    // <if test="postId != null">and post_id = #{postId}</if>
    private void toTestIf(List<String> columnList){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<where>").append("\r\n");
        for (int i = 0; i < columnList.size(); i++) {
            String fieldName = underlineToCamel(columnList.get(i));
            // <if test="postId != null">and post_id = #{postId}</if>
            stringBuilder.append("<if test=\"").append(fieldName).append(" != null\">and ").append(columnList.get(i)).append(" = #{").append(fieldName).append("}</if>").append("\r\n");
        }
        stringBuilder.append("and deleted = 0").append("\r\n");
        stringBuilder.append("</where>").append("\r\n");
        System.out.println(stringBuilder);

    }



    private void toBaseSelectSql(List<String> columnList){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<sql id=\"selectSql\">").append("\r\n");
        for (int i = 0; i < columnList.size(); i++) {
            stringBuilder.append("`t.").append(columnList.get(i)).append("`").append(", ");
            stringBuilder.append("\r\n");
        }
        stringBuilder.replace(stringBuilder.lastIndexOf(","),stringBuilder.lastIndexOf(",")+1,"");
        stringBuilder.append("\r\n").append("</sql>");
        System.out.println(stringBuilder);
    }

    private void toBatchInsertValue(List<String> columnList){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<sql id=\"batchInsertValue\">").append("\r\n");
        for (int i = 0; i < columnList.size(); i++) {
            stringBuilder.append("#{item.").append(underlineToCamel(columnList.get(i))).append("}, ");
            stringBuilder.append("\r\n");
        }
        stringBuilder.replace(stringBuilder.lastIndexOf(","),stringBuilder.lastIndexOf(",")+1,"");
        stringBuilder.append("\r\n").append("</sql>");
        System.out.println(stringBuilder);
    }

    private void toInsertValue(List<String> columnList){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<sql id=\"insertValue\">").append("\r\n");
        for (int i = 0; i < columnList.size(); i++) {
            stringBuilder.append("#{").append(underlineToCamel(columnList.get(i))).append("}, ");
            stringBuilder.append("\r\n");
        }
        stringBuilder.replace(stringBuilder.lastIndexOf(","),stringBuilder.lastIndexOf(",")+1,"");
        stringBuilder.append("\r\n").append("</sql>");
        System.out.println(stringBuilder);
    }
    private void toInsertColumn(List<String> columnList){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<sql id=\"insertColumn\">").append("\r\n");
        int count = 1;
        for (int i = 0; i < columnList.size(); i++) {
            stringBuilder.append("`").append(columnList.get(i)).append("`").append(", ");
            if(count++ % 5 == 0){
                stringBuilder.append("\r\n");
            }
        }
        stringBuilder.replace(stringBuilder.lastIndexOf(","),stringBuilder.lastIndexOf(",")+1,"");
        stringBuilder.append("\r\n").append("</sql>");
        System.out.println(stringBuilder);
    }
    private void toResultMapper(List<String> columnList){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < columnList.size(); i++) {
            stringBuilder.append("<result property=\"")
                    .append(underlineToCamel(columnList.get(i)))
                    .append("\" column=\"")
                    .append(columnList.get(i))
                    .append("\"/>")
                    .append("\r\n");
        }
        System.out.println(stringBuilder.toString());
    }
    private String underlineToCamel(String line){
        boolean smallCamel = true;
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

}
