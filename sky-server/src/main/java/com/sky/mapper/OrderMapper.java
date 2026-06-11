package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Select("select * from sky_take_out.orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndorderTime(Integer status, LocalDateTime orderTime);

    /**
     * 分页查询订单数据
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单数据
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 根据订单状态查询订单数量
     * @param status
     * @return
     */
    @Select("select count(id) from sky_take_out.orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 根据订单状态和时间范围动态查询订单总金额
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 根据订单状态和时间范围动态查询订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 根据时间范围查询销量前十的商品数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<GoodsSalesDTO> getSaleTop10(LocalDateTime beginTime, LocalDateTime endTime);
}
