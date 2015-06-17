注意web.xml（24，25行）配置注释调整
	    <param-value>local</param-value>
	    <!-- <param-value>${profiles.active}</param-value> -->
	    
mvn clean package -P test

test 为打包环境目录参数：local,test,online