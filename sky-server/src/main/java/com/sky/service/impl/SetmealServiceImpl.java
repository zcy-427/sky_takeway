package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     *
     * @param setmealDTO
     */
    @Transactional
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

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断当前套餐是否为起售状态
        ids.forEach(id->{
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus()== StatusConstant.ENABLE){
                //当前套餐为起售状态，不能删除
                throw new RuntimeException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        //删除套餐
        setmealMapper.deleteByIds(ids);
        //删除套餐和菜品的关联关系
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 根据id查询套餐信息和关联的菜品信息
     *
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        //查询套餐基本信息
        Setmeal setmeal = setmealMapper.getById(id);

        //查询套餐关联的菜品信息
        List<SetmealDish> dishes = setmealMapper.getBySetmealId(id);

        //封装数据并返回
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(dishes);

        return setmealVO;
    }

    /**
     * 修改套餐信息，同时修改套餐和菜品的关联关系
     *
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();

        //拷贝对象属性
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //修改套餐基本信息
        setmealMapper.update(setmeal);

        //删除套餐和菜品的关联关系
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        //保存套餐和菜品的关联关系到数据库
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes!=null && setmealDishes.size()>0) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmeal.getId());
            });
        }
        //批量插入数据到数据库
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void startOrStop(Integer status, long id) {
        //如果是启用套餐，需要判断关联的菜品是否为起售状态
        if (status == StatusConstant.ENABLE) {
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && dishList.size() > 0) {
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE == dish.getStatus()) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}

