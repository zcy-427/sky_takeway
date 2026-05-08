package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     *
     * @param setmealDTO
     */
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal =new Setmeal();

        //拷贝对象属性
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //保存数据到数据库
        setmealMapper.insert(setmeal);

        //从insert当中获取到套餐id
        Long id = setmeal.getId();

        //保存套餐和菜品的关联关系到数据库
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(id);
        });

        //批量插入数据到数据库
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        //开启分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        //执行分页查询
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        //封装分页查询结果并返回
        return new PageResult(page.getTotal(), page.getResult());
    }
}
