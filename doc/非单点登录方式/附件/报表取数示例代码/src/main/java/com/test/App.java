package com.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class App {
    //公钥
    private static String PublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTUaxC5BL/J/SaDDBXe26sXNU/LsGvDmDsfHoHGrrPWy2IC+xPUV7Rl7GEoA/f1ei0rn3Kje2SXKxdr2sowPnVp/FvnczvM1lQ5KVe8CcG/F0Bva7VzQfv/nSe7yXM7OhtLF3baLU+pESOxAnEEbNba0Wd/ftypcb5WhzX7e7EHQIDAQAB";

    //根据税号查询报税数据
    public static void main( String[] args ){
        //税号
        String taxNos="9131000060729587XQ,1,2,3,123456789011121314";//多个税号用逗号分开
        //日期类型，取值范围：month(月报数据)、quarter(季报数据)，如果不写则查月报数据
        String dateType="month";
        //期间（此参数可不写，如果不写的话就表示上月或上季度的数据）
        String period="201711";
        //根据公钥计算数字签名
        String sign= RSATool.encryptByRSAStr(PublicKey,taxNos);
        System.out.println("数字签名：\n"+ sign);

        System.out.println("\n---------根据税号查询报税数据------------");
        //根据税号查询报税数据(批量)
        query(taxNos,dateType,period,sign);
    }

    /**
     * @param taxNos
     * @param dateType
     * @param period
     * @param sign
     */
    private static void query(String taxNos,String dateType,String period,String sign){
        CloseableHttpClient httpClient= HttpClientBuilder.create().build();

        String strUri= String.format("https://mcp.chanjet.com/rpt/downloadBatch?taxNos=%s&dateType=%s&period=%s",
                    taxNos,dateType,period);

        System.out.println("请求地址：\n"+strUri);
        HttpGet httpGet=new HttpGet(strUri);

        try{
            //添加数字签名
            httpGet.addHeader("Authorization",sign);

            CloseableHttpResponse httpResponse= httpClient.execute(httpGet);
            HttpEntity entity= httpResponse.getEntity();
            String text= EntityUtils.toString(entity);

            if (StringUtils.isNotEmpty(text)) {
                System.out.println("------返回数据-------:\n" + text);
                JSONObject jsonObject = JSONObject.parseObject(text);
                System.out.println("------data-------:\n" + jsonObject.getJSONObject("data"));
            }else{
                System.out.println("返回数据为空！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
