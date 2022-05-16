## nginx

### 镜像
nginx镜像使用官方提供的镜像包，没有单独打镜像包。因此只有compose


### 组成
default.conf

### 启动
`cd E:\Development\Projects_backup\test-parent\tester-docker\nginx`  
docker-compose -p nginx_compose up -d


### 说明
现在这个nginx是给网关用的
> 网关请求：http://backend-gateway-balance/api/ -> http://tester-gateway:8002/  
> 网关请求：http://backend-gateway-balance/api/ -> http://tester-gateway1:8002/



### 备注
compose里指定了挂在文件夹，需要修改后部署



