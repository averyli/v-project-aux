package myTest2;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;

import annotations.Widget;

@Widget(name = "my second local widget with no parameters")
public class WidgetWithNoParameters {

	@Autowired
	private Logger logger;
	
	@MessageMapping
	public void method(){
		logger.info("WidgetWithNoParameters got the message!");
	}
	
}
