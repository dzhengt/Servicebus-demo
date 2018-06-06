package com.cn.demo.service.imp;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

/**
 * 接收订阅某个Topic消息
 * 
 * @date 2018-06-06
 * 
 * */
@Service
public class TopicReceiver {
	
	
	private static TopicClient topicClient;

    private static SubscriptionClient subscriptionClient;
    
    @Value("${azure.servicebus.connection-string}") 
    private String connectionUlrl;
    
    @Value("${FirstTopicSharedAccessKey}") 
    private String sharedAccessKey;
    
    @Value("${FirstSubscriptionName}")
    private String subscriptionName;
    
    @Value("${FirstTopicName}")
    private String topicName;
    
    @PostConstruct
    public  void afterConstruct() throws ServiceBusException, InterruptedException{
    	
    	 subscriptionClient =new SubscriptionClient(new ConnectionStringBuilder(connectionUlrl+sharedAccessKey, topicName + "/subscriptions/" + subscriptionName), ReceiveMode.PEEKLOCK);
         receiveSubscriptionMessage();
    }
    
    private void receiveSubscriptionMessage() throws ServiceBusException, InterruptedException {
        subscriptionClient.registerMessageHandler(new MessageHandler(), new MessageHandlerOptions());
    }

    /**
     * 根据提供Endpoint是否有具有发送消息的权限去创建topicClient
     * 
     * 若没有权限topicClient，则notifyException时则会报权限异常
     * 
     * */
	public void sendTopicMessage(String messageBody) throws ServiceBusException, InterruptedException {
        System.out.println("RefundOrder Topic Sending message: " + messageBody);
        final Message message = new Message(messageBody.getBytes(StandardCharsets.UTF_8));
        topicClient.send(message);
    }

     class MessageHandler implements IMessageHandler {
         
        public CompletableFuture<Void> onMessageAsync(IMessage message) {
        	
            final String messageString = new String(message.getBody(), StandardCharsets.UTF_8);
            
            System.out.println("Topic Received message: " + messageString);
            
            /**
             * TO DO 
             * 处理接收到的消息(数据)
             * 
             * */
           
            return CompletableFuture.completedFuture(null);
        }

        public void notifyException(Throwable exception, ExceptionPhase phase) {
            System.out.println(phase + " encountered exception:" + exception.getMessage());
            try {
				subscriptionClient.close();
				topicClient.close();
				afterConstruct();
				
			} catch (ServiceBusException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
}
