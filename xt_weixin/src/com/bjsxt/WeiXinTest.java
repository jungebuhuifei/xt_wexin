package com.bjsxt;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public class WeiXinTest {
public static void main(String[] args) throws ClientProtocolException, IOException {
  AccessToken token=MessageUtil.getAccessToken();
  System.out.println("token="+token.getToken());
  System.out.println("有效期="+token.getExpiresIn());
}
}
