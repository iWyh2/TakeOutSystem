package com.wyh;

import com.wyh.TakeOut.utils.SMSUtils;
import com.wyh.TakeOut.utils.SendSms;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SMSTest {
    @Test
    public void SendMsg() {
        //SMSUtils.sendMessage("iWyh2","SMS_267015394","18715705116","1234");
        SendSms.sendMessage("18715705116","0920");
    }
}
