package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理控制器
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类管理相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类接口
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类接口")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询接口
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询分类接口")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分页查询分类：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.PageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除分类接口
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类接口")
    public Result delete(Long id) {
        log.info("删除分类：id={}", id);
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 修改分类接口
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类接口")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类");
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用分类接口")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id) {
        log.info("启用或禁用分类：{},{}", status, id);
        categoryService.startOrStop(status, id);
        return Result.success();
    }
    /**
     * 查询分类列表接口
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询分类列表接口")
    public Result<List<Category>> list(Integer type) {
        log.info("查询分类列表：type={}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
