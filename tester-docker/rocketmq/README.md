## rocketmq

### 镜像
下载了官方的rocketmq压缩包后打的镜像  
根据角色打了三个镜像：broker、namesrv、dashboard  


### 组成
Dockerfile及打包需要的文件  
compose及env

### 启动
`cd E:\Development\Projects_backup\test-parent\tester-docker\rocketmq`  
docker-compose -p rocketmq_compose up -d  
每次部署都需要重新配置集群关系  
`redis-cli --cluster create 宿主机ip:8881 宿主机ip:8882 宿主机ip:8883 宿主机ip:8884 宿主机ip:8885 宿主机ip:8886 --cluster-replicas 1`  


### 说明
整个compose文件包含了broker、namesrv、dashboard三个角色的部署  
**dashboard**：单实例  
**namesrv**：两个实例  
**broker**：  
一个集群，集群由两个broker集群组成，每个broker集群由一主一从两个实例组成。共四个实例
> 默认只配置了CVCluster1集群，需要可以加上CVCluster2及其他新的集群


### 访问
http://localhost:8080/#/topic  








