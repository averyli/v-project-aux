package myTest2;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SoketController {

	@Autowired
	private Logger logger;

	@Autowired
	private SimpMessagingTemplate template;

	
	@MessageMapping("/request")
	@SendTo("/outgoing/answer")
	public SocketGreeting getMessageViaWebSocket(SocketRequest request) {
		logger.info("Got request via socket: " + request);
		logger.info("sending message back is requested");
		sendResponseBackInTimeout();
		return sentMessageViaWebSocket();
	}


	public void ping() {
		template.convertAndSend("/outgoing/pong","Pong!");
	}

	public SocketGreeting sentMessageViaWebSocket() {
		SocketGreeting greeting = new SocketGreeting();
		greeting.setMessage("message");
		greeting.setName("name");
		logger.info("Sent message " + greeting);
		return greeting;
	}

	private void sendResponseBackInTimeout() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				for (int i = 0; i < 10; i++) {
					// TODO Auto-generated method stub
					logger.info("start process request");

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}

					ping();

					logger.info("sent request");
				}
			}
		}).start();
	}
}
