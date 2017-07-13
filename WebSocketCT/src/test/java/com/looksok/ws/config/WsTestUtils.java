package com.looksok.ws.config;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.function.Consumer;

/** This is auxiliary class for test */
class WsTestUtils {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // defined here as a method, not in @Before block in test for some reason ...
    WebSocketStompClient createWebSocketClient() {

    	/* Using the StompClient with a minimum configuration */
    	WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new StringMessageConverter());
        
        return stompClient;
    }

    /* I'm recognising, brother Vasya  */
    static class MyStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("Stomp client is connected");
            super.afterConnected(session, connectedHeaders);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            log.info("Exception: " + exception);
            super.handleException(session, command, headers, payload, exception);
        }
    }

    /** 
     * The MyStompFrameHandler class is responsible for handling the incoming message
     * in within the session
     * and completing the CompletableFuture promise that it received as an argument.
     *  */
    public static class MyStompFrameHandler implements StompFrameHandler {

        private final Consumer<String> frameHandler;

        // constructror
        public MyStompFrameHandler(Consumer<String> frameHandler) {
            this.frameHandler = frameHandler;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            log.info("received message: {} with headers: {}", payload, headers);
            frameHandler.accept(payload.toString());
        }
    }
}
