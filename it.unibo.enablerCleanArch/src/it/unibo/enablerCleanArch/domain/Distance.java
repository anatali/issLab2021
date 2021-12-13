package it.unibo.enablerCleanArch.domain;

public class Distance implements IDistance{
	  private int v;
	    public Distance(int d) { v=d;	}
	    @Override
	    public void setVal(int d) {	v = d;	}
	    @Override
	    public int getVal() { return v; }
}
