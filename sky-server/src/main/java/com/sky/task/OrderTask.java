package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单的定时任务
     */
    @Scheduled(cron = "0 * * * * ?")//每分钟执行一次
//    @Scheduled(cron = "1/5 * * * * ?")
    public void processTimeoutOrders() {
        log.info("开始处理超时订单");
        LocalDateTime time=LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getByStatusAndorderTime(Orders.PENDING_PAYMENT, time);

        if(ordersList !=null &&ordersList.size()>0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直派送中的订单的定时任务
     */
    @Scheduled(cron = "0 0 1 * * ?")//每小时执行一次
//    @Scheduled(cron = "0/5 * * * * ?")//每分钟执行一次
    public void processDeliveryOrders() {
        log.info("开始处理派送中的订单");
        LocalDateTime time=LocalDateTime.now().plusHours(-1);
        List<Orders> ordersList = orderMapper.getByStatusAndorderTime(Orders.DELIVERY_IN_PROGRESS, time);

        if(ordersList !=null && ordersList.size()>0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
