## 微服务集群

### 镜像
当前工程的服务镜像，Dockerfile在各个模块下  


### 组成
compose和env


### 启动
`cd E:\Development\Projects_backup\test-parent\tester-docker\ms`  
docker-compose -p ms_compose up -d
docker-compose -f docker-compose-search.yaml -p search_compose up -d

### 说明
当前配置了两个网关实例  
两个微服务（各一个实例）  


### 访问
有ng  
http://localhost:80/api/tester-webapp/testfeign/get  
http://localhost:80/api/tester-webapp/demo/demoStart1  
无ng  
http://localhost:8004/demo/demoStart1  


### 备注
compose的env里指定了挂在文件夹，需要修改后部署






