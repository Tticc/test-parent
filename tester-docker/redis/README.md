## redis

### 镜像
下载了官方的redis压缩包后打的镜像


### 组成
Dockerfile及打包需要的文件  
compose及env

### 启动
`cd E:\Development\Projects_backup\test-parent\tester-docker\redis\compose`  
docker-compose -f docker-compose-redis-cluster1_1.yaml -p redis_compose up -d  
每次部署都需要重新配置集群关系  
`redis-cli --cluster create 宿主机ip:8881 宿主机ip:8882 宿主机ip:8883 宿主机ip:8884 宿主机ip:8885 宿主机ip:8886 --cluster-replicas 1`  


### 说明



