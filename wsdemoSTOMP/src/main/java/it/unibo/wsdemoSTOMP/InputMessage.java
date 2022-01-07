package it.unibo.wsdemoSTOMP;

public class InputMessage {
	private String name;
	public InputMessage() { this.name = "xxx"; }
	public InputMessage(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
