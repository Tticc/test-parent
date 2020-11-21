package com.tester.testerwebapp;

import org.junit.Test;

import java.util.*;

public class KeyComparedTest {

    @Test
    public void test_lanKey(){
        String oldStr = getOldKeyStr();
        Map<String, String> oldKeyMap = get_keyMap(oldStr);
        System.out.println("oldKeyMap size:"+oldKeyMap.size());
        String newStr = getNewKeyStr();
        Map<String, String> newKeyMap = get_keyMap(newStr);
        System.out.println("newKeyMap size:"+newKeyMap.size());
        Set<String> strings = oldKeyMap.keySet();
        for (String string : strings) {
            newKeyMap.remove(string);
        }
        System.out.println(newKeyMap.size());
        System.out.println("\r\n\n\n");
        Set<String> strings1 = newKeyMap.keySet();
        for (String s : strings1) {
            System.out.println(s+" = "+newKeyMap.get(s));
        }
    }

    public Map<String, String> get_keyMap(String newStr){

        List<String> keyList = new ArrayList<String>(Arrays.asList(newStr.split("\n")));
        Map<String, String> map = new HashMap<>();
        for (String s : keyList) {
            String[] split = s.split("=");
            if(split.length!=2){
                System.err.println("something went wrong."+s);
                return null;
            }
            map.put(split[0].trim(),split[1].trim());
        }
        return map;
    }

    private String getNewKeyStr() {
        return "lang.card.platform.common.dbError = 数据库异常\n" +
                "lang.card.platform.common.dataError = 数据异常\n" +
                "lang.card.platform.common.responseSignVerifyFailed = 返回数据验签失败\n" +
                "lang.card.platform.common.tryLockFailed = 分布式锁获取失败\n" +
                "lang.card.platform.common.noPermission = 没有权限\n" +
                "lang.card.platform.member.getMemberIdFailed = 获取会员id失败\n" +
                "lang.card.platform.member.noLogin = 没有登录\n" +
                "lang.card.platform.member.getStoreFailed = 获取店铺信息失败\n" +
                "lang.card.platform.member.memberInfoError = 会员信息异常\n" +
                "lang.card.platform.point.pointOpeFailed = 积分操作失败\n" +
                "lang.card.platform.point.pointUseFailed = 积分利用失败\n" +
                "lang.card.platform.order.orderNotExists = 订单不存在\n" +
                "lang.card.platform.order.payCallBackError = 支付回调失败\n" +
                "lang.card.platform.order.payCodeIsNull = 支付信息为空\n" +
                "lang.card.platform.order.diffInPayAndOrderAmount = 订单金额与支付金额不一致\n" +
                "lang.card.platform.order.canceledBeforePay = 未支付取消\n" +
                "lang.card.platform.order.orderFetchFailed = 订单信息获取失败\n" +
                "lang.card.platform.order.invalidOrderNo = 无效的订单号\n" +
                "lang.card.platform.order.emptyGoodsList = 商品详细信息不能为空\n" +
                "lang.card.platform.order.failedOrderShouldNotSave = 非成功订单不可保存\n" +
                "lang.card.platform.order.orderPayTypeError = 订单支付类型错误\n" +
                "lang.card.platform.order.giftTypeError = 礼品类型错误\n" +
                "lang.card.platform.order.consumePointFailed = 积分使用失败。{0}\n" +
                "lang.card.platform.order.orderCouldNotCancel = 当前订单无法取消\n" +
                "lang.card.platform.order.opeStatusError = 订单状态已变更，操作失败\n" +
                "lang.card.platform.order.orderStatusError = 订单状态异常\n" +
                "lang.card.platform.order.salesOrderNotFinish = 销售订单未完成\n" +
                "lang.card.platform.order.orderQueryParamError = 订单查询参数错误\n" +
                "lang.card.platform.order.afterSaleOrderCouldNotCancel = 当前售后订单不可取消\n" +
                "lang.card.platform.order.couponDeliveryFailed = 券发放失败。{0}\n" +
                "lang.card.platform.order.seckillItemError = 礼品优惠活动数据获取失败\n" +
                "lang.card.platform.order.orderQueryFailed = 订单查询失败\n" +
                "lang.card.platform.order.getDetailTraceFailed = 订单追踪信息查询失败\n" +
                "lang.card.platform.order.orderUnReturnable = 当前订单不支持退货\n" +
                "lang.card.platform.order.orderHaveValidAfterSaleRecord = 当前订单已存在有效售后单\n" +
                "lang.card.platform.order.orderVerifyFailed = 自提订单核销失败。{0}\n" +
                "lang.card.platform.order.couponReturnFailed = 券退回失败。{0}\n" +
                "lang.card.platform.param.empty = 参数为空\n" +
                "lang.card.platform.pre.pay.fail = 礼品卡预支付失败\n" +
                "lang.card.platform.pay.query.fail = 礼品卡支付查询失败\n" +
                "lang.card.platform.balance.query.fail = 礼品卡余额查询失败\n" +
                "lang.card.platform.prepay.id.not.empty = 礼品卡预支付ID不能为空\n" +
                "lang.card.platform.pay.query.not.empty = 礼品卡支付查询结果不能为空\n" +
                "lang.card.platform.refund.fail = 礼品卡退货接口调用失败\n" +
                "lang.card.platform.refund.not.empty = 礼品卡支付查询结果不能为空\n" +
                "lang.card.platform.external.interface.call.exception = 外部接口调用异常\n" +
                "lang.card.platform.external.interface_parse.exception = 外部接口解析异常\n" +
                "lang.card.platform.empty.request.method.name = 调用外部接口方法名为空\n" +
                "lang.card.platform.valid.fail = 签名验证失败\n" +
                "lang.card.platform.pan.and.pin.pay.exception = 礼品卡卡号魔密支付接口调用异常\n" +
                "lang.card.platform.pan.and.pin.pay.fail = 礼品卡卡号魔密支付失败\n" +
                "lang.card.platform.refund.query.exception = 礼品卡退货接口异常\n" +
                "lang.card.platform.refund.query.fail = 礼品卡退货接口调用失败\n" +
                "lang.card.platform.pcode.pay.exception = 礼品卡卡码支付接口调用异常\n" +
                "lang.card.platform.pcode.pay.fail = 礼品卡卡码支付失败\n" +
                "lang.card.platform.pan.and.pin.refund.exception = 礼品卡卡号魔密红冲接口调用异常\n" +
                "lang.card.platform.pan.and.pin.refund.fail = 礼品卡卡号魔密红冲失败\n" +
                "lang.card.platform.pan.and.pin.query.exception = 礼品卡卡号魔密查询接口调用异常\n" +
                "lang.card.platform.pan.and.pin.query.fail = 礼品卡卡号魔密查询失败\n" +
                "lang.card.platform.pan.and.pin.rd.exception = 礼品卡卡号魔密退货接口调用异常\n" +
                "lang.card.platform.pan.and.pin.rd.fail = 礼品卡卡号魔密退货失败\n" +
                "lang.card.platform.payment.payQueryErr = 购卡支付查询失败\n" +
                "lang.card.platform.payment.payQuerySuccessOrderFail = 查询成功，通知订单失败\n" +
                "lang.card.platform.payment.cardPayTypeError = 购卡支付方式错误\n" +
                "lang.card.platform.payment.orderNotPaidCouldNotRefund = 订单未支付，不可冲正\n" +
                "lang.card.platform.payment.noPaymentRecord = 未找到支付记录\n" +
                "lang.card.platform.payment.orderCouldNotPay = 当前订单状态不可支付\n" +
                "lang.card.platform.payment.orderPaying = 订单支付中\n" +
                "lang.card.platform.payment.payResultQueryFailed = 支付结果查询失败\n" +
                "lang.card.platform.invoice.invoiceStatusQueryFiled = 开票状态查询失败\n" +
                "lang.card.platform.invoice.storeInvoiceStatusQueryFiled = 门店可开票状态查询失败\n" +
                "lang.card.platform.invoice.orderCouldNotOpenInvoice = 当前订单不可开发票\n" +
                "lang.card.platform.invoice.orderStatusNotSupportInvoice = 订单当前状态不可开票\n" +
                "lang.card.platform.invoice.openInvoiceError = 开票失败\n" +
                "lang.card.platform.invoice.invoiceHeadNull = 发票抬头为空\n" +
                "lang.card.platform.invoice.orderItemEmpty = 订单商品记录为空\n" +
                "lang.card.platform.invoice.orderPaymentEmpty = 订单支付记录为空\n" +
                "lang.card.platform.orderGift.giftItemUnmarketable = {0}礼品已经下架咯,请重新下单！\n" +
                "lang.card.platform.orderGift.giftStockOut = {0}礼品已经抢完咯,请重新下单！\n" +
                "lang.card.platform.orderGift.giftStockNotEnough = {0}礼品购买库存不足,请重新下单！\n" +
                "lang.card.platform.orderGift.giftNotExist = 礼品不存在\n" +
                "lang.card.platform.orderGift.giftInfoError = 礼品数据异常\n" +
                "lang.card.platform.orderGift.seckillGiftInvalid = 秒杀礼品已失效\n" +
                "lang.card.platform.orderGift.giftInvalid = 礼品已失效\n" +
                "lang.card.platform.orderGift.giftStockOpeFailed = 礼品库存操作失败\n" +
                "lang.card.platform.langOnly.order.customer = 顾客\n" +
                "lang.card.platform.langOnly.order.system = 系统\n" +
                "lang.card.platform.langOnly.sale.order.trace.create = 创建订单\n" +
                "lang.card.platform.langOnly.sale.order.trace.point_paid = 已支付积分\n" +
                "lang.card.platform.langOnly.sale.order.trace.pay_to_audit = 买家支付\n" +
                "lang.card.platform.langOnly.sale.order.trace.audit_to_pickup = 待审核\n" +
                "lang.card.platform.langOnly.sale.order.trace.pickup_to_delivery = 待备货\n" +
                "lang.card.platform.langOnly.sale.order.trace.delivery_to_receipt = 待发货\n" +
                "lang.card.platform.langOnly.sale.order.trace.waiting_receipt = 待签收\n" +
                "lang.card.platform.langOnly.sale.order.trace.finished = 已完成\n" +
                "lang.card.platform.langOnly.sale.order.trace.canceled_before_pay = 未支付取消订单\n" +
                "lang.card.platform.langOnly.sale.order.trace.canceled_before_audit = 审核前取消\n" +
                "lang.card.platform.langOnly.sale.order.trace.canceled_when_audit = 审核取消\n" +
                "lang.card.platform.langOnly.sale.order.trace.cancel_auditing = 取消审核中\n" +
                "lang.card.platform.langOnly.sale.order.trace.apply_cancel = 取消申请审核中\n" +
                "lang.card.platform.langOnly.sale.order.trace.agree_cancel_when_audit = 订单已取消\n" +
                "lang.card.platform.langOnly.sale.order.trace.reject_cancel = 订单处理中，取消失败\n" +
                "lang.card.platform.langOnly.sale.order.event.create = 创建订单，待支付\n" +
                "lang.card.platform.langOnly.sale.order.event.point_paid = 买家积分付款，待金额付款\n" +
                "lang.card.platform.langOnly.sale.order.event.pay_to_audit = 买家付款，待审核\n" +
                "lang.card.platform.langOnly.sale.order.event.audit_to_pickup = 审核通过，待备货\n" +
                "lang.card.platform.langOnly.sale.order.event.pickup_to_delivery = 备货完成，待发货\n" +
                "lang.card.platform.langOnly.sale.order.event.delivery_to_receipt = 发货完成，待签收\n" +
                "lang.card.platform.langOnly.sale.order.event.waiting_receipt = 待签收\n" +
                "lang.card.platform.langOnly.sale.order.event.finished = 已完成\n" +
                "lang.card.platform.langOnly.sale.order.event.canceled_before_pay = 未支付取消订单\n" +
                "lang.card.platform.langOnly.sale.order.event.canceled_before_audit = 审核前取消\n" +
                "lang.card.platform.langOnly.sale.order.event.canceled_when_audit = 审核取消\n" +
                "lang.card.platform.langOnly.sale.order.event.cancel_auditing = 取消审核中\n" +
                "lang.card.platform.langOnly.sale.order.event.apply_cancel = 取消申请审核中\n" +
                "lang.card.platform.langOnly.sale.order.event.agree_cancel_when_audit = 订单已取消\n" +
                "lang.card.platform.langOnly.sale.order.event.reject_cancel = 订单处理中，取消失败\n" +
                "lang.card.platform.langOnly.afterSale.order.trace.creating = 创建中\n" +
                "lang.card.platform.langOnly.afterSale.order.trace.waiting_audit = 申请售后\n" +
                "lang.card.platform.langOnly.afterSale.order.trace.waiting_send_back = 审核通过\n" +
                "lang.card.platform.langOnly.afterSale.order.trace.waiting_storage = 已寄回\n" +
                "lang.card.platform.langOnly.afterSale.order.trace.waiting_refund = 待入库\n" +
                "lang.card.platform.langOnly.afterSale.order.trace.refunding = 退款中\n" +
                "lang.card.platform.langOnly.afterSale.order.trace.finished = 退款完成\n" +
                "lang.card.platform.langOnly.afterSale.order.trace.canceled = 已取消\n" +
                "lang.card.platform.langOnly.afterSale.order.event.creating = 创建订单\n" +
                "lang.card.platform.langOnly.afterSale.order.event.waiting_audit = 待审核\n" +
                "lang.card.platform.langOnly.afterSale.order.event.waiting_send_back = 待寄回\n" +
                "lang.card.platform.langOnly.afterSale.order.event.waiting_storage = 待入库\n" +
                "lang.card.platform.langOnly.afterSale.order.event.waiting_refund = 待退款\n" +
                "lang.card.platform.langOnly.afterSale.order.event.refunding = 退款中\n" +
                "lang.card.platform.langOnly.afterSale.order.event.finished = 已完成\n" +
                "lang.card.platform.langOnly.afterSale.order.event.canceled = 已取消\n" +
                "lang.card.platform.app.langOnly.order.sale.confirm.store.order.tips = 确认订单\n" +
                "lang.card.platform.app.langOnly.order.orderDeletedForMember = 订单删除成功\n" +
                "lang.card.platform.app.langOnly.order.afterSale.orderReturnSuccessTitle = 退货申请成功\n" +
                "lang.card.platform.excel.after.sale.order.export.description = 售后单记录导出\n" +
                "lang.card.platform.excel.after.sale.order.export.fileName = 售后单列表\n" +
                "lang.card.platform.excel.after.sale.order.export.sheet0.userList = 售后单\n" +
                "lang.card.platform.excel.afterSale.order.export.row.orderNo = 售后单号\n" +
                "lang.card.platform.excel.afterSale.order.export.row.saleOrderNo = 关联销售单号\n" +
                "lang.card.platform.excel.afterSale.order.export.row.orderStatus = 售后状态\n" +
                "lang.card.platform.excel.afterSale.order.export.row.storeCode = 店铺编码\n" +
                "lang.card.platform.excel.afterSale.order.export.row.storeName = 所属商户\n" +
                "lang.card.platform.excel.afterSale.order.export.row.returnWay = 商品退回方式\n" +
                "lang.card.platform.excel.afterSale.order.export.row.reasonCode = 售后原因\n" +
                "lang.card.platform.excel.afterSale.order.export.row.descInfo = 问题描述\n" +
                "lang.card.platform.excel.afterSale.order.export.row.memberId = 会员ID\n" +
                "lang.card.platform.excel.afterSale.order.export.row.memberPhone = 会员手机号\n" +
                "lang.card.platform.excel.afterSale.order.export.row.orderTime = 申请时间\n" +
                "lang.card.platform.excel.afterSale.order.export.row.auditTime = 审核时间\n" +
                "lang.card.platform.excel.afterSale.order.export.row.totalRefundAmount = 退货总金额\n" +
                "lang.card.platform.excel.afterSale.order.export.row.itemCode = 礼品编码\n" +
                "lang.card.platform.excel.afterSale.order.export.row.skuName = 礼品名称\n" +
                "lang.card.platform.excel.afterSale.order.export.row.saleQty = 礼品数量\n" +
                "lang.card.platform.excel.afterSale.order.export.row.salePrice = 礼品现金支付\n" +
                "lang.card.platform.excel.afterSale.order.export.row.salePricePoint = 礼品积分支付\n" +
                "lang.card.platform.excel.sale.order.export.description = 销售单记录导出\n" +
                "lang.card.platform.excel.sale.order.export.fileName = 销售单列表\n" +
                "lang.card.platform.excel.sale.order.export.sheet0.orders = 销售订单\n" +
                "lang.card.platform.excel.sale.order.export.row.orderNo = 销售单号\n" +
                "lang.card.platform.excel.sale.order.export.row.orderStatusDesc = 订单状态\n" +
                "lang.card.platform.excel.sale.order.export.row.storeName = 所属商户\n" +
                "lang.card.platform.excel.sale.order.export.row.deliveryTypeDesc = 配送方式\n" +
                "lang.card.platform.excel.sale.order.export.row.orderTime = 下单时间\n" +
                "lang.card.platform.excel.sale.order.export.row.payTime = 支付时间\n" +
                "lang.card.platform.excel.sale.order.export.row.realPayAmount = 订单总金额\n" +
                "lang.card.platform.excel.sale.order.export.row.memberId = 会员ID\n" +
                "lang.card.platform.excel.sale.order.export.row.memberPhone = 会员手机号\n" +
                "lang.card.platform.excel.sale.order.export.row.itemCode = 礼品编码\n" +
                "lang.card.platform.excel.sale.order.export.row.saleQty = 礼品数量\n" +
                "lang.card.platform.excel.sale.order.export.row.skuName = 礼品名称\n" +
                "lang.card.platform.excel.sale.order.export.row.salePrice = 礼品金额";
    }

    private String getOldKeyStr() {
        return "lang.card.platform.common.dbError = 数据库异常\n" +
                "lang.card.platform.common.responseSignVerifyFailed = 返回数据验签失败\n" +
                "lang.card.platform.member.getMemberIdFailed = 获取会员id失败\n" +
                "lang.card.platform.member.noLogin = 没有登录\n" +
                "lang.card.platform.member.getStoreFailed = 获取店铺信息失败\n" +
                "lang.card.platform.member.memberInfoError = 会员信息异常\n" +
                "lang.card.platform.point.pointOpeFailed = 积分操作失败\n" +
                "lang.card.platform.point.pointUseFailed = 积分利用失败\n" +
                "lang.card.platform.order.orderNotExists = 订单不存在\n" +
                "lang.card.platform.order.payCallBackError = 支付回调失败\n" +
                "lang.card.platform.order.payCodeIsNull = 支付信息为空\n" +
                "lang.card.platform.order.diffInPayAndOrderAmount = 订单金额与支付金额不一致\n" +
                "lang.card.platform.order.canceledBeforePay = 未支付取消\n" +
                "lang.card.platform.order.orderFetchFailed = 订单信息获取失败\n" +
                "lang.card.platform.order.invalidOrderNo = 无效的订单号\n" +
                "lang.card.platform.order.emptyGoodsList = 商品详细信息不能为空\n" +
                "lang.card.platform.order.failedOrderShouldNotSave = 非成功订单不可保存\n" +
                "lang.card.platform.order.orderPayTypeError = 订单支付类型错误\n" +
                "lang.card.platform.param.empty = 参数不能为空\n" +
                "lang.card.platform.pre.pay.fail = 礼品卡预支付失败\n" +
                "lang.card.platform.pay.query.fail = 礼品卡支付查询失败\n" +
                "lang.card.platform.balance.query.fail = 礼品卡余额查询失败\n" +
                "lang.card.platform.prepay.id.not.empty = 礼品卡预支付ID不能为空\n" +
                "lang.card.platform.pay.query.not.empty = 礼品卡支付查询结果不能为空\n" +
                "lang.card.platform.refund.fail = 礼品卡退货接口调用失败\n" +
                "lang.card.platform.refund.not.empty = 礼品卡支付查询结果不能为空\n" +
                "lang.card.platform.external.interface.call.exception = 外部接口调用异常\n" +
                "lang.card.platform.external.interface_parse.exception = 外部接口解析异常\n" +
                "lang.card.platform.empty.request.method.name = 调用外部接口方法名为空\n" +
                "lang.card.platform.valid.fail = 签名验证失败\n" +
                "lang.card.platform.pan.and.pin.pay.exception = 礼品卡卡号魔密支付接口调用异常\n" +
                "lang.card.platform.pan.and.pin.pay.fail = 礼品卡卡号魔密支付失败\n" +
                "lang.card.platform.refund.query.exception = 礼品卡退货接口异常\n" +
                "lang.card.platform.refund.query.fail = 礼品卡退货接口调用失败\n" +
                "lang.card.platform.pcode.pay.exception = 礼品卡卡码支付接口调用异常\n" +
                "lang.card.platform.pcode.pay.fail = 礼品卡卡码支付失败\n" +
                "lang.card.platform.pan.and.pin.refund.exception = 礼品卡卡号魔密红冲接口调用异常\n" +
                "lang.card.platform.pan.and.pin.refund.fail = 礼品卡卡号魔密红冲失败\n" +
                "lang.card.platform.pan.and.pin.query.exception = 礼品卡卡号魔密查询接口调用异常\n" +
                "lang.card.platform.pan.and.pin.query.fail = 礼品卡卡号魔密查询失败\n" +
                "lang.card.platform.pan.and.pin.rd.exception = 礼品卡卡号魔密退货接口调用异常\n" +
                "lang.card.platform.pan.and.pin.rd.fail = 礼品卡卡号魔密退货失败\n" +
                "lang.card.platform.payment.payQueryErr = 购卡支付查询失败\n" +
                "lang.card.platform.payment.payQuerySuccessOrderFail = 查询成功，通知订单失败\n" +
                "lang.card.platform.payment.cardPayTypeError = 购卡支付方式错误\n" +
                "lang.card.platform.payment.orderNotPaidCouldNotRefund = 订单未支付，不可冲正\n" +
                "lang.card.platform.payment.noPaymentRecord = 未找到支付记录\n" +
                "lang.card.platform.payment.orderCouldNotPay = 当前订单状态不可支付\n" +
                "lang.card.platform.invoice.invoiceStatusQueryFiled = 开票状态查询失败\n" +
                "lang.card.platform.invoice.storeInvoiceStatusQueryFiled = 门店可开票状态查询失败\n" +
                "lang.card.platform.invoice.orderCouldNotOpenInvoice = 当前订单不可开发票\n" +
                "lang.card.platform.invoice.orderStatusNotSupportInvoice = 订单当前状态不可开票\n" +
                "lang.card.platform.invoice.openInvoiceError = 开票失败\n" +
                "lang.card.platform.invoice.invoiceHeadNull = 发票抬头信息为空\n" +
                "lang.card.platform.invoice.orderItemEmpty = 订单商品记录为空\n" +
                "lang.card.platform.invoice.orderPaymentEmpty = 订单支付记录为空";
    }
}
