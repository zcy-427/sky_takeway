package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    /**
     * 新增菜品与口味信息
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
