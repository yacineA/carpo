package com.zunyi.carpo.model;

import java.util.Date;




public abstract class Event {

	private int id;
	private int creator;
	private Date startTimeDate;
	private float startLat;
	private float startLog;
	private float endLat;
	private float endLog;
	private boolean status;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public Date getStartTimeDate() {
		return startTimeDate;
	}
	public void setStartTimeDate(Date startTimeDate) {
		this.startTimeDate = startTimeDate;
	}
	public float getStartLat() {
		return startLat;
	}
	public void setStartLat(float startLat) {
		this.startLat = startLat;
	}
	public float getStartLog() {
		return startLog;
	}
	public void setStartLog(float startLog) {
		this.startLog = startLog;
	}
	public float getEndLat() {
		return endLat;
	}
	public void setEndLat(float endLat) {
		this.endLat = endLat;
	}
	public float getEndLog() {
		return endLog;
	}
	public void setEndLog(float endLog) {
		this.endLog = endLog;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	
	
}
