package com.tester.testercommon.util.sqlgenerator.qywxuserinfo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class QywxSingleUserInfoBO extends BaseQywxTransforBO {
        /** 成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节*/
        private String userid;
        /** 成员名称，此字段从2019年12月30日起，对新创建第三方应用不再返回，2020年6月30日起，对所有历史第三方应用不再返回，后续第三方仅通讯录应用可获取，第三方页面需要通过通讯录展示组件来展示名字*/
        private String name;
        /** 手机号码，第三方仅通讯录应用可获取*/
        private String mobile;
        /** 成员所属部门id列表，仅返回该应用有查看权限的部门id*/
        private List<Integer> department;
        /** 部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。值范围是[0, 2^32)*/
        private List<Integer> order;
        /** 职务信息；第三方仅通讯录应用可获取*/
        private String position;
        /** 性别。0表示未定义，1表示男性，2表示女性*/
        private Integer gender;
        /** 邮箱，第三方仅通讯录应用可获取*/
        private String email;
        /** 表示在所在的部门内是否为上级。；第三方仅通讯录应用可获取*/
        private List<Integer> is_leader_in_dept;
        /** 头像url。 第三方仅通讯录应用可获取*/
        private String avatar;
        /** 头像缩略图url。第三方仅通讯录应用可获取*/
        private String thumb_avatar;
        /** 座机。第三方仅通讯录应用可获取*/
        private String telephone;
        /** 成员启用状态。1表示启用的成员，0表示被禁用。注意，服务商调用接口不会返回此字段*/
        private Integer enable;
        /** 别名；第三方仅通讯录应用可获取*/
        private String alias;
        /** 扩展属性，第三方仅通讯录应用可获取*/
        private Map<String, List<QywxUserExtInfo>> extattr;
        /** 激活状态: 1=已激活，2=已禁用，4=未激活。
         已激活代表已激活企业微信或已关注微工作台（原企业号）。未激活代表既未激活企业微信又未关注微工作台（原企业号）。*/
        private Integer status;
        /** 员工个人二维码，扫描可添加为外部联系人(注意返回的是一个url，可在浏览器上打开该url以展示二维码)；第三方仅通讯录应用可获取*/
        private String qr_code;
        /** 成员对外属性，字段详情见对外属性；第三方仅通讯录应用可获取*/
        private QywxUserExtProfile external_profile;
        /** 对外职务，如果设置了该值，则以此作为对外展示的职务，否则以position来展示。*/
        private String external_position;
        /** 地址。*/
        private String address;


        /** */
        private Integer hide_mobile;
        /** */
        private Integer isleader;
}