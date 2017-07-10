package hello;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SendTelemetryTask implements Runnable {

	final Random random = new Random();

	@Autowired
	private SimpMessagingTemplate template;
	
	@Override
	public void run() {
		
		for (int i = 0; i < 10; i++) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			template.convertAndSend("/queue/telemetry", new TelemetryModel(String.valueOf(random.nextInt())));
		}

	}

}
