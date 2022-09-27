![](https://www.zifisense.com/v4/images/logo.png#pic_center)

![ZETA](https://img.shields.io/badge/project-ZETA_HTTP_SDK-blue)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-brightgreen.svg)
[![license](https://img.shields.io/badge/license-MIT-orange)](https://github.com/zifisense/zeta-http-sdk/blob/master/LICENSE.txt)

概述
============
此SDK是网管平台的`JAVA` http对接方式以及示例代码，如果非java语言，请使用http的方法请求即可，接口参考文档

* **[Java语言-Jar](https://github.com/zifisense/zeta-http-sdk/releases)**

安装
============

### 依赖包(下载最新的release中的*zip*包)
* [slf4j](http://www.slf4j.org/) / logback (Logger)
* [fastjson2](https://github.com/alibaba/fastjson2) (Alibaba JSON Utils)

> 其中 slf4j 可以与 logback, log4j, commons-logging 等日志框架一起工作，可根据你的需要配置使用。

如果使用 Maven 构建项目，要先将zip中的zeta-http-sdk-xxx.jar文件安装到本地的maven仓库(**[安装方法](https://blog.csdn.net/Ivy_Xinxxx/article/details/126284107)**)
<br/>完成后需要在你的项目 pom.xml 里增加：

```Java
    <dependency>
        <groupId>com.zifisense.zeta</groupId>
        <artifactId>zeta-http-sdk</artifactId>
        <version>2.0.0</version>
    </dependency>


    <!-- 如果项目中已经引入，就不必引入了 -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.2</version>
    </dependency>

    <!-- 如果项目中已经引入日志框架了，就不必引入了 -->
    <!-- For logback log dependency-->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.3</version>
    </dependency>
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>ch.qos.logback</artifactId>
        <version>1.2.3</version>
    </dependency>
```

如果不使用 Maven 构建项目，则项目 libs/ 目录下有依赖的 jar 可复制到你的项目里去。


SDK使用方式
=================

```java
    /**
    * @desc demo获取终端下行控制 
    * hostName
    * api_key ----- 在后台系统->系统管理->企业管理-->企业秘钥字段
    * secret_key ----- 在后台系统->系统管理->企业管理-->企业编码字段
    * msDeviceId(终端设备ID) ----- 需要在后台系统->设备管理->终端管理中获取
    */
    @Test
    public void httpClientTest()throws Exception{
        String msDeviceId = "70000001";
        IZifiHttpClient client = ZifiHttpClientFactory.getClient(HOST_NAME, API_KEY, SECRET_KEY);
        String uri = String.format("/teamcms/ws/zeta_v2/wan_ms/query/%s/getMsCtlHistoryByDate",msDeviceId);
        Map<String,String>header = new HashMap<>();
        header.put("Content-Type","application/json");
        Map<String,Object> params = new HashMap<>();
        params.put("starttime","1659283200");
        params.put("endtime","1661875200");
        ApiResponse apiResponse = client.requestUrl(HttpMethod.POST,uri,header,params);
        System.out.println(JSON.toJSONString(apiResponse));
    }
```

> 提示：<br>
> 1. 更多的API接口请见文档<br>
> 2. 默认使用的是v2版本(<img src="https://github.githubassets.com/images/icons/emoji/unicode/26a0.png"  height="20" width="20">注意：v1和v2接口的分辨是根据请求URL中的zeta_v2或zeta_v1来分辨的)
> 3. `ZifiHttpClientFactory.getClient`默认使用的v2版本的客户端如果想使用v1的客户端(v1不推荐使用,此处只是为了兼容版本)，则使用下面方式来显示辨识v1<br/>`IZifiHttpClient client = ZifiHttpClientFactory.getClient(hostName, api_key, secret_key,ZifiHttpVersion.V1);`


完整的代码示例
============
调用方式参考：com.zifisense.zeta.http.api.ZifiNewHttpClientTest.testV2

> 提示：<br>
> 1. v1版本的代码示例在com.zifisense.zeta.http.api.ZifiNewHttpClientTest.testV1



