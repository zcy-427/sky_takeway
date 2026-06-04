package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单条件查询接口
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单条件查询接口")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单条件查询接口：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 订单统计接口
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("订单统计接口")
    public Result<OrderStatisticsVO> orderStatistics() {
        log.info("订单统计接口");
        OrderStatisticsVO orderStatisticsVO = orderService.orderStatistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 订单详情接口
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("订单详情接口")
    public Result<OrderVO> details(@PathVariable Long id) {
        log.info("订单详情接口：{}", id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 订单确认接口
     *
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("订单确认接口")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("订单确认接口：{}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 订单拒绝接口
     * @param ordersRejectionDTO
     * @return
     * @throws Exception
     */
    @PutMapping("/rejection")
    @ApiOperation("订单拒绝接口")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("订单拒绝接口：{}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 订单取消接口
     * @param ordersCancelDTO
     * @return
     * @throws Exception
     */
    @PutMapping("/cancel")
    @ApiOperation("订单取消接口")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("订单取消接口：{}", ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 订单派送接口
     * @param id
     * @return
     */
    @PutMapping("delivery/{id}")
    @ApiOperation("订单派送接口")
    public Result deliver(@PathVariable Long id) {
        log.info("订单派送接口：id={}", id);
        orderService.deliver(id);
        return Result.success();
    }

    /**
     * 订单完成接口
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("订单完成接口")
    public Result complete(@PathVariable Long id) {
        log.info("订单完成接口：id={}", id);
        orderService.complete(id);
        return Result.success();
    }
}
