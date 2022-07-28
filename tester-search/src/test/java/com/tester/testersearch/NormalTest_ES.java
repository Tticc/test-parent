package com.tester.testersearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import com.tester.testersearch.esUtil.Knowledge;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Function;


@Slf4j
public class NormalTest_ES {
    public static ElasticsearchClient client;
    public static ElasticsearchAsyncClient asyncClient;
    public static RestHighLevelClient hlrc;

    static {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("10.10.38.4", 9200)).build();
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        // And create the API client
        client = new ElasticsearchClient(transport);
        asyncClient = new ElasticsearchAsyncClient(transport);
//        hlrc = new RestHighLevelClientBuilder(restClient)
//                .setApiCompatibilityMode(true)
//                .build();
    }


    @Test
    public void test_firstRequest() throws Exception {
        SearchResponse<Knowledge> search = mySearch(s -> s
                        .index("test_knowledge")
                        .query(q -> q
                                .bool(q1 -> q1.should(l -> l.match(m -> m.field("desc").query("转眼之间")))
//                                .term(t -> t
//                                        .field("desc")
//                                        .value(v -> v.stringValue("转眼之间"))
//                                )
                                )).size(10).from(0),
                Knowledge.class);

        for (Hit<Knowledge> hit : search.hits().hits()) {
            processProduct(hit.source());
        }
    }

    public void processProduct(Knowledge knowledge) {
        System.out.println("knowledge = " + knowledge);
    }


    /**
     * @Date 15:27 2022/7/27
     * @Author 温昌营
     * @see ElasticsearchClient#search(java.util.function.Function, java.lang.Class)
     **/
    public final <TDocument> SearchResponse<TDocument> mySearch(
            Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>> fn, Class<TDocument> tDocumentClass)
            throws IOException, ElasticsearchException {
        SearchRequest build = fn.apply(new SearchRequest.Builder()).build();
        log.info("request info:{}", build);
        return client.search(build, tDocumentClass);
    }
//    public final <TDocument> SearchResponse<TDocument> myHlrcSearch(
//            Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>> fn, Class<TDocument> tDocumentClass)
//            throws IOException, ElasticsearchException {
//        SearchRequest build = fn.apply(new SearchRequestBuilder()).build();
//        log.info("request info:{}", build);
//        return hlrc.search(build, null);
//    }


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
