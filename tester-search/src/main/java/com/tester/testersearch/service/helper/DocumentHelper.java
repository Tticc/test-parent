package com.tester.testersearch.service.helper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.util.ObjectBuilder;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testersearch.model.Knowledge;
import com.tester.testersearch.service.ESClientInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

/**
 * @Author 温昌营
 * @Date 2022-8-23 10:19:30
 */
@Slf4j
@Component
public class DocumentHelper {

    @Autowired
    private ESClientInterface esClient;


    /**
     * 创建文档
     * @Date 2022-8-23 10:19:30
     */
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
        return myCreate(e -> e.id(knowledge.getCode()).index("test_knowledge").document(knowledge));
    }

    /**
     * 修改文档
     * @Date 2023-1-12 15:35:43
     */
    public CreateResponse commonUpdate(MyConsumer<Knowledge> consumer) throws Exception {
        Date date = new Date();
        Knowledge knowledge = new Knowledge();
        knowledge
                .setUpdatedBy("wenc")
                .setUpdatedTime(date);
        consumer.accept(knowledge);
        UpdateResponse<Knowledge> test_knowledge = myUpdate(e -> e.id(knowledge.getCode()).index("test_knowledge").doc(knowledge), Knowledge.class);
        System.out.println(test_knowledge);
        return null;
    }


    /**
     * 默认使用spring注入的client
     * @Date 2023-1-12 17:46:53
     * @Author 温昌营
     * @see ElasticsearchClient#search(java.util.function.Function, java.lang.Class)
     **/
    public final <TDocument> SearchResponse<TDocument> mySearch(
            Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>> fn, Class<TDocument> tDocumentClass)
            throws IOException, ElasticsearchException {
        return mySearch(esClient.getClient(),fn,tDocumentClass);
    }

    /**
     * @Date 15:27 2022/7/27
     * @Author 温昌营
     * @see ElasticsearchClient#search(java.util.function.Function, java.lang.Class)
     **/
    public static final <TDocument> SearchResponse<TDocument> mySearch(ElasticsearchClient client,
            Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>> fn, Class<TDocument> tDocumentClass)
            throws IOException, ElasticsearchException {
        SearchRequest build = fn.apply(new SearchRequest.Builder()).build();
        log.info("query request info:\n【{}】", build);
        SearchResponse<TDocument> search = client.search(build, tDocumentClass);
        log.info("query response info:\n【{}】", search);
        return search;
    }

    /**
     * 默认使用spring注入的client
     * @Date 2023-1-12 17:46:40
     * @Author 温昌营
     * @see ElasticsearchClient#create(java.util.function.Function)
     **/
    public final <TDocument> CreateResponse myCreate(
            Function<CreateRequest.Builder<TDocument>, ObjectBuilder<CreateRequest<TDocument>>> fn)
            throws IOException, ElasticsearchException {
        return myCreate(esClient.getClient(),fn);
    }

    /**
     * @Date 9:49 2022/8/23
     * @Author 温昌营
     * @see ElasticsearchClient#create(java.util.function.Function)
     **/
    public static final <TDocument> CreateResponse myCreate(ElasticsearchClient client,
            Function<CreateRequest.Builder<TDocument>, ObjectBuilder<CreateRequest<TDocument>>> fn)
            throws IOException, ElasticsearchException {
        CreateRequest<TDocument> build = fn.apply(new CreateRequest.Builder<TDocument>()).build();
        log.info("create request info:\n【{}】", build);
        CreateResponse createResponse = client.create(build);
        log.info("create response info:\n【{}】", createResponse);
        return createResponse;
    }

    /**
     * 默认使用spring注入的client
     * @Date 2023-1-12 17:48:26
     * @Author 温昌营
     * @see ElasticsearchClient#update(java.util.function.Function, java.lang.Class)
     **/
    public final <TDocument, TPartialDocument> UpdateResponse<TDocument> myUpdate(
            Function<UpdateRequest.Builder<TDocument, TPartialDocument>, ObjectBuilder<UpdateRequest<TDocument, TPartialDocument>>> fn,
            Class<TDocument> tDocumentClass) throws IOException, ElasticsearchException {
        return myUpdate(esClient.getClient(),fn,tDocumentClass);
    }

    /**
     * @Date 9:49 2022/8/23
     * @Author 温昌营
     * @see ElasticsearchClient#update(java.util.function.Function, java.lang.Class)
     **/
    public static final <TDocument, TPartialDocument> UpdateResponse<TDocument> myUpdate(ElasticsearchClient client,
            Function<UpdateRequest.Builder<TDocument, TPartialDocument>, ObjectBuilder<UpdateRequest<TDocument, TPartialDocument>>> fn,
            Class<TDocument> tDocumentClass) throws IOException, ElasticsearchException {
        UpdateRequest<TDocument, TPartialDocument> build = fn.apply(new UpdateRequest.Builder<TDocument, TPartialDocument>()).build();
        log.info("update request info:\n【{}】", build);
        UpdateResponse<TDocument> update = client.update(build, tDocumentClass);
        log.info("update response info:\n【{}】", update);
        return update;
    }

    /**
     * 默认使用spring注入的client
     * @Date 2023-1-12 17:49:45
     * @Author 温昌营
     * @see ElasticsearchClient#delete(java.util.function.Function)
     **/
    public final DeleteResponse myDelete(Function<DeleteRequest.Builder, ObjectBuilder<DeleteRequest>> fn)
            throws IOException, ElasticsearchException {
        return myDelete(esClient.getClient(),fn);
    }

    /**
     * @Date 14:22 2022/8/23
     * @Author 温昌营
     * @see ElasticsearchClient#delete(java.util.function.Function)
     **/
    public static final DeleteResponse myDelete(ElasticsearchClient client,
                                         Function<DeleteRequest.Builder, ObjectBuilder<DeleteRequest>> fn)
            throws IOException, ElasticsearchException {
        DeleteRequest build = fn.apply(new DeleteRequest.Builder()).build();
        log.info("delete request info:\n【{}】", build);
        DeleteResponse delete = client.delete(build);
        log.info("delete response info:\n【{}】", delete);
        return delete;
    }


    public static Date getVeryStartTime() {
        LocalDateTime localDateTime = DateUtil.getLocalDateTime("19700101000000");
        return DateUtil.getDateFromLocalDateTime(localDateTime);
    }

    public static Date getVeryEndTime() {
        LocalDateTime localDateTime = DateUtil.getLocalDateTime("99990101000000");
        return DateUtil.getDateFromLocalDateTime(localDateTime);
    }

    /**
     * 初始化数据。<br/>也作为数据插入案例
     *
     * @Date 14:58 2022/10/25
     * @Author 温昌营
     **/
    public void test_data_init() throws Exception {
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


}
