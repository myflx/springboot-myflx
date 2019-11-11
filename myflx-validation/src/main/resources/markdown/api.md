# 自定义文档说明

## 效果说明

在`1.9.3`版本中,`swagger-bootstrap-ui`为了满足文档的个性化配置,添加了自定义文档功能

开发者可自定义`md`文件扩展补充整个系统的文档说明

开发者可以在当前项目中添加一个文件夹，文件夹中存放`.md`格式的markdown文件,每个`.md`文档代表一份自定义文档说明

**注意**：自定义文档说明必须以`.md`结尾的文件,其他格式文件会被忽略

https://doc.xiaominfo.com/
http://10.0.20.69:8080/doc.html

目前Springfox-Swagger以及SwaggerBootstrapUi提供的资源接口包括如下：

资源	说明
/doc.html	SwaggerBootstrapUi提供的文档访问地址
/api-docs-ext	SwaggerBootstrapUi提供的增强接口地址
/swagger-resources	Springfox-Swagger提供的分组接口
/api-docs	Springfox-Swagger提供的分组实例详情接口
/swagger-ui.html	Springfox-Swagger提供的文档访问地址
/swagger-resources/configuration/ui	Springfox-Swagger提供
/swagger-resources/configuration/security	Springfox-Swagger提供