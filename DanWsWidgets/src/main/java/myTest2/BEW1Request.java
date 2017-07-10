package myTest2;

public class BEW1Request {
public BEW1Request() {
		super();
	}
private String param1;
private int param2;
public String getParam1() {
	return param1;
}
public void setParam1(String param1) {
	this.param1 = param1;
}
@Override
public String toString() {
	return "BEW1Request [param1=" + param1 + ", param2=" + param2 + "]";
}
public int getParam2() {
	return param2;
}
public void setParam2(int param2) {
	this.param2 = param2;
}

}
