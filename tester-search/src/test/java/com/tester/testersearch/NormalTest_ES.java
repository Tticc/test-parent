package com.tester.testersearch;

import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.tester.testersearch.util.DocumentHelper;
import com.tester.testersearch.model.Knowledge;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;


@Slf4j
public class NormalTest_ES {

    public DocumentHelper documentHelper = new DocumentHelper();

    @Test
    public void test_create() throws Exception {
        Date date = new Date();
        Knowledge knowledge = new Knowledge();
        knowledge.setType(1)
                .setCode("00000002")
                .setBelong("wenc")
                .setKeyword("镖车")
                .setTitle("镖车运输时间点")
                .setDescription("镖车时间点")
                .setDetail("0-2雪原2-4古树4-6碧波----6-8雪原8-10古树10-12碧波----12-14雪原14-16古树16-18碧波----18-20雪原20-22古树22-24碧波")
                .setAuthor("wenc")
                .setPriority(1)
                .setDeleted(0)
                .setStartTime(date)
                .setEndTime(date)
                .setCreatedBy("wenc")
                .setCreatedTime(date)
                .setUpdatedBy("wenc")
                .setUpdatedTime(date);
        CreateResponse test_knowledge = documentHelper.myCreate(e -> e.id(knowledge.getCode()).index("test_knowledge").document(knowledge));
        System.out.println("test_knowledge = " + test_knowledge);
    }

    @Test
    public void test_update() throws Exception {
        Knowledge knowledge = new Knowledge();
        knowledge.setCode("00000002").setTitle("镖车运输时间点_2022-8-23 10:23:36").setUpdatedTime(new Date());
        documentHelper.myUpdate(e -> e.id("00000002").index("test_knowledge").doc(knowledge), Knowledge.class);
    }

    @Test
    public void test_firstRequest() throws Exception {
        SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
                        .index("test_knowledge")
                        .query(q -> q
                                .bool(q1 -> q1.should(l -> l.match(e -> e.field("description").query("转眼之间").analyzer("ik_max_word")))
                                        .should(l -> l.term(e -> e.field("keyword").value("欧莎")))
                                        .should(l -> l.multiMatch(e -> e.fields(Arrays.asList("description", "keyword")).query("转眼之间")))
                                )).size(10).from(0),
                Knowledge.class);

        for (Hit<Knowledge> hit : search.hits().hits()) {
            processKnowledge(hit.source());
        }
    }

    private void processKnowledge(Knowledge knowledge) {
        System.out.println("knowledge = " + knowledge);
    }




    @Test
    public void printSql() {
        String branch = "feature-hk-release-20220831";
        StringBuilder sb = new StringBuilder();
        sb.append("{\"area\": \"hk\",\"env\": \"SIT-HK\",\"list\": [");
        String str = getStr();
        String[] split = str.split(",");
        for (String s : split) {
            String[] split1 = s.split(":");
            sb.append("{\"gitTagBranch\": \"" + branch + "\",\"server\": \"" + split1[1] + "\",\"jobName\": \"" + split1[2] + "\",\"id\": " + split1[0] + "},");
        }
        sb.append("]}");
        System.out.println(sb.toString());
    }


    private String getStr() {
        return "583:middleend-support:middleend-support-api,584:middleend-support:middleend-support-backend,587:middleend-member:middleend-member-backend,590:middleend-member:middleend-member-api,593:app:app-api,594:middleend-member:middleend-member-dto,595:middleend-member:middleend-member-client,610:card-platform:card-platform-backend,611:card-platform:card-platform-api,612:card-platform:card-platform-job,639:middleend-marketing:middleend-marketing-api,640:middleend-marketing:middleend-marketing-backend,641:middleend-marketing:middleend-marketing-job,642:picking:picking-app-api,748:middleend-auth:middleend-auth-starter,778:dubhe-excel:dubhe-excel-api,807:middleend:frontend-middleend-framework-hk,808:middleend:frontend-middleend-admin-hk,809:middleend:frontend-middleend-order-hk,930:middleend-member:middleend-member-backend-client,1019:middleend-auth:middleend-auth-api,1020:middleend-auth:middleend-auth-backend,1021:middleend-auth:middleend-auth-job,1022:middleend-auth:middleend-login-server,1040:middleend-payment:payment-backend-client,1041:middleend-payment:payment-client,1042:middleend-payment:payment-safety-client,1043:middleend-payment:payment-safety-backend-client,1044:middleend-payment:payment-apppay-api,1046:middleend-payment:payment-backend,1047:middleend-payment:payment-notify-api,1048:middleend-payment:payment-job,1049:middleend-payment:payment-safety-api,1050:middleend-payment:payment-safety-backend,1051:middleend-payment:payment-safety-job,1052:middleend-fulfillment:middleend-fulfillment-client,1055:middleend-member:middleend-member-mq,1056:middleend-member:middleend-member-job,1057:middleend-product:middleend-product-api,1058:middleend-product:middleend-product-backend,1059:middleend-product:middleend-product-job,1060:middleend-product:middleend-product-client,1061:middleend-product:middleend-product-backend-client,1062:middleend-search:search-delivery-api,1063:middleend-search:search-delivery-prod-job,1064:middleend-coupon:middleend-coupon-api,1065:middleend-coupon:middleend-coupon-backend,1066:middleend-coupon:middleend-coupon-job,1067:middleend-coupon:middleend-coupon-client,1068:middleend-coupon:middleend-coupon-backend-client,1069:middleend-marketing:middleend-marketing-client,1070:middleend-marketing:middleend-marketing-backend-client,1071:middleend-message:middleend-message-backend-client,1072:middleend-message:middleend-message-client,1073:middleend-message:middleend-message-api,1074:middleend-message:middleend-message-backend,1075:middleend-message:middleend-message-job,1076:middleend-order:middleend-order-api,1077:middleend-order:middleend-order-client,1078:middleend-order:middleend-order-backend-client,1079:middleend-order:middleend-order-backend,1080:middleend-order:middleend-order-callback,1081:middleend-order:middleend-order-job,1082:cloud-pos:cloud-pos-api,1083:cloud-pos:cloud-pos-backend,1084:cloud-pos:cloud-pos-job,1085:middleend-decoration:middleend-decoration-api,1086:middleend-decoration:middleend-decoration-client,1087:middleend-decoration:middleend-decoration-backend,1088:middleend-decoration:middleend-decoration-job,1089:middleend-fulfillment:middleend-fulfillment-api,1090:middleend-fulfillment:middleend-fulfillment-backend,1091:middleend-fulfillment:middleend-fulfillment-backend-client,1092:middleend-fulfillment:middleend-fulfillment-job,1094:middleend-fulfillment:middleend-fulfillment-callback,1095:middleend-support:middleend-support-client,1096:middleend-support:middleend-support-backend-client,1098:middleend-coupon:middleend-coupon-dto,1104:dubhe-excel:dubhe-excel-job,1105:middleend-promo:middleend-promo-dto,1106:middleend-promo:middleend-promo-backend-client,1107:middleend-promo:middleend-promo-client,1108:middleend-promo:middleend-promo-api,1109:middleend-promo:middleend-promo-job,1110:middleend-promo:middleend-promo-erp-sync,1111:middleend-promo:middleend-promo-backend,1112:middleend-search:search-coupon-dto,1113:middleend-search:search-coupon-client,1114:middleend-search:search-coupon-job,1115:middleend-search:search-coupon-api,1117:middleend-search:search-delivery-prom-job,1118:middleend-support:middleend-support-dto,1120:middleend-support:middleend-support-job,1121:middleend-search:search-delivery-backend-client,1122:middleend-search:search-delivery-client,1123:middleend-search:search-delivery-backend,1124:dubhe:dubhe,1125:cloud-pos:cloud-pos-msg,1223:verify-code:verify-code-backend-client,1224:verify-code:verify-code-client,1225:verify-code:verify-code-backend-server,1226:verify-code:verify-code-server,1235:assets:assets-client,1236:assets:assets-api,1237:middleend-invoice:middleend-invoice-api,1238:middleend-invoice:middleend-invoice-backend,1246:m站:frontend-m-h5-hk,1247:picking:picking-client,1262:picking:picking-api,1263:picking:picking-backend,1268:gateway:gateway-exterior,1269:gateway:gateway-middleend,1277:middleend-marketing:middleend-marketing-dto,1320:data-transfer-platform:dtp-backend,1321:data-transfer-platform:dtp-job,1322:data-transfer-platform:dtp-server,1323:data-transfer-platform:dtp-transfer-starter,1344:middleend-report:middleend-report-backend,1345:middleend-report:middleend-report-backend-client,1346:middleend-report:middleend-report-backend-job,1365:open:open-api,2573:audit-log:middleend-auditlog,2666:open:open-biz-api,2690:open:open-backend,2691:open:open-backend-client,2694:access-auth:access-auth-api,2695:access-auth:access-auth-dto,2696:access-auth:access-auth-client,2697:popularize:middleend-popularize-api-client,2698:popularize:middleend-popularize-backend-client,2699:popularize:middleend-popularize-api,2700:popularize:middleend-popularize-backend,2701:access-auth:access-auth-starter,2702:access-auth:access-auth-backend-client,2763:resource-server:resource-client,2764:resource-server:resource-server-api,2769:m站:frontend-m-framework-hk,2790:open:open-client,3122:middleend-payment:payment-refund-api,3297:xxl-job-admin:xxl-job-admin,3299:middleend-push:middleend-push-backend";
    }

}
