## mysql docker集群

### 镜像
mysql镜像使用官方提供的镜像包，没有单独打镜像包。因此只有compose


### 组成
conf里包含了master和slave的配置


### 启动
`cd E:\Development\Projects_backup\test-parent\tester-docker\mysql`  
docker-compose -p mysql_compose up -d


### 说明
mysql使用了一主两从的集群模式，被其他服务和组件依赖，可以最先启动  
~~初始启动需要按照笔记的步骤配置集群主从关系~~   




### 备注
compose里指定了挂在文件夹，需要修改后部署




