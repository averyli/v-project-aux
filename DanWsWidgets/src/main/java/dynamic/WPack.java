package dynamic;

import java.lang.reflect.Method;

public class WPack {

	static int i = 0;
	
	public WPack() {
		i++;
		incomingId = "widget"+i;
	}
	public String incomingId;
	public Method incoming;
	public Object obj;
	
}
