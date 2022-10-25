package com.tester.testersearch;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testersearch.model.Knowledge;
import com.tester.testersearch.service.helper.DocumentHelper;
import com.tester.testersearch.util.EsSearchHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;


@Slf4j
public class NormalTest_ES {

    public DocumentHelper documentHelper = new DocumentHelper();

    @Test
    public void test_firstRequest() throws Exception {
        String key = null;
        Knowledge kn = new Knowledge();
        kn.setType(2);
        kn.setTitle("黑暗");
//        SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
//                        .index("test_knowledge")
//                        .query(q -> q
//                                .bool(q1 -> q1.should(l -> l.match(e -> e.field("detail").query("森林").analyzer("ik_smart").minimumShouldMatch("2").boost(1f)))
//                                        .should(l -> l.term(e -> e.field("keyword").value("欧莎").boost(99f)))
////                                        .should(l -> l.multiMatch(e -> e.fields(Arrays.asList("description", "keyword")).query("转眼之间")))
//                                )).size(10).from(0),
//        Knowledge.class);
        SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
                .index("test_knowledge")
                .query(q -> q.bool(q1 -> baseProcess(q1, kn))
//                                        .should(l -> l.multiMatch(e -> e.fields(Arrays.asList("description", "keyword")).query("转眼之间")))
                ).size(10).from(0), Knowledge.class);

        for (Hit<Knowledge> hit : search.hits().hits()) {
            processKnowledge(hit.source());
        }
    }

    public BoolQuery.Builder baseProcess(BoolQuery.Builder queryBuilder, Knowledge knowledge) {
        Field[] fields = knowledge.getClass().getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            field.setAccessible(true);
            String o = null;
            try {
                Object o1 = field.get(knowledge);
                if (null == o1) {
                    continue;
                }
                o = String.valueOf(field.get(knowledge));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            processField_ik_smart(queryBuilder, name, o, EsSearchHelper.fieldBoostMap.get(name), EsSearchHelper.fieldAnalyzerMap.get(name));
        }
        return queryBuilder;
    }


    public BoolQuery.Builder processField_ik_smart(BoolQuery.Builder queryBuilder, String fieldName, String fieldValue, Float boost, String analyzer) {
        if (StringUtils.isEmpty(fieldValue)) {
            return queryBuilder;
        }
        if (null == boost) {
            boost = 1.0f;
        }
        final Float finalBoost = boost;

        queryBuilder.filter(f -> f.term(t -> t.field(fieldName).value(1)));

        return queryBuilder.should(l -> l.match(e -> e.field(fieldName).query(fieldValue).analyzer("ik_smart").minimumShouldMatch("2").boost(finalBoost)));
    }


    @Test
    public void test_update() throws Exception {
        Knowledge knowledge = new Knowledge();
        knowledge.setCode("00000002").setTitle("镖车运输时间点_2022-8-23 10:23:36").setUpdatedTime(new Date());
        documentHelper.myUpdate(e -> e.id("00000002").index("test_knowledge").doc(knowledge), Knowledge.class);
    }

    private void processKnowledge(Knowledge knowledge) {
        System.out.println("knowledge = " + knowledge);
    }


    @Test
    public void test_create() throws Exception {
        commonCreate((e) -> {
            e.setType(2)
                    .setCode("00000001")
                    .setKeyword("挪威的森林")
                    .setTitle("挪威的森林")
                    .setDescription("挪威的森林")
                    .setDetail("每个人都有属于自己的一片森林，也许我们 从来不曾去过，但它一直在那里，总会在那里。迷失的人迷失了，相逢的人会再相逢。")
                    .setAuthor("村上村树");
        });
        commonCreate((e) -> {
            e.setType(1)
                    .setCode("00000002")
                    .setKeyword("德克廉 欧莎 寂静谷 黑渊 特步特")
                    .setTitle("冬眠者")
                    .setDescription("冬眠者")
                    .setDetail("转眼之间，荒芜的错觉幻化泯灭。相互铰接的细长黑色建筑模块从四周的黑暗中延伸到你的跟前。当无人机的目标系统锁定住你的飞船的时候，感应器开始疯狂地把尖啸声送进你的听觉中枢。你并不孤独，或许你从来就没有孤独过。\\n冬眠者出现了。")
                    .setAuthor("eve");
        });
        commonCreate((e) -> {
            e.setType(2)
                    .setCode("00000003")
                    .setKeyword("伟大的心魂 风雨 自由")
                    .setTitle("伟大的心魂有如崇山峻岭")
                    .setDescription("伟大的心魂有如崇山峻岭")
                    .setDetail("伟大的心魂有如崇山峻岭，风雨吹荡它，云翳包围它，但人们在那里呼吸时，比别处更自由更有力。纯洁的大气可以洗涤心灵的秽浊，而云翳破散的时候，他威临着人类了。")
                    .setAuthor("somebody");
        });
        commonCreate((e) -> {
            e.setType(2)
                    .setCode("00000004")
                    .setKeyword("黑暗森林 三体")
                    .setTitle("黑暗森林")
                    .setDescription("宇宙是一座黑暗森林，每个文明都是")
                    .setDetail("宇宙是一座黑暗森林，每个文明都是带枪的猎人，像幽灵般潜行于林间，轻轻拨开挡路的树枝，竭力不让脚步发出一点儿声音，连呼吸都小心翼翼，他必须小心，因为林中到处都有与他一样潜行的猎人。如果他发现了别的生命，不管是不是猎人，不管是天使还是魔鬼，不管是娇嫩的婴儿还是步履蹒跚的老人，也不管是天仙般的少女还是天神般的男神，能做的只有一件事：开枪消灭之，在这片森林中，他人就是地狱，就是永恒的威胁，任何暴露自己存在的生命都将很快被消灭。这就是宇宙文明的图景，这就是对费米悖论的解释，这也就是宇宙的黑暗森林法则。")
                    .setAuthor("刘慈欣");
        });
        commonCreate((e) -> {
            e.setType(2)
                    .setCode("00000005")
                    .setKeyword("唐吉诃德 骑士 桑丘")
                    .setTitle("唐·吉诃德")
                    .setDescription("我愿意跟随那些过往的真正骑士的脚步")
                    .setDetail("我愿意跟随那些过往的真正骑士的脚步 \n" +
                            "         在沉沉入睡的荒野中—— \n" +
                            "         信马漫步。 \n" +
                            "         我的命运将紧连着动人的传说， \n" +
                            "         追随自己的信念—— \n" +
                            "         将是我一生的行为。 \n" +
                            "         岁月啊…… \n" +
                            "         究竟是在无尽的幻想中 \n" +
                            "         昏然流逝而去 \n" +
                            "         还是应该 \n" +
                            "         在广阔的世界中 \n" +
                            "         刻下万世留名的战绩？ \n" +
                            "         堂吉诃德， \n" +
                            "         一个年老的乡村绅士， \n" +
                            "         怀着伟大骑士的灵魂 \n" +
                            "         苦苦思索着无人能明白的理想 \n" +
                            "         在庸碌现实中——想非现实的梦， \n" +
                            "         他寻找着梦境。 \n" +
                            "         往昔多么美妙 \n" +
                            "         骑士、公主、骏马和恶龙 \n" +
                            "         如今多么黯淡 \n" +
                            "         村绅、村姑、骡子与绵羊 \n" +
                            "         此刻，谁在世上奔走哭泣？ \n" +
                            "         谁在世上横行施暴？ \n" +
                            "         你睁开眼 \n" +
                            "         ——眼中只有 \n" +
                            "         怜悯弱者的哭泣。 \n" +
                            "         你闭上眼 \n" +
                            "         ——耳中只有 \n" +
                            "         巨兽咆哮的风暴。 \n" +
                            "         这世界需要拯救，需要伟大的堂吉诃德骑士。 \n" +
                            "         堂吉诃德带着桑丘开始了他们伟大的出征， \n" +
                            "         他们是那么可笑，内心却又那么认真。 \n" +
                            "         可怜的桑丘 \n" +
                            "         他什么也不知道 \n" +
                            "         噢！这没关系 \n" +
                            "         因为他相信 \n" +
                            "         他相信主人许诺给他岛屿 \n" +
                            "         就像相信死后能得到天堂。 \n" +
                            "         田野上的风车 \n" +
                            "         在他眼中是—— \n" +
                            "         放肆的巨人，呼啸舞动的长臂 \n" +
                            "         战斗是命运给骑士最好的安排。 \n" +
                            "         “上帝， \n" +
                            "         老天爷， \n" +
                            "         主啊， \n" +
                            "         救救他……” \n" +
                            "         在桑丘的呐喊助威下 \n" +
                            "         堂吉诃德拿起长矛 \n" +
                            "         冲向风车 \n" +
                            "         ……啊！ \n" +
                            "         堂吉诃德和马飞上了天 \n" +
                            "         飞上去的是梦想 \n" +
                            "         掉下来的是实实在在的土地 \n" +
                            "         告诉我这是怎么回事？ \n" +
                            "         一定是魔法师的妒忌 \n" +
                            "         才将巨人变成风车。 ")
                    .setAuthor("塞万提斯·萨维德拉");
        });
    }

    public CreateResponse commonCreate(MyConsumer<Knowledge> consumer) throws Exception {
        Date date = new Date();
        Knowledge knowledge = new Knowledge();
        knowledge
//                .setType(2)
//                .setCode("00000001")
                .setBelong("wenc")
//                .setKeyword("挪威的森林")
//                .setTitle("挪威的森林")
//                .setDescription("挪威的森林")
//                .setDetail("每个人都有属于自己的一片森林，也许我们 从来不曾去过，但它一直在那里，总会在那里。迷失的人迷失了，相逢的人会再相逢。")
//                .setAuthor("村上村树")
                .setPriority(1)
                .setDeleted(0)
                .setStartTime(DocumentHelper.getVeryStartTime())
                .setEndTime(DocumentHelper.getVeryEndTime())
                .setCreatedBy("wenc")
                .setCreatedTime(date)
                .setUpdatedBy("wenc")
                .setUpdatedTime(date);
        consumer.accept(knowledge);
        return documentHelper.myCreate(e -> e.id(knowledge.getCode()).index("test_knowledge").document(knowledge));
    }


}
