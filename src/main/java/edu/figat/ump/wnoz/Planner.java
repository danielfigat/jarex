package edu.figat.ump.wnoz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.figat.ump.wnoz.core.Date;
import edu.figat.ump.wnoz.core.Hour;

public class Planner 
{
	public static String data[][];
	private static int COL = 177;
	private static int ROW = 698;
	public static List<String> grupy = new Vector<String>();
	public static Map<Integer, Date> allDays = new TreeMap<Integer, Date>();
	public static Map<Integer, Hour> allHours = new TreeMap<Integer, Hour>();
	static List<Subject> przedmioty = new LinkedList<Subject>();
	public static void print(Object obj) {
		System.out.println(obj);
	}
	public static void main( String[] args )
	{
		FileInputStream file = null;
		XSSFWorkbook workbook = null;
		try {
			file = new FileInputStream(new File("test.xlsx"));
			workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			data = new String[COL][ROW];
			for(int i=0; i<COL; i++) {
				for(int j=0; j<ROW; j++) {
					data[i][j] = "";
				}
			}
			
			for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
				CellRangeAddress region = sheet.getMergedRegion(i); //Region of merged cells

				int firstCol = region.getFirstColumn(); //number of columns merged
				int firstRow = region.getFirstRow();      //number of rows merged
				int lastCol = region.getLastColumn();
				int lastRow = region.getLastRow();
				//print(firstCol + ":" + firstRow + ":" + lastCol + ":" + lastRow);
				merger(firstCol, firstRow, lastCol, lastRow);
			}
			System.out.println("generated ...");

			//*
			String groupName = "_";
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int localColIndex = cell.getColumnIndex();
					int localRowIndex = cell.getRowIndex();

					if(localColIndex == 2 || localColIndex == 1 || localColIndex == 94 || localColIndex == 95) {
						//if(cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
						if(cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
							String hour = new Integer(cell.getDateCellValue().getHours()).toString();
							String minutes = new Integer(cell.getDateCellValue().getMinutes()).toString();

							if((minutes != null) && (minutes.length() == 1)) {
								minutes += "0";
							}
							data[cell.getColumnIndex()][cell.getRowIndex()] = hour + ":" +  minutes;
						}
					}
					else {
						cell.setCellType(CellType.STRING);
						String newValue = cell.getStringCellValue();
						if(newValue.isEmpty() == false) {
							data[localColIndex][localRowIndex] = newValue;
						}
					}

					if(localColIndex == 0) {
						String newGroupName = cell.getStringCellValue();
						if(newGroupName.contains("GRUPA")) {
							groupName = newGroupName
									.replaceAll(" ", "")
									.replaceAll("GRUPA", "GRUPA ");
							groupName = convertGroup(groupName);
							data[localColIndex][localRowIndex] = groupName;
						}
						if(data[localColIndex][localRowIndex].equals("_")) {
							data[localColIndex][localRowIndex] = groupName;
						}
					}
				}
			}

			// obliczanie dat
			for(int localColIndex=0; localColIndex<COL; localColIndex++) {
				int localRowIndex=5;
				Integer number = tryParse(data[localColIndex][localRowIndex]);
				//print("-->" + number);
				if(number != null) {
					Date newLocalDate = new Date();
					newLocalDate.setDayNumber(Integer.parseInt(data[localColIndex][localRowIndex]));
					newLocalDate.setColNumber(localColIndex);
					newLocalDate.setMonth(data[localColIndex][localRowIndex+1]);
					newLocalDate.setDay(data[localColIndex][localRowIndex+2]);
					allDays.put(localColIndex, newLocalDate);
				}
			}

			//obliczanie godzin
			boolean lastRowWasHour = false;
			for(int localRowIndex=8; localRowIndex < 695; localRowIndex++) {
				int localColIndex=1;
				if(data[localColIndex][localRowIndex].equals("_") == false) {
					lastRowWasHour = true;
					Hour localHour = new Hour(
							data[localColIndex][localRowIndex],
							data[localColIndex+1][localRowIndex],
							Integer.parseInt(data[localColIndex+2][localRowIndex]),
							localRowIndex,
							lastRowWasHour
							);
					allHours.put(localRowIndex, localHour);
				}
				else {
					if(lastRowWasHour) {
						lastRowWasHour = false;
						data[localColIndex][localRowIndex] = data[localColIndex][localRowIndex-1]; 
						data[localColIndex+1][localRowIndex] = data[localColIndex+1][localRowIndex-1]; 
						data[localColIndex+2][localRowIndex] = data[localColIndex+2][localRowIndex-1]; 
						Hour localHour = new Hour(
								data[localColIndex][localRowIndex],
								data[localColIndex+1][localRowIndex],
								Integer.parseInt(data[localColIndex+2][localRowIndex]),
								localRowIndex,
								lastRowWasHour
								);
						allHours.put(localRowIndex, localHour);
					}
				}
			}

			/*
			for (Map.Entry<Integer, Date> entry : allDays.entrySet()) {
			     System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
			}
			for (Map.Entry<Integer, Hour> entry : allHours.entrySet()) {
			     System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
			}
			 */

			//*/
			print("computed...");
			List<Integer> godziny = new ArrayList<Integer>(allHours.keySet());
			List<Integer> dni = new ArrayList<Integer>(allDays.keySet());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(int dzien: dni) {
				//if(dzien == 10) break;
				for(int godzina: godziny) {
					if((data[dzien][godzina].length() != 0) && (data[dzien][godzina].equals("_") == false)) {
						String subjectName = data[dzien][godzina];
						String grupa = data[0][godzina];

						Date localDate = allDays.get(dzien);
						String checkedMonth = Integer.toString(localDate.getMonth());
						if(checkedMonth.length() == 1) {
							checkedMonth = "0" + checkedMonth;
						}
						String checkedDay = Integer.toString(localDate.getDayNumber());
						if(checkedDay.length() == 1) {
							checkedDay = "0" + checkedDay;
						}

						Hour localHour = allHours.get(godzina);
						String checkedHour = null;
						if(localHour.isFull()) {
							checkedHour = localHour.getStartTime();
						}
						else {
							checkedHour = localHour.getEndTime();
						}
						if(checkedHour.length() == 4) {
							checkedHour = "0" + checkedHour;
						}

						String strDate = localDate.getYear() + "-" + checkedMonth + "-" +
								checkedDay + " " + checkedHour;
						java.util.Date newDate = df.parse(strDate);

						boolean findTime = true;
						int iter = 1;
						while(findTime) {
							if(data[dzien][godzina+iter].equals("_")) {
								iter++;
							}
							else {
								iter--;
								findTime = false;
							}
						};
						Hour endTime = allHours.get(godzina+iter);

						Subject newLocalSubject = new Subject(subjectName, newDate, grupa);
						if(localHour.isFull()) {
							newLocalSubject.setStartTime(localHour.getStartTime());							
						}
						else {
							newLocalSubject.setStartTime("po " + localHour.getStartTime());
						}
						//print(godzina);
						//print(godzina+iter);
						if(endTime.isFull()) {
							newLocalSubject.setEndTime("przed " + endTime.getEndTime());							
						}
						else {
							newLocalSubject.setEndTime(endTime.getEndTime());
						}


						//System.out.println(grupa + " [" + localHour.getStartTime() + "] " + subjectName);
						przedmioty.add(newLocalSubject);
					}
				}
			}

			print("sorting...");
			Collections.sort(przedmioty);
			print("sorted...");

			List<Subject> temp = new LinkedList<Subject>();

			print("merging...");
			Iterator<Subject> it = przedmioty.iterator();
			temp.add(przedmioty.get(0));
			it.next();
			while(it.hasNext()) {
				Subject outS = (Subject) it.next();
				boolean inTemp = false;
				for(int i=0; i < temp.size(); i++) {
					Subject inS = temp.get(i);
					//if(inTemp) break;
					if(inS.toCheck().equals(outS.toCheck())) {
						if(inS.getGroup().contains(outS.getGroup()) == false) {
							temp.get(i).setGroup(inS.getGroup() + ", " + outS.getGroup());
							inTemp = true;
						}
					}
				}
				if(inTemp == false) {
					temp.add(outS);
				}
				//print(temp.size());
			}
			print("merged...");
			for(int i=0; i<temp.size(); i++) {
				String testString = "GRUPA 01, GRUPA 02, GRUPA 03, GRUPA 04, GRUPA 05, GRUPA 06, GRUPA 07, GRUPA 08, GRUPA 09, GRUPA 10, GRUPA 11, GRUPA 12, GRUPA 13, GRUPA 14, GRUPA 15, GRUPA 16";
				if(temp.get(i).getGroup().equals(testString)) {
					temp.get(i).setGroup("CA£Y ROK");
				}
				/*
				if(temp.get(i).getSubjectName().equals("Szk")) {
					print(">>" + temp.get(i).getGroup() + "<<");
				}
				*/
			}
			przedmioty = temp;
			save();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (file != null) {
				try {
					file.close();
					workbook.close();

					file = null;
				} catch (Exception e) {
					System.out.println("File closing operation failed");
					e.printStackTrace();
				}
			}
		}		
	}
	private static String convertGroup(String groupName) {
		if(groupName.equals("GRUPA I")) 	return "GRUPA 01";
		if(groupName.equals("GRUPA II"))	return "GRUPA 02";
		if(groupName.equals("GRUPA III"))	return "GRUPA 03";
		if(groupName.equals("GRUPA IV"))	return "GRUPA 04";
		if(groupName.equals("GRUPA V"))		return "GRUPA 05";
		if(groupName.equals("GRUPA VI"))	return "GRUPA 06";
		if(groupName.equals("GRUPA VII"))	return "GRUPA 07";
		if(groupName.equals("GRUPA VIII"))	return "GRUPA 08";
		if(groupName.equals("GRUPA IX"))	return "GRUPA 09";
		if(groupName.equals("GRUPA X"))		return "GRUPA 10";
		if(groupName.equals("GRUPA XI"))	return "GRUPA 11";
		if(groupName.equals("GRUPA XII")) 	return "GRUPA 12";
		if(groupName.equals("GRUPA XIII")) 	return "GRUPA 13";
		if(groupName.equals("GRUPA XIV")) 	return "GRUPA 14";
		if(groupName.equals("GRUPA XV")) 	return "GRUPA 15";
		if(groupName.equals("GRUPA XVI")) 	return "GRUPA 16";
		return groupName;
	}
public static Integer tryParse(String text) {
	try {
		return Integer.parseInt(text);
	} catch (NumberFormatException e) {
		return null;
	}
}

private static void merger(int firstCol, int firstRow, int lastCol, int lastRow) {
	boolean firstTime = true;
	// data[COL][ROW]
	for(int j=firstRow; j <= lastRow; j++) { //ROW
		for(int i=firstCol; i <= lastCol; i++) { //COL
			if(firstTime) {
				data[i][j] = "@";
				firstTime = false;
			}
			else {
				data[i][j] = "_";
			}
		}
		//System.out.println();
	}
}
private static void save() throws FileNotFoundException {
	/*
		PrintWriter out = new PrintWriter("filename.txt");
		for(int i=0; i<ROW; i++) {
			for(int j=0; j<COL; j++) {
				out.print(data[j][i]);
				out.print("|");
			}
			out.println();
		}
		out.close();
		//*/

	PrintWriter out2 = new PrintWriter("lista.txt");
	Object oldSubjectName = "";
	for(Subject s: przedmioty) {
		if(s.getSubjectName().equals(oldSubjectName) == false) {
			oldSubjectName = s.getSubjectName();
			out2.println("[" + oldSubjectName + "]");
		}
		out2.println(s);
	}
	out2.close();
	System.out.println("file created ...");
}
}