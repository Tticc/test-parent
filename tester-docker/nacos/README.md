## nacos docker集群

### 镜像
nacos镜像使用官方提供的镜像包，没有单独打镜像包。因此只有compose


### 组成
nacos-mysql.env

### 启动
`cd E:\Development\Projects_backup\test-parent\tester-docker\nacos`  
docker-compose -p nacos_compose up -d


### 说明
nacos给微服务使用，同时被nginx代理。  
使用了mysql集群，所以需要后于mysql集群启动


### 访问
http://localhost:8849/nacos


### 备注
compose里指定了挂在文件夹，需要修改后部署






