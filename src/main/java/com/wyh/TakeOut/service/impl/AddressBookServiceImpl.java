package com.wyh.TakeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.TakeOut.pojo.AddressBook;
import com.wyh.TakeOut.mapper.AddressBookMapper;
import com.wyh.TakeOut.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
