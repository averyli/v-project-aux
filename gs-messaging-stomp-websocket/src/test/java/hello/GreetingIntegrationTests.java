package hello;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@RunWith(SpringRunner.class) // this is runner
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingIntegrationTests {

    @LocalServerPort
    private int port;
    
    // for emulating SockJsClient
    private SockJsClient sockJsClient;
    
    // for emulating WebSocketStompClient, which uses SockJsClient
    private WebSocketStompClient stompClient;
    
    // 'headers' needed for WebSocket handshake
    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    
    /*
     * Here we will create stub objects predominantly for FRONT-END . (this can be auto-generated from source class)
     * */
    @Before
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        
        // wrappers for standard WebSocket protocol?!
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        
        // creating new SockJs client 
        this.sockJsClient = new SockJsClient(transports);

        // creating new SockJs client over WebSocketStompClient
        this.stompClient = new WebSocketStompClient(sockJsClient);
        
        // needed
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    /*
     * Here we go with the test. (this can be auto-generated from source class) 
     * */
    @Test
    public void getGreeting() throws Exception {

        
    	// hmm...
    	final CountDownLatch latch = new CountDownLatch(1);
        
    	// interesting...
    	final AtomicReference<Throwable> failure = new AtomicReference<>();

    	// we do need this guy for the test 
        StompSessionHandler handler = new TestSessionHandler(failure) {

            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
                
            	 // subscribe to Channel (queue) within the session: 
            	session.subscribe("/topic/greetings", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Greeting.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Greeting greeting = (Greeting) payload;
                        try {
                            assertEquals("Hello, Spring!", greeting.getContent());
                        } catch (Throwable t) {
                            failure.set(t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                    }
                });
            	
                try {
                	// stub-client sends message 'Spring' through WebSocket 
                    session.send("/app/hello", new HelloMessage("Spring"));
                } catch (Throwable t) {
                    failure.set(t);
                    latch.countDown();
                }
            }
        };

        // That's the main part of test: stub client connects to socket 'gs-guide-websocket'
        // 'headers' needed for WebSocket handshake
        this.stompClient.connect("ws://localhost:{port}/gs-guide-websocket", this.headers, handler, this.port);

        if (latch.await(3, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        }
        else {
            fail("Greeting not received");
        }
    }

    
    /*********************** AUX CLASSES *************************************/
    
    
    /* I'm recognising, brother Vasya  */
    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;
        
        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        // Acquaintance
        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }
    }
}
