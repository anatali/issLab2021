package it.unibo.enablerCleanArch.domain;

public class SonarState implements IDistance{
private int v;

	public SonarState(int d) {
		v=d;
	}
	@Override
	public void setVal(int d) {
		v = d;
	}

	@Override
	public int getVal() {
		return v;
	}
}
