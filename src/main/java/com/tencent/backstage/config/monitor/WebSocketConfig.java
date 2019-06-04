package com.tencent.backstage.config.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 配置WebSocket消息代理端点，即stomp服务端
 * Created with IDEA
 * author: lujun
 * Date:2019/5/24
 * Time:14:27
 */
@Configuration
public class WebSocketConfig  implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    /**
     * 推送日志到/topic/pull Logger
     */
    @PostConstruct
    public void pushLogger(){
        Runnable runnable= () -> {
            while (true) {
                try {
                    LogMessage log = LoggerQueue.getInstance().poll();
                    if(log!=null){
                        // 格式化异常堆栈信息
                        if("ERROR".equals(log.getLevel()) && "com.tencent.backstage.common.exception.handler.GlobalExceptionHandler".equals(log.getClassName())){
                            log.setBody("<pre>"+log.getBody()+"</pre>");
                        }
                        if(log.getClassName().equals("jdbc.resultsettable")){
                            log.setBody("<br><pre>"+log.getBody()+"</pre>");
                        }
                        if(messagingTemplate!=null){
                            messagingTemplate.convertAndSend("/topic/logMsg",log);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        executorService.submit(runnable);
    }

}
