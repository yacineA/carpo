package com.zunyi.carpo.model;

import java.sql.Date;




public abstract class Event {

	private int id;
	private int creator;
	private Date startTimeDate;
	private float startLat;
	private float startLog;
	private float endLat;
	private float endLog;
	private boolean status;

	int getId() {
		return id;
	}
	void setId(int id) {
		this.id = id;
	}
	int getCreator() {
		return creator;
	}
	void setCreator(int creator) {
		this.creator = creator;
	}
	Date getStartTimeDate() {
		return startTimeDate;
	}
	void setStartTimeDate(Date startTimeDate) {
		this.startTimeDate = startTimeDate;
	}
	float getStartLat() {
		return startLat;
	}
	void setStartLat(float startLat) {
		this.startLat = startLat;
	}
	float getStartLog() {
		return startLog;
	}
	void setStartLog(float startLog) {
		this.startLog = startLog;
	}
	float getEndLat() {
		return endLat;
	}
	void setEndLat(float endLat) {
		this.endLat = endLat;
	}
	float getEndLog() {
		return endLog;
	}
	void setEndLog(float endLog) {
		this.endLog = endLog;
	}
	boolean isStatus() {
		return status;
	}
	void setStatus(boolean status) {
		this.status = status;
	}

	
	
}
