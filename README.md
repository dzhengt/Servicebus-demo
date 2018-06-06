#Servicebus-demo

	1.此Demo不采用SpringBoot官方提供的ServiceBus配置接入。
	
	2.基于SpringBoot的配置，只能订阅单个Topic。故此Demo提供自定义配置，同时订阅多个Topic。
	
	3.基于Azuer Servicebus官方提供的Jar去实现，Jar配置如下(Maven依赖)：
	
		<dependency>
			<groupId>com.microsoft.azure</groupId>
			<artifactId>azure-servicebus</artifactId>
			<version>1.1.0</version>
		</dependency>
