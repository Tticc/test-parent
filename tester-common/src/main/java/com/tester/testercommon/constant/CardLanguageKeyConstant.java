package com.tester.testercommon.constant;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 多语言key</p>
 * 需要严格按照格式增加key，包括注释</p>
 * @Author 温昌营
 * @Date
 */
public class CardLanguageKeyConstant {

    /************************** common *********************************************/
    /**
     * 数据库异常
     */
    public static final String DB_ERROR = "lang.card.platform.common.dbError";
    /**
     * 数据异常
     */
    public static final String DATA_ERROR = "lang.card.platform.common.dataError";
    /**
     * 返回数据验签失败
     */
    public static final String RESPONSE_SIGN_VERIFY_FAILED = "lang.card.platform.common.responseSignVerifyFailed";
    /**
     * 分布式锁获取失败
     */
    public static final String TRY_LOCK_FAILED = "lang.card.platform.common.tryLockFailed";
    /**
     * 没有权限
     */
    public static final String NO_PERMISSION = "lang.card.platform.common.noPermission";

    /************************** member *********************************************/
    /**
     * 获取会员id失败
     **/
    public static final String GET_MEMBER_ID_FAILED = "lang.card.platform.member.getMemberIdFailed";
    /**
     * 没有登录
     */
    public static final String NO_LOGIN = "lang.card.platform.member.noLogin";

    /**
     * 获取店铺信息失败
     */
    public static final String GET_STORE_FAILED = "lang.card.platform.member.getStoreFailed";
    /**
     * 会员信息异常
     */
    public static final String MEMBER_INFO_ERROR = "lang.card.platform.member.memberInfoError";


    /************************** point *********************************************/
    /**
     * 积分操作失败
     **/
    public static final String POINT_OPE_FAILED = "lang.card.platform.point.pointOpeFailed";

    /**
     * 积分利用失败
     */
    public static final String POINT_USE_FAILED = "lang.card.platform.point.pointUseFailed";
    /**
     * 您的积分不够，不能换购！
     */
    public static final String POINT_NOT_ENOUGH = "lang.card.platform.point.pointNotEnough";

    /************************** order *********************************************/
    /**
     * 订单不存在
     **/
    public static final String ORDER_NOT_EXISTS = "lang.card.platform.order.orderNotExists";
    /**
     * 支付回调失败
     **/
    public static final String PAY_CALL_BACK_ERROR = "lang.card.platform.order.payCallBackError";

    /**
     * 支付信息为空
     **/
    public static final String PAY_CODE_IS_NULL = "lang.card.platform.order.payCodeIsNull";
    /**
     * 订单金额与支付金额不一致
     */
    public static final String DIFF_IN_PAY_AND_ORDER_AMOUNT = "lang.card.platform.order.diffInPayAndOrderAmount";
    /**
     * 未支付取消
     */
    public static final String CANCELED_BEFORE_PAY = "lang.card.platform.order.canceledBeforePay";

    /**
     * 订单信息获取失败
     */
    public static final String ORDER_FETCH_FAILED = "lang.card.platform.order.orderFetchFailed";

    /**
     * 无效的订单号
     */
    public static final String INVALID_ORDER_NO = "lang.card.platform.order.invalidOrderNo";
    /**
     * 商品详细信息不能为空
     */
    public static final String EMPTY_GOODS_LIST = "lang.card.platform.order.emptyGoodsList";

    /**
     * 非成功订单不可保存
     */
    public static final String FAILED_ORDER_SHOULD_NOT_SAVE = "lang.card.platform.order.failedOrderShouldNotSave";

    /**
     * 订单支付类型错误
     */
    public static final String ORDER_PAY_TYPE_ERROR = "lang.card.platform.order.orderPayTypeError";
    /**
     * 礼品类型错误
     */
    public static final String GIFT_TYPE_ERROR = "lang.card.platform.order.giftTypeError";
    /**
     * 积分使用失败。{0}
     */
    public static final String CONSUME_POINT_FAILED = "lang.card.platform.order.consumePointFailed";
    /**
     * 当前订单无法取消
     */
    public static final String ORDER_COULD_NOT_CANCEL = "lang.card.platform.order.orderCouldNotCancel";
    /**
     * 订单状态已变更，操作失败
     */
    public static final String OPE_STATUS_ERROR = "lang.card.platform.order.opeStatusError";
    /**
     * 订单状态异常
     */
    public static final String ORDER_STATUS_ERROR = "lang.card.platform.order.orderStatusError";
    /**
     * 销售订单未完成
     */
    public static final String SALES_ORDER_NOT_FINISH = "lang.card.platform.order.salesOrderNotFinish";
    /**
     * 订单查询参数错误
     */
    public static final String ORDER_QUERY_PARAM_ERROR = "lang.card.platform.order.orderQueryParamError";
    /**
     * 当前售后订单不可取消
     */
    public static final String AFTER_SALE_ORDER_COULD_NOT_CANCEL = "lang.card.platform.order.afterSaleOrderCouldNotCancel";
    /**
     * 券发放失败。{0}
     */
    public static final String COUPON_DELIVERY_FAILED = "lang.card.platform.order.couponDeliveryFailed";
    /**
     * 礼品优惠活动数据获取失败
     */
    public static final String SECKILL_ITEM_ERROR = "lang.card.platform.order.seckillItemError";
    /**
     * 订单查询失败
     */
    public static final String ORDER_QUERY_FAILED = "lang.card.platform.order.orderQueryFailed";
    /**
     * 订单追踪信息查询失败
     */
    public static final String GET_DETAIL_TRACE_FAILED = "lang.card.platform.order.getDetailTraceFailed";
    /**
     * 当前订单不支持退货
     */
    public static final String ORDER_UN_RETURNABLE = "lang.card.platform.order.orderUnReturnable";
    /**
     * 当前订单已存在有效售后单
     */
    public static final String ORDER_HAVE_VALID_AFTER_SALE_RECORD = "lang.card.platform.order.orderHaveValidAfterSaleRecord";
    /**
     * 自提订单核销失败。{0}
     */
    public static final String ORDER_VERIFY_FAILED = "lang.card.platform.order.orderVerifyFailed";
    /**
     * 订单不可核销
     */
    public static final String ORDER_COULD_NOT_VERIFY = "lang.card.platform.order.orderCouldNotVerify";
    /**
     * 券退回失败。{0}
     */
    public static final String COUPON_RETURN_FAILED = "lang.card.platform.order.couponReturnFailed";
    /**
     * 订单已关闭
     */
    public static final String ORDER_CLOSED = "lang.card.platform.order.orderClosed";

    /************************** 礼品卡用卡 *********************************************/
    /**
     * 参数为空
     */
    public static final String PARAM_EMPTY = "lang.card.platform.param.empty";
    /**
     * 礼品卡预支付失败
     */
    public static final String CARD_PLATFORM_PRE_PAY_FAIL = "lang.card.platform.pre.pay.fail";
    /**
     * 礼品卡支付查询失败
     */
    public static final String CARD_PLATFORM_PAY_QUERY_FAIL = "lang.card.platform.pay.query.fail";
    /**
     * 礼品卡余额查询失败
     */
    public static final String CARD_PLATFORM_BALANCE_QUERY_FAIL = "lang.card.platform.balance.query.fail";
    /**
     * 礼品卡预支付ID不能为空
     */
    public static final String CARD_PLATFORM_PREPAY_ID_NOT_EMPTY = "lang.card.platform.prepay.id.not.empty";
    /**
     * 礼品卡支付查询结果不能为空
     */
    public static final String CARD_PLATFORM_PAY_QUERY_NOT_EMPTY = "lang.card.platform.pay.query.not.empty";
    /**
     * 礼品卡退货接口调用失败
     */
    public static final String CARD_PLATFORM_REFUND_FAIL = "lang.card.platform.refund.fail";
    /**
     * 礼品卡支付查询结果不能为空
     */
    public static final String CARD_PLATFORM_REFUND_NOT_EMPTY = "lang.card.platform.refund.not.empty";
    /**
     * 外部接口调用异常
     */
    public static final String EXTERNAL_INTERFACE_CALL_EXCEPTION = "lang.card.platform.external.interface.call.exception";
    /**
     * 外部接口解析异常
     */
    public static final String EXTERNAL_INTERFACE_PARSE_EXCEPTION = "lang.card.platform.external.interface_parse.exception";
    /**
     * 调用外部接口方法名为空
     */
    public static final String EMPTY_REQUEST_METHOD_NAME = "lang.card.platform.empty.request.method.name";
    /**
     * 签名验证失败
     */
    public static final String VALID_FAIL = "lang.card.platform.valid.fail";
    /**
     * 礼品卡卡号魔密支付接口调用异常
     */
    public static final String CARD_PLATFORM_PAN_AND_PIN_PAY_EXCEPTION = "lang.card.platform.pan.and.pin.pay.exception";
    /**
     * 礼品卡卡号魔密支付失败
     */
    public static final String CARD_PLATFORM_PAN_AND_PIN_PAY_FAIL = "lang.card.platform.pan.and.pin.pay.fail";
    /**
     * 礼品卡退货接口异常
     */
    public static final String CARD_PLATFORM_REFUND_QUERY_EXCEPTION = "lang.card.platform.refund.query.exception";
    /**
     * 礼品卡退货接口调用失败
     */
    public static final String CARD_PLATFORM_REFUND_QUERY_FAIL = "lang.card.platform.refund.query.fail";
    /**
     * 礼品卡卡码支付接口调用异常
     */
    public static final String CARD_PLATFORM_PCODE_PAY_EXCEPTION = "lang.card.platform.pcode.pay.exception";
    /**
     * 礼品卡卡码支付失败
     */
    public static final String CARD_PLATFORM_PCODE_PAY_FAIL = "lang.card.platform.pcode.pay.fail";
    /**
     * 礼品卡卡号魔密红冲接口调用异常
     */
    public static final String CARD_PLATFORM_REVOKE_EXCEPTION = "lang.card.platform.pan.and.pin.refund.exception";
    /**
     * 礼品卡卡号魔密红冲失败
     */
    public static final String CARD_PLATFORM_REVOKE_FAIL = "lang.card.platform.pan.and.pin.refund.fail";
    /**
     * 礼品卡卡号魔密查询接口调用异常
     */
    public static final String CARD_PLATFORM_PAN_PIN_QUERY_EXCEPTION = "lang.card.platform.pan.and.pin.query.exception";
    /**
     * 礼品卡卡号魔密查询失败
     */
    public static final String CARD_PLATFORM_PAN_PIN_QUERY_FAIL = "lang.card.platform.pan.and.pin.query.fail";
    /**
     * 礼品卡卡号魔密退货接口调用异常
     */
    public static final String CARD_PLATFORM_PAN_AND_PIN_RD_EXCEPTION = "lang.card.platform.pan.and.pin.rd.exception";
    /**
     * 礼品卡卡号魔密退货失败
     */
    public static final String CARD_PLATFORM_PAN_AND_PIN_RD_FAIL = "lang.card.platform.pan.and.pin.rd.fail";


    /************************** payment *********************************************/
    /**
     * 购卡支付查询失败
     */
    public static final String PAY_QUERY_ERR = "lang.card.platform.payment.payQueryErr";
    /**
     * 查询成功，通知订单失败
     */
    public static final String PAY_QUERY_SUCCESS_ORDER_FAIL = "lang.card.platform.payment.payQuerySuccessOrderFail";
    /**
     * 购卡支付方式错误
     */
    public static final String CARD_PAY_TYPE_ERROR = "lang.card.platform.payment.cardPayTypeError";

    /**
     * 订单未支付，不可冲正
     */
    public static final String ORDER_NOT_PAID_COULD_NOT_REFUND = "lang.card.platform.payment.orderNotPaidCouldNotRefund";

    /**
     * 未找到支付记录
     */
    public static final String NO_PAYMENT_RECORD = "lang.card.platform.payment.noPaymentRecord";

    /**
     * 当前订单状态不可支付
     */
    public static final String ORDER_COULD_NOT_PAY = "lang.card.platform.payment.orderCouldNotPay";
    /**
     * 订单支付中
     */
    public static final String ORDER_PAYING = "lang.card.platform.payment.orderPaying";
    /**
     * 支付结果查询失败
     */
    public static final String PAY_RESULT_QUERY_FAILED = "lang.card.platform.payment.payResultQueryFailed";





    /************************** buy card invoice *********************************************/
    /**
     * 开票状态查询失败
     */
    public static final String INVOICE_STATUS_QUERY_FILED = "lang.card.platform.invoice.invoiceStatusQueryFiled";

    /**
     * 门店可开票状态查询失败
     */
    public static final String STORE_INVOICE_STATUS_QUERY_FILED = "lang.card.platform.invoice.storeInvoiceStatusQueryFiled";

    /**
     * 当前订单不可开发票
     */
    public static final String ORDER_COULD_NOT_OPEN_INVOICE = "lang.card.platform.invoice.orderCouldNotOpenInvoice";

    /**
     * 订单当前状态不可开票
     */
    public static final String ORDER_STATUS_NOT_SUPPORT_INVOICE = "lang.card.platform.invoice.orderStatusNotSupportInvoice";
    /**
     * 开票失败
     */
    public static final String OPEN_INVOICE_ERROR = "lang.card.platform.invoice.openInvoiceError";

    /**
     * 发票抬头为空
     */
    public static final String INVOICE_HEAD_NULL = "lang.card.platform.invoice.invoiceHeadNull";

    /**
     * 订单商品记录为空
     */
    public static final String ORDER_ITEM_EMPTY = "lang.card.platform.invoice.orderItemEmpty";

    /**
     * 订单支付记录为空
     */
    public static final String ORDER_PAYMENT_EMPTY = "lang.card.platform.invoice.orderPaymentEmpty";


    /********************************** order-gift item - 60008xxxx ************************************************/
    /**
     * {0}礼品已经下架咯,请重新下单！
     */
    public static final String GIFT_ITEM_UNMARKETABLE = "lang.card.platform.orderGift.giftItemUnmarketable";
    /**
     * {0}礼品已经抢完咯,请重新下单！
     */
    public static final String GIFT_STOCK_OUT = "lang.card.platform.orderGift.giftStockOut";
    /**
     * {0}礼品购买库存不足,请重新下单！
     */
    public static final String GIFT_STOCK_NOT_ENOUGH = "lang.card.platform.orderGift.giftStockNotEnough";
    /**
     * 礼品不存在
     */
    public static final String GIFT_NOT_EXIST = "lang.card.platform.orderGift.giftNotExist";
    /**
     * 礼品数据异常
     */
    public static final String GIFT_INFO_ERROR = "lang.card.platform.orderGift.giftInfoError";
    /**
     * 秒杀礼品已失效
     */
    public static final String SECKILL_GIFT_INVALID = "lang.card.platform.orderGift.seckillGiftInvalid";
    /**
     * 礼品已失效
     */
    public static final String GIFT_INVALID = "lang.card.platform.orderGift.giftInvalid";
    /**
     * 礼品库存操作失败
     */
    public static final String GIFT_STOCK_OPE_FAILED = "lang.card.platform.orderGift.giftStockOpeFailed";










    // other


    /**
     * ￥
     */
    public static final String RMB = "lang.card.platform.langOnly.rmb";
    /**
     * 积分
     */
    public static final String POINT = "lang.card.platform.langOnly.point";

    /**
     * 顾客
     */
    public static final String CUSTOMER = "lang.card.platform.langOnly.order.customer";

    /**
     * 自提地址：
     */
    public static final String PICKUP_ADDRESS = "lang.card.platform.langOnly.order.pickup_address";


    /**
     * 系统
     */
    public static final String SYSTEM = "lang.card.platform.langOnly.order.system";
    /**
     * 扣减库存失败
     */
    public static final String ORDER_VERIFY_DEDUCT_STOCK_FAILED = "lang.card.platform.langOnly.order.verify.deduct_stock_failed";
    /**
     * 当前订单状态不可核销
     */
    public static final String ORDER_VERIFY_ORDER_COULD_NOT_VERIFY = "lang.card.platform.langOnly.order.verify.order_could_not_verify";




    // sale order - status trace
    /**
     * 创建订单
     */
    public static final String LNAG_SALE_ORDER_TRACE_CREATE = "lang.card.platform.langOnly.sale.order.trace.create";
    /**
     * 已支付积分
     */
    public static final String LNAG_SALE_ORDER_TRACE_POINT_PAID = "lang.card.platform.langOnly.sale.order.trace.point_paid";
    /**
     * 待支付
     */
    public static final String LNAG_SALE_ORDER_TRACE_WAITING_PAY = "lang.card.platform.langOnly.sale.order.trace.waiting_pay";
    /**
     * 已支付
     */
    public static final String LNAG_SALE_ORDER_TRACE_PAID = "lang.card.platform.langOnly.sale.order.trace.paid";
    /**
     * 待审核
     */
    public static final String LNAG_SALE_ORDER_TRACE_WAITING_AUDIT = "lang.card.platform.langOnly.sale.order.trace.waiting_audit";
    /**
     * 已审核
     */
    public static final String LNAG_SALE_ORDER_TRACE_AUDITED = "lang.card.platform.langOnly.sale.order.trace.audited";
    /**
     * 待备货
     */
    public static final String LNAG_SALE_ORDER_TRACE_WAITING_PICKUP = "lang.card.platform.langOnly.sale.order.trace.waiting_pickup";
    /**
     * 已备货
     */
    public static final String LNAG_SALE_ORDER_TRACE_PICKUPED = "lang.card.platform.langOnly.sale.order.trace.pickuped";
    /**
     * 待发货
     */
    public static final String LNAG_SALE_ORDER_TRACE_WAITING_DELIVERY = "lang.card.platform.langOnly.sale.order.trace.waiting_delivery";
    /**
     * 已发货
     */
    public static final String LNAG_SALE_ORDER_TRACE_DELIVERYED = "lang.card.platform.langOnly.sale.order.trace.deliveryed";
    /**
     * 待签收
     */
    public static final String LNAG_SALE_ORDER_TRACE_WAITING_RECEIPT = "lang.card.platform.langOnly.sale.order.trace.waiting_receipt";
    /**
     * 已签收
     */
    public static final String LNAG_SALE_ORDER_TRACE_RECEIPTED = "lang.card.platform.langOnly.sale.order.trace.receipted";
    /**
     * 已完成
     */
    public static final String LNAG_SALE_ORDER_TRACE_FINISHED = "lang.card.platform.langOnly.sale.order.trace.finished";
    /**
     * 未支付取消订单
     */
    public static final String LNAG_SALE_ORDER_TRACE_CANCELED_BEFORE_PAY = "lang.card.platform.langOnly.sale.order.trace.canceled_before_pay";
    /**
     * 审核前取消
     */
    public static final String LNAG_SALE_ORDER_TRACE_CANCELED_BEFORE_AUDIT = "lang.card.platform.langOnly.sale.order.trace.canceled_before_audit";
    /**
     * 审核取消
     */
    public static final String LNAG_SALE_ORDER_TRACE_CANCELED_WHEN_AUDIT = "lang.card.platform.langOnly.sale.order.trace.canceled_when_audit";

    /**
     * 取消审核中
     */
    public static final String LNAG_SALE_ORDER_TRACE_CANCEL_AUDITING = "lang.card.platform.langOnly.sale.order.trace.cancel_auditing";

    /**
     * 取消申请审核中
     */
    public static final String LNAG_SALE_ORDER_TRACE_APPLY_CANCEL = "lang.card.platform.langOnly.sale.order.trace.apply_cancel";
    /**
     * 订单已取消
     */
    public static final String LNAG_SALE_ORDER_TRACE_AGREE_CANCEL_WHEN_AUDIT = "lang.card.platform.langOnly.sale.order.trace.agree_cancel_when_audit";
    /**
     * 订单处理中，取消失败
     */
    public static final String LNAG_SALE_ORDER_TRACE_REJECT_CANCEL = "lang.card.platform.langOnly.sale.order.trace.reject_cancel";

    // sale order - status event
    /**
     * 创建订单，待支付
     */
    public static final String LNAG_SALE_ORDER_EVENT_CREATE = "lang.card.platform.langOnly.sale.order.event.create";
    /**
     * 买家积分付款，待金额付款
     */
    public static final String LNAG_SALE_ORDER_EVENT_POINT_PAID = "lang.card.platform.langOnly.sale.order.event.point_paid";
    /**
     * 买家付款，待审核
     */
    public static final String LNAG_SALE_ORDER_EVENT_PAY_TO_AUDIT = "lang.card.platform.langOnly.sale.order.event.pay_to_audit";
    /**
     * 审核通过，待备货
     */
    public static final String LNAG_SALE_ORDER_EVENT_AUDIT_TO_PICKUP = "lang.card.platform.langOnly.sale.order.event.audit_to_pickup";
    /**
     * 备货完成，待发货
     */
    public static final String LNAG_SALE_ORDER_EVENT_PICKUP_TO_DELIVERY = "lang.card.platform.langOnly.sale.order.event.pickup_to_delivery";
    /**
     * 发货完成，待签收
     */
    public static final String LNAG_SALE_ORDER_EVENT_DELIVERY_TO_RECEIPT = "lang.card.platform.langOnly.sale.order.event.delivery_to_receipt";
    /**
     * 待签收
     */
    public static final String LNAG_SALE_ORDER_EVENT_WAITING_RECEIPT = "lang.card.platform.langOnly.sale.order.event.waiting_receipt";
    /**
     * 已完成
     */
    public static final String LNAG_SALE_ORDER_EVENT_FINISHED = "lang.card.platform.langOnly.sale.order.event.finished";
    /**
     * 未支付取消订单
     */
    public static final String LNAG_SALE_ORDER_EVENT_CANCELED_BEFORE_PAY = "lang.card.platform.langOnly.sale.order.event.canceled_before_pay";
    /**
     * 审核前取消
     */
    public static final String LNAG_SALE_ORDER_EVENT_CANCELED_BEFORE_AUDIT = "lang.card.platform.langOnly.sale.order.event.canceled_before_audit";
    /**
     * 审核取消
     */
    public static final String LNAG_SALE_ORDER_EVENT_CANCELED_WHEN_AUDIT = "lang.card.platform.langOnly.sale.order.event.canceled_when_audit";

    /**
     * 取消审核中
     */
    public static final String LNAG_SALE_ORDER_EVENT_CANCEL_AUDITING = "lang.card.platform.langOnly.sale.order.event.cancel_auditing";

    /**
     * 取消申请审核中
     */
    public static final String LNAG_SALE_ORDER_EVENT_APPLY_CANCEL = "lang.card.platform.langOnly.sale.order.event.apply_cancel";
    /**
     * 订单已取消
     */
    public static final String LNAG_SALE_ORDER_EVENT_AGREE_CANCEL_WHEN_AUDIT = "lang.card.platform.langOnly.sale.order.event.agree_cancel_when_audit";
    /**
     * 订单处理中，取消失败
     */
    public static final String LNAG_SALE_ORDER_EVENT_REJECT_CANCEL = "lang.card.platform.langOnly.sale.order.event.reject_cancel";




    // after sale - trace
    /**
     * 创建中
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_CREATING = "lang.card.platform.langOnly.afterSale.order.trace.creating";
    /**
     * 申请售后
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_APPLY_RETURN = "lang.card.platform.langOnly.afterSale.order.trace.apply_return";
    /**
     * 待审核
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_WAITING_AUDIT = "lang.card.platform.langOnly.afterSale.order.trace.waiting_audit";
    /**
     * 已审核
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_AUDITED = "lang.card.platform.langOnly.afterSale.order.trace.audited";
    /**
     * 待寄回
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_WAITING_SENDBACK = "lang.card.platform.langOnly.afterSale.order.trace.waiting_sendback";
    /**
     * 已寄回
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_SENDBACKED = "lang.card.platform.langOnly.afterSale.order.trace.sendbacked";
    /**
     * 待入库
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_WAITING_STORAGE = "lang.card.platform.langOnly.afterSale.order.trace.waiting_storage";
    /**
     * 已入库
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_STORAGED = "lang.card.platform.langOnly.afterSale.order.trace.storaged";
    /**
     * 退款中
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_REFUNDING = "lang.card.platform.langOnly.afterSale.order.trace.refunding";
    /**
     * 已完成
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_FINISHED = "lang.card.platform.langOnly.afterSale.order.trace.finished";
    /**
     * 已取消
     */
    public static final String LNAG_AFTER_SALE_ORDER_TRACE_CANCELED = "lang.card.platform.langOnly.afterSale.order.trace.canceled";

    // after sale - event
    /**
     * 创建订单
     */
    public static final String LNAG_AFTER_SALE_ORDER_EVENT_CREATING = "lang.card.platform.langOnly.afterSale.order.event.creating";
    /**
     * 待审核
     */
    public static final String LNAG_AFTER_SALE_ORDER_EVENT_WAITING_AUDIT = "lang.card.platform.langOnly.afterSale.order.event.waiting_audit";
    /**
     * 待寄回
     */
    public static final String LNAG_AFTER_SALE_ORDER_EVENT_WAITING_SEND_BACK = "lang.card.platform.langOnly.afterSale.order.event.waiting_send_back";
    /**
     * 待入库
     */
    public static final String LNAG_AFTER_SALE_ORDER_EVENT_WAITING_STORAGE = "lang.card.platform.langOnly.afterSale.order.event.waiting_storage";
    /**
     * 待退款
     */
    public static final String LNAG_AFTER_SALE_ORDER_EVENT_WAITING_REFUND = "lang.card.platform.langOnly.afterSale.order.event.waiting_refund";
    /**
     * 退款中
     */
    public static final String LNAG_AFTER_SALE_ORDER_EVENT_REFUNDING = "lang.card.platform.langOnly.afterSale.order.event.refunding";
    /**
     * 已完成
     */
    public static final String LNAG_AFTER_SALE_ORDER_EVENT_FINISHED = "lang.card.platform.langOnly.afterSale.order.event.finished";
    /**
     * 已取消
     */
    public static final String LNAG_AFTER_SALE_ORDER_EVENT_CANCELED = "lang.card.platform.langOnly.afterSale.order.event.canceled";


    // app
    /**
     * 确认订单
     */
    public static final String DEFAULT_STORE_ORDER_TIPS = "lang.card.platform.app.langOnly.order.sale.confirm.store.order.tips";
    /**
     * 订单删除成功
     */
    public static final String ORDER_DELETED_FOR_MEMBER = "lang.card.platform.app.langOnly.order.orderDeletedForMember";
    /**
     * 退货申请成功
     */
    public static final String ORDER_RETURN_SUCCESS_TITLE = "lang.card.platform.app.langOnly.order.afterSale.orderReturnSuccessTitle";
    /**
     * 客服备注
     */
    public static final String APP_ORDER_AFTER_SALE_SERVICE_REMARK = "lang.card.platform.app.langOnly.order.after.sale.service_remark";
    /**
     * 积分商城
     */
    public static final String APP_ORDER_AFTER_SALE_ORDER_NO_TITLE = "lang.card.platform.app.langOnly.order.after.sale.order_no_title";
    /**
     * 1.该单已用积分，会返还到您账户。
     */
    public static final String APP_ORDER_AFTER_SALE_APPLY_RETURN_TIPS = "lang.card.platform.app.langOnly.order.after.sale.apply_return_tips";


    // sale - app
    // status name
    /**
     * 待付款
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_WAITING_PAY = "lang.card.platform.app.langOnly.order.sale.status.name.waiting_pay";
    /**
     * 已下单
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_ORDERED = "lang.card.platform.app.langOnly.order.sale.status.name.ordered";
    /**
     * 发放中
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_SENDING = "lang.card.platform.app.langOnly.order.sale.status.name.sending";
    /**
     * 已发放
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_SENT = "lang.card.platform.app.langOnly.order.sale.status.name.sent";
    /**
     * 已发货
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_DELIVERYED = "lang.card.platform.app.langOnly.order.sale.status.name.deliveryed";
    /**
     * 待自提
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_WAITING_TAKE = "lang.card.platform.app.langOnly.order.sale.status.name.waiting_take";
    /**
     * 已完成
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_FINISHED = "lang.card.platform.app.langOnly.order.sale.status.name.finished";
    /**
     * 取消订单审核中
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_CANCEL_AUDITING = "lang.card.platform.app.langOnly.order.sale.status.name.cancel_auditing";
    /**
     * 已关闭
     */
    public static final String APP_ORDER_SALE_STATUS_NAME_CANCEL_CLOSED = "lang.card.platform.app.langOnly.order.sale.status.name.closed";

    // tips
    /**
     * 订单正在处理中，请耐心等待~
     */
    public static final String APP_ORDER_SALE_TIPS_ORDER_PROCESSING = "lang.card.platform.app.langOnly.order.sale.tips.order_processing";
    /**
     * 优惠券已发放~
     */
    public static final String APP_ORDER_SALE_TIPS_COUPON_SENT = "lang.card.platform.app.langOnly.order.sale.tips.coupon_sent";
    /**
     * 商品已准备好，请前往门店取货~
     */
    public static final String APP_ORDER_SALE_TIPS_WAITING_TAKE = "lang.card.platform.app.langOnly.order.sale.tips.waiting_take";
    /**
     * 商家已发货，请注意查收~
     */
    public static final String APP_ORDER_SALE_TIPS_WAITING_RECEIPT = "lang.card.platform.app.langOnly.order.sale.tips.waiting_receipt";
    /**
     * 订单已完成，欢迎下次购买~
     */
    public static final String APP_ORDER_SALE_TIPS_FINISHED = "lang.card.platform.app.langOnly.order.sale.tips.finished";
    /**
     * 该订单已取消({0})~
     */
    public static final String APP_ORDER_SALE_TIPS_CANCELED = "lang.card.platform.app.langOnly.order.sale.tips.canceled";


    //after sale - app
    // status name
    /**
     * 待审核
     */
    public static final String APP_ORDER_AFTER_SALE_STATUS_NAME_WAITING_AUDIT = "lang.card.platform.app.langOnly.order.after.sale.status.name.waiting_audit";

    /**
     * 待寄回
     */
    public static final String APP_ORDER_AFTER_SALE_STATUS_NAME_WAITING_SEND_BACK = "lang.card.platform.app.langOnly.order.after.sale.status.name.waiting_send_back";

    /**
     * 商品退回中
     */
    public static final String APP_ORDER_AFTER_SALE_STATUS_NAME_WAITING_STORAGE = "lang.card.platform.app.langOnly.order.after.sale.status.name.waiting_storage";

    /**
     * 退款中
     */
    public static final String APP_ORDER_AFTER_SALE_STATUS_NAME_REFUNDING = "lang.card.platform.app.langOnly.order.after.sale.status.name.refunding";

    /**
     * 取消退货
     */
    public static final String APP_ORDER_AFTER_SALE_STATUS_NAME_CANCELED = "lang.card.platform.app.langOnly.order.after.sale.status.name.canceled";

    /**
     * 退款成功
     */
    public static final String APP_ORDER_AFTER_SALE_STATUS_NAME_FINISHED = "lang.card.platform.app.langOnly.order.after.sale.status.name.finished";

    // tips
    /**
     * 请耐心等待客服处理
     */
    public static final String APP_ORDER_AFTER_SALE_TIPS_ORDER_PROCESSING = "lang.card.platform.app.langOnly.order.after.sale.tips.order_processing";
    /**
     * 请快递寄回，并填写物流单号
     */
    public static final String APP_ORDER_AFTER_SALE_TIPS_WAITING_SENDBACK = "lang.card.platform.app.langOnly.order.after.sale.tips.waiting_sendback";
    /**
     * 优惠券待回收
     */
    public static final String APP_ORDER_AFTER_SALE_TIPS_COUPON_WAITING_SENDBACK = "lang.card.platform.app.langOnly.order.after.sale.tips.coupon_waiting_sendback";
    /**
     * 商品退回中
     */
    public static final String APP_ORDER_AFTER_SALE_TIPS_GIFT_BACKING = "lang.card.platform.app.langOnly.order.after.sale.tips.gift_backing";
    /**
     * 正在退款中，请耐心等待!
     */
    public static final String APP_ORDER_AFTER_SALE_TIPS_REFUNDING = "lang.card.platform.app.langOnly.order.after.sale.tips.refunding";
    /**
     * 取消退货
     */
    public static final String APP_ORDER_AFTER_SALE_TIPS_CANCELED = "lang.card.platform.app.langOnly.order.after.sale.tips.canceled";
    /**
     * 已原路退回金额 {0}
     */
    public static final String APP_ORDER_AFTER_SALE_TIPS_FINISHED = "lang.card.platform.app.langOnly.order.after.sale.tips.finished";


    // excel key
    // after sale
    /**
     * 售后单记录导出
     */
    public static final String EXCEl_AFTER_SALE_ORDER_DESCRIPTION = "lang.card.platform.excel.after.sale.order.export.description";
    /**
     * 售后单列表
     */
    public static final String EXCEl_AFTER_SALE_ORDER_FILENAME = "lang.card.platform.excel.after.sale.order.export.fileName";
    /**
     * 售后单
     */
    public static final String EXCEL_AFTER_SALE_ORDER_USERLIST = "lang.card.platform.excel.after.sale.order.export.sheet0.userList";
    /**
     * 售后单号
     */
    public static final String EXCEL_AFTER_SALE_ORDER_ORDERNO = "lang.card.platform.excel.afterSale.order.export.row.orderNo";
    /**
     * 关联销售单号
     */
    public static final String EXCEL_AFTER_SALE_ORDER_SALEORDERNO = "lang.card.platform.excel.afterSale.order.export.row.saleOrderNo";
    /**
     * 售后状态
     */
    public static final String EXCEL_AFTER_SALE_ORDER_ORDERSTATUS = "lang.card.platform.excel.afterSale.order.export.row.orderStatus";
    /**
     * 店铺编码
     */
    public static final String EXCEL_AFTER_SALE_ORDER_STORECODE = "lang.card.platform.excel.afterSale.order.export.row.storeCode";
    /**
     * 所属商户
     */
    public static final String EXCEL_AFTER_SALE_ORDER_STORENAME = "lang.card.platform.excel.afterSale.order.export.row.storeName";
    /**
     * 商品退回方式
     */
    public static final String EXCEL_AFTER_SALE_ORDER_RETURNWAY = "lang.card.platform.excel.afterSale.order.export.row.returnWay";
    /**
     * 售后原因
     */
    public static final String EXCEL_AFTER_SALE_ORDER_REASONCODE = "lang.card.platform.excel.afterSale.order.export.row.reasonCode";
    /**
     * 问题描述
     */
    public static final String EXCEL_AFTER_SALE_ORDER_DESCINFO = "lang.card.platform.excel.afterSale.order.export.row.descInfo";
    /**
     * 会员ID
     */
    public static final String EXCEL_AFTER_SALE_ORDER_MEMBERID = "lang.card.platform.excel.afterSale.order.export.row.memberId";
    /**
     * 会员手机号
     */
    public static final String EXCEL_AFTER_SALE_ORDER_MEMBERPHONE = "lang.card.platform.excel.afterSale.order.export.row.memberPhone";
    /**
     * 申请时间
     */
    public static final String EXCEL_AFTER_SALE_ORDER_ORDERTIME = "lang.card.platform.excel.afterSale.order.export.row.orderTime";
    /**
     * 审核时间
     */
    public static final String EXCEL_AFTER_SALE_ORDER_AUDITTIME = "lang.card.platform.excel.afterSale.order.export.row.auditTime";
    /**
     * 退货总金额
     */
    public static final String EXCEL_AFTER_SALE_ORDER_TOTALREFUNDAMOUNT = "lang.card.platform.excel.afterSale.order.export.row.totalRefundAmount";
    /**
     * 礼品编码
     */
    public static final String EXCEL_AFTER_SALE_ORDER_ITEMCODE = "lang.card.platform.excel.afterSale.order.export.row.itemCode";
    /**
     * 礼品名称
     */
    public static final String EXCEL_AFTER_SALE_ORDER_SKUNAME = "lang.card.platform.excel.afterSale.order.export.row.skuName";
    /**
     * 礼品数量
     */
    public static final String EXCEL_AFTER_SALE_ORDER_SALEQTY = "lang.card.platform.excel.afterSale.order.export.row.saleQty";
    /**
     * 礼品现金支付
     */
    public static final String EXCEL_AFTER_SALE_ORDER_SALEPRICE = "lang.card.platform.excel.afterSale.order.export.row.salePrice";
    /**
     * 礼品积分支付
     */
    public static final String EXCEL_AFTER_SALE_ORDER_SALEPRICEPOINT = "lang.card.platform.excel.afterSale.order.export.row.salePricePoint";


    // sale
    /**
     * 销售单记录导出
     */
    public static final String EXCEL_SALE_ORDER_DESCRIPTION = "lang.card.platform.excel.sale.order.export.description";
    /**
     * 销售单列表
     */
    public static final String EXCEL_SALE_ORDER_FILENAME = "lang.card.platform.excel.sale.order.export.fileName";
    /**
     * 销售订单
     */
    public static final String EXCEL_SALE_ORDER_ORDERS = "lang.card.platform.excel.sale.order.export.sheet0.orders";
    /**
     * 销售单号
     */
    public static final String EXCEL_SALE_ORDER_ORDERNO = "lang.card.platform.excel.sale.order.export.row.orderNo";
    /**
     * 订单状态
     */
    public static final String EXCEL_SALE_ORDER_ORDERSTATUSDESC = "lang.card.platform.excel.sale.order.export.row.orderStatusDesc";
    /**
     * 所属商户
     */
    public static final String EXCEL_SALE_ORDER_STORENAME = "lang.card.platform.excel.sale.order.export.row.storeName";
    /**
     * 配送方式
     */
    public static final String EXCEL_SALE_ORDER_DELIVERYTYPEDESC = "lang.card.platform.excel.sale.order.export.row.deliveryTypeDesc";
    /**
     * 下单时间
     */
    public static final String EXCEL_SALE_ORDER_ORDERTIME = "lang.card.platform.excel.sale.order.export.row.orderTime";
    /**
     * 支付时间
     */
    public static final String EXCEL_SALE_ORDER_PAYTIME = "lang.card.platform.excel.sale.order.export.row.payTime";
    /**
     * 订单总金额
     */
    public static final String EXCEL_SALE_ORDER_REALPAYAMOUNT = "lang.card.platform.excel.sale.order.export.row.realPayAmount";

   /**
     * 会员ID
     */
    public static final String EXCEL_SALE_ORDER_MEMBERID = "lang.card.platform.excel.sale.order.export.row.memberId";
    /**
     * 会员手机号
     */
    public static final String EXCEL_SALE_ORDER_MEMBERPHONE = "lang.card.platform.excel.sale.order.export.row.memberPhone";
    /**
     * 礼品编码
     */
    public static final String EXCEL_SALE_ORDER_ITEMCODE = "lang.card.platform.excel.sale.order.export.row.itemCode";
    /**
     * 礼品数量
     */
    public static final String EXCEL_SALE_ORDER_SALEQTY = "lang.card.platform.excel.sale.order.export.row.saleQty";
    /**
     * 礼品名称
     */
    public static final String EXCEL_SALE_ORDER_SKUNAME = "lang.card.platform.excel.sale.order.export.row.skuName";
    /**
     * 礼品金额
     */
    public static final String EXCEL_SALE_ORDER_SALEPRICE= "lang.card.platform.excel.sale.order.export.row.salePrice";



    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        String property = System.getProperty("user.dir");
        String path = property+"\\tester-common\\src\\main\\java\\com\\tester\\testercommon\\constant\\CardLanguageKeyConstant.java";
        java.io.File f =new java.io.File(path);
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String temp = null;
            int line = 1;
            while ((temp = reader.readLine()) != null) {
                if (temp.contains("public static void main")) {
                    break;
                }
                sb.append(temp).append("\r\n");
                line++;
            }
        }
        int num = 0;
        char[] chars = sb.toString().toCharArray();
        for (char aChar : chars) {
            if('='==aChar){
                num ++;
            }
        }
        String msg = "key 数量："+num+"\r\n";
        String[] split = sb.toString().split("/\\*\\*");
        List<String> chineseList = new ArrayList<>();
        List<String> keyList = new ArrayList<>();
        for (String s : split) {
            if(!s.contains("=")){
                continue;
            }
            int splitIndex = s.indexOf("*/");
            int keyStart = s.indexOf("public");
            int keyEnd = s.lastIndexOf(";");
            if(splitIndex<0){
                continue;
            }
            if(keyStart < 0){
                continue;
            }

            String chineseLine = s.substring(0, splitIndex);
            String replace = chineseLine.replace("* ", "");
            chineseList.add(replace.trim().replace("*","").replace("\r\n","").trim());
            String keyLine = s.substring(keyStart,keyEnd);
            keyList.add(keyLine);
        }
        msg+="chineseList.size()："+chineseList.size()+"\r\n";
        msg+="keyList.size()："+keyList.size()+"\r\n";
        if(keyList.size() == chineseList.size() && keyList.size() == num){
            msg+="数量一致，可用\r\n";
        }else{

            msg+="异常！数量不一致，不可用\r\n";
        }
        System.out.println("\r\n\r\n\r\n");
        for (int i = 0; i < chineseList.size(); i++) {
            String key = keyList.get(i);
            String apolloKey = key.substring(key.indexOf("\"")+1, key.lastIndexOf("\""));
            String chinese = chineseList.get(i);
            System.out.println(apolloKey+" = "+chinese);
        }
        System.out.println("\r\n\r\n\r\n"+msg);
    }

}
