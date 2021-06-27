package com.momo.server.utils;

public class Entry <Key extends Comparable<Key>, Value> {

	
	private Key ky;
	private Value val;
	public Entry(Key newKey, Value newValue) {
		ky = newKey;
		val = newValue;
	}
	public Key getKey() {
		return ky;
	}
	public void setKey(Key newKey) {
		ky=newKey;
	}
	public Value getValue() {
		return val;
	}
	public void setValue(Value newValue) {
		val=newValue;
	}
}
