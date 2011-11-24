package com.zunyi.carpo.model;

public class Offer extends Event {

	private int capacity;
	private boolean shared;
	
	int getCapacity() {
		return capacity;
	}
	void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	boolean isShared() {
		return shared;
	}
	void setShared(boolean shared) {
		this.shared = shared;
	}
}
