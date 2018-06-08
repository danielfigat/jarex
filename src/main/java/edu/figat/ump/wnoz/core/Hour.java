package edu.figat.ump.wnoz.core;

import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

public class Hour {
	private String startTime;
	private String endTime;
	private int hourNumber;
	private int rowNumber;
	private boolean isFull;

	public Hour(String startTime, String endTime, int hourNumber, int rowNumber, boolean full) throws ParseException {
		//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		//this.startTime = sdf.parse(startTime);
		//this.endTime = sdf.parse(endTime);
		this.startTime = startTime;
		this.endTime = endTime;
		this.hourNumber = hourNumber;
		this.rowNumber = rowNumber;
		this.isFull = full;
	}
	public Hour() {}
	@Override
	public String toString() {
		return "Hour [startTime=" + startTime + ", endTime=" + endTime + ", hourNumber=" + hourNumber + ", rowNumber="
				+ rowNumber + ", isFull=" + isFull + "]";
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getHourNumber() {
		return hourNumber;
	}
	public void setHourNumber(int hourNumber) {
		this.hourNumber = hourNumber;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public boolean isFull() {
		return isFull;
	}
	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}
}
