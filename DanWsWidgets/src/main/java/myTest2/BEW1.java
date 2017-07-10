package myTest2;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;

import annotations.Widget;

@Widget(name="Backend widget 1")
public class BEW1 {

	@Autowired
	private Logger logger;
	
	@MessageMapping("connect")
	public String connect(BEW1Request param){
		logger.info("BEW1 got message: "+param);
		return "OK";
	}
	
}
