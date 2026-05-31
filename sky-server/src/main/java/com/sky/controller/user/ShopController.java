package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "用户端店铺管理相关接口")
public class ShopController {

    public static final String KEY="SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询店铺状态接口
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查询店铺状态接口")
    public Result<Integer> getStatus() {
        log.info("查询店铺状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("店铺状态：{}", status==1?"营业中":"打烊中");
        return Result.success(status);
    }
}
