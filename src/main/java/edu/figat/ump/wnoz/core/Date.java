package edu.figat.ump.wnoz.core;

import java.util.Calendar;

public class Date {
	private int dayNumber;
	private int colNumber;
	private int month;
	private String day;
	private int year;
	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	
	public Date(int dayNumber, int colNumber, String month, String day) {
		this.dayNumber = dayNumber;
		this.setColNumber(colNumber);
		this.month = calculateMonth(month);
		this.day = day;
		this.setYear(calculateYear(month));
	}
	public Date() {}
	
	private int calculateMonth(String month) {
		if(month.equals("I")) 	return 1;
		if(month.equals("II"))	return 2;
		if(month.equals("III"))	return 3;
		if(month.equals("IV"))	return 4;
		if(month.equals("V"))	return 5;
		if(month.equals("VI"))	return 6;
		if(month.equals("VII"))	return 7;
		if(month.equals("VIII"))	return 8;
		if(month.equals("IX"))	return 9;
		if(month.equals("X"))	return 10;
		if(month.equals("XI"))	return 11;
		if(month.equals("XII")) return 12;
		return 0;
	}
	private int calculateYear(String month) {
		if(month.equals("I")) {
			return currentYear +1;
		}
		if(month.equals("II")) {
			return currentYear +1;
		}
		if(month.equals("III")) {
			return currentYear +1;
		}
		if(month.equals("IV")) {
			return currentYear +1;
		}
		if(month.equals("V")) {
			return currentYear +1;
		}
		if(month.equals("VI")) {
			return currentYear +1;
		}
		return currentYear;
	}
	public int getDayNumber() {
		return dayNumber;
	}
	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = calculateMonth(month);
		this.setYear(calculateYear(month));
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public int getColNumber() {
		return colNumber;
	}
	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "Date [dayNumber=" + dayNumber + ", colNumber=" + colNumber + ", month=" + month + ", day=" + day
				+ ", year=" + year + ", currentYear=" + currentYear + "]";
	}
}
