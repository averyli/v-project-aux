package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

@Controller
public class TelemetryWsController {
	
	@Autowired
	ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	SendTelemetryTask sendTelemetryTask;
    
    @MessageMapping("/start")    
    public void getTelemetry(WidgetModel widget) throws Exception {

    	taskExecutor.execute(sendTelemetryTask);
        
    }

}
