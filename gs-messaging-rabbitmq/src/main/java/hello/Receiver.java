package hello;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;


/** Reciever reacts on recieved message =Listener: POJO class */
@Component // this gonna be a Spring bean
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        
    	// reacting
    	System.out.println("Received <" + message + ">");
        
    	latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
