package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理相关接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品接口
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品接口")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        //新增菜品后需要清除redis中菜品数据，以便下次查询时能够获取到最新数据
        String key="dish_"+dishDTO.getCategoryId();
        clearCache(key);
        return Result.success();
    }

    /**
     * 菜品分页查询接口
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品接口")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品接口
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除菜品接口")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除菜品：ids={}", ids);
        dishService.deleteBatch(ids);

        //todo:感觉这里没有必要,因为只能够删除起售中的菜品,所以可以放在起售停售接口中,当起售停售时直接清除redis中菜品数据,以便下次查询时能够获取到最新数据
        //删除菜品后直接清除redis中所有菜品数据，以便下次查询时能够获取到最新数据
        clearCache("dish_*");

        return Result.success();
    }

    /**
     * 根据id查询菜品信息接口
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品信息接口")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品信息：id={}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息接口
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品信息接口")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        //修改菜品后需要清除redis中菜品数据，以便下次查询时能够获取到最新数据
        clearCache("dish_*");

        return Result.success();
    }
    /**
     * 根据分类id查询菜品接口
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        log.info("根据分类id查询菜品：categoryId={}", categoryId);
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用菜品接口")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用或禁用菜品：{},{}", status, id);
        dishService.startOrStop(status, id);
        //启用或禁用菜品后需要清除redis中菜品数据，以便下次查询时能够获取到最新数据
        clearCache("dish_*");

        return Result.success();
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void clearCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);

    }
}
