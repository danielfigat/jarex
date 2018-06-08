package edu.figat.ump.wnoz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Subject implements Comparable<Subject> {
	private String subjectName;
	private Date date;
	private String group;
	private String startTime;
	private String endTime;
	Subject(String subjectName, Date date, String group) {
		this.subjectName = subjectName;
		this.date = date;
		this.group = group;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public int compareTo(Subject o) {
		int ans = this.subjectName.compareTo(o.subjectName);
		if (ans == 0) {
			ans = this.group.compareTo(o.group);
			if (ans == 0) {
				ans = this.date.compareTo(o.date);
			}
		}
		return ans;
	}
	public String check() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		
		String pStartTime = startTime;
		String pEndTime = endTime;
		if((startTime.length() == 4)) {
			pStartTime = "0" + startTime;
		}
		if(endTime.length() == 4) {
			pEndTime = "0" + endTime;
		}
				
		return 	"[" + pStartTime +
				"][" + pEndTime +
				"] " + df.format(date) + 
				"";
	}
	public String toCheck() {
		return this.subjectName + check();
	}
	@Override
	public String toString() {
		return 	group + " " + check();
	//	return "[przedmiot=" + subjectName + ", data=" + df.format(date) + ", start=" + startTime 
	//			+ ", end=" + endTime + ", groupa=" + group + "]";
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
}
