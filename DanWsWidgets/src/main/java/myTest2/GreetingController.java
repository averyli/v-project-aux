package myTest2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	@RequestMapping("/")
	public String greeting() {
		return "Hello word";
	}
	
	@RequestMapping("/param")
	public String greeting(@RequestParam(name="param") String param) {
		return "Hello "+param;
	}
}
