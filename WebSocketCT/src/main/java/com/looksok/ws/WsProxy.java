package com.looksok.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component // = @Bean in @Configuration
public class WsProxy {

    // we will use this for message sending from server
	private SimpMessagingTemplate messagingTemplate;

    // constructor injection for some reason
    @Autowired
    public WsProxy(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // where is @RequestParam ?! May be no need, because we doesn't test REST here
    public void sendMessage(String clientId, String payload){
        messagingTemplate.convertAndSend("/queue/" + clientId, payload);
    }
}
