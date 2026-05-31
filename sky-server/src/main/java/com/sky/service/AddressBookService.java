package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 新增地址接口
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 查询地址列表接口
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 根据id查询地址信息接口
     * @param id
     * @return
     */
    AddressBook getById(Long id);

    /**
     * 根据id修改地址信息接口
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 设置默认地址接口
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);

    /**
     * 删除地址接口
     * @param id
     */
    void delete(Long id);
}
