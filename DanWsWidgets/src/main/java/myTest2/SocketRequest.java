package myTest2;

public class SocketRequest {

	@Override
	public String toString() {
		return "SocketRequest [client=" + client + ", count=" + count + "]";
	}
	private String client;
	private int count;
	
	public SocketRequest() {
		super();
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}
