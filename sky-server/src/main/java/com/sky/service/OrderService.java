package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单接口
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 查询历史订单接口
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQueryHistoryOrders(int pageNum, int pageSize, Integer status);

    /**
     * 查询订单详情接口
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 取消订单接口
     * @param id
     */
    void cancelById(Long id) throws Exception;

    /**
     * 再来一单接口
     * @param id
     */
    void repetition(Long id);

    /**
     * 条件查询订单接口
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单统计接口
     * @return
     */
    OrderStatisticsVO orderStatistics();

    /**
     * 订单确认接口
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 订单拒绝接口
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 订单取消接口
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 订单派送接口
     * @param id
     */
    void deliver(Long id);

    /**
     * 订单完成接口
     * @param id
     */
    void complete(Long id);
}
