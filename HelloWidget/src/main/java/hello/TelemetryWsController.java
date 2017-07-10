package hello;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TelemetryWsController {
	
	final Random random = new Random();
	
	@Autowired
	private SimpMessagingTemplate template;

	/*	
    @MessageMapping("/start")
    @SendTo("/queue/telemetry")
    public TelemetryModel getTelemetry(WidgetModel widget) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new TelemetryModel(String.valueOf(random.nextInt()));
    }
	*/    
    
    @MessageMapping("/start")    
    public void getTelemetry(WidgetModel widget) throws Exception {
		for (int i = 0; i < 10; i++) {
			Thread.sleep(1000);
			template.convertAndSend("/queue/telemetry", new TelemetryModel(String.valueOf(random.nextInt())));
		}    	
        
    }

}
