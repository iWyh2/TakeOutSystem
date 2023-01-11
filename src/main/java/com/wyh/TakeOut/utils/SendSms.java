package com.wyh.TakeOut.utils;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.models.*;
import com.aliyun.sdk.service.dysmsapi20170525.*;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import java.util.concurrent.CompletableFuture;

/**
 * 短信发送工具类
 */
public class SendSms {
    public static void sendMessage(String phoneNumbers,String param) {
        // Configure Credentials authentication information, including ak, secret, token
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId("LTAI5tRufyjPoi1uimEtrkMZ")
                .accessKeySecret("iWhYqKHjGakPzpXOwxHwcE7yqyM1VW")
                .build());

        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region("cn-wulanchabu") // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();

        // Parameter settings for API request
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("iWyh2")
                .templateCode("SMS_267015394")
                .phoneNumbers(phoneNumbers)
                .templateParam("{\"code\":\""+param+"\"}")
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        // Synchronously get the return value of the API request
        SendSmsResponse resp = null;
        try {
            resp = response.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(new Gson().toJson(resp));

        // Finally, close the client
        client.close();
    }
}
