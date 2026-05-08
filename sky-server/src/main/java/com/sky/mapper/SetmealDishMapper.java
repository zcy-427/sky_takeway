package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品ID查询所有关联的套餐ID
     * @param DishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> DishIds);

    /**
     * 批量新增套餐与菜品的关联关系
     * @param
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
