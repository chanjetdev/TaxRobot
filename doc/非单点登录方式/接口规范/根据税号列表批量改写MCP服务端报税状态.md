## 根据税号列表批量改写MCP服务端报税状态接口

**公共参数:**

+ 必须在http请求header中的Authorization字段中加入数字签名，数字签名的计算请参考[示例程序](./附件/报表取数示例代码)。

### 接口信息
| 接口调用方式 | Restful Service        |
| :----- | :--------------------- |
| 接口地址   |https://mcp.chanjet.com/rpt/updateStatusBatch |
| 请求方法   | GET                    |

### 接口业务参数说明

| 参数名称      | 数据类型    | 限定   | 说明        |
| :-------- | :------ | :--- | :-------- |
| taxNos  | String    | 必填   | 税号或税号串，如果有多个税号用逗号分割 |
|  	status  | String    | 必填   | 状态，取值范围：10（未填写）、15（已填写）、20（已申报） |
|  	period  | String    | 选填   | 期间，如果不填默认为上个报税期，例： 201711 |

例:
====
```
https://mcp.chanjet.com/rpt/updateStatusBatch?taxNos=1234,4567&status=15
```

### 返回值示例及说明
```
{
        "result": true,//成功(true)/失败(false)
        "message": ""//如果 result=false时返回错误信息
}
```

