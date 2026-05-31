package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "用户端地址簿相关接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址接口
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址接口")
    public Result save(@RequestBody AddressBook addressBook) {
        log.info("新增地址接口");
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 查询当前用户所有的地址接口
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前用户所有的地址接口")
    public Result<List<AddressBook>> list() {
        log.info("查询当前用户所有的地址接口");
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list= addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 根据id查询地址接口
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址接口")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据id查询地址接口");
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址接口
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址接口")
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("根据id修改地址接口");
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址接口
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址接口")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址接口");
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 删除地址接口
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除地址接口")
    public Result delete(Long id) {
        log.info("删除地址接口：id={}", id);
        addressBookService.delete(id);
        return Result.success();
    }

    @GetMapping("default")
    @ApiOperation("查询默认地址接口")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }
}
