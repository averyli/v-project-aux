package myTest2;

public class SocketGreeting {

	@Override
	public String toString() {
		return "SocketGreeting [name=" + name + ", counter=" + counter + ", message=" + message + "]";
	}

	private String name;
	private int counter;
	private String message;
	
	private static int count=0;
	
	public SocketGreeting(){
		count++;
		counter = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCounter() {
		return counter;
	}
	
}
