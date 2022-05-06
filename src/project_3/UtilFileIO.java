package project_3;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class UtilFileIO {
	
	public static void main(String[] args) {
		File file = new File("./test.txt");
		saveFile(file, "다시test임다");
		
		
		saveFile("무슨일이생기면 언제나 어디서나 나타나는\n홍반장 ~~~~");
		
		// 파일 읽어오기
		// loadFile을 사용하여 파일 내용을 문자열로 불러온 후 출력한다.
		String str = loadFile(file);
		System.out.println("<파일내용>\n" + str);
		
		File filedt = new File("./20160825_173923.txt");
		System.out.println(loadFile(filedt));
	}

	
	// 파일을 저장할 때 사용한다. File에 String 데이터를 저장하여 파일을 생성한다. FileWriter는 문자 기반 스트림으로 텍스트 데이터를 파일에 저장할 때 사용한다. 
	// 문자 단위로 저장하므로 텍스트만 저장 가능하다. BufferedWriter는 System.out.println();과 유사하고 속도 측면에서 훨씬 빠르기 때문에
	// (입력된 데이터가 바로 전달되지 않고 버퍼를 거쳐 전달되므로 데이터 처리 효율성을 높임) 많은 양의 데이터를 처리할 때 유리하다성
	public static boolean saveFile(File file, String str) {
		boolean result = false;
		//File에 String 데이타 저장
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(str);
			bw.flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 파일을 저장할 때 사용한다. File에 JTextArea에 있는 Text를 저장한다.
	public static boolean saveFile(File file, JTextArea jta) {
		boolean result = false;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		// File에 String 데이터를 저장하여 파일을 생성한다. 
		// FileWriter는 문자 기반 스트림으로 텍스트 데이터를 파일에 저장할 때 사용한다. 문자 단위로 저장하므로 텍스트만 저장 가능하다. 
		// BufferedWriter는 System.out.println();과 유사하고 속도 측면에서 훨씬 빠르기 때문에
		// (입력된 데이터가 바로 전달되지 않고 버퍼를 거쳐 전달되므로 데이터 처리 효율성을 높임) 많은 양의 데이터를 처리할 때 유리하다. 
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(jta.getText());
			bw.flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 파일을 저장할 때 사용한다. File에 JTextPane에 Text를 저장한다. 코드 내용은 위의 내용과 흡사하므로 생략한다.
	public static boolean saveFile(File file, JTextPane jtp) {
		boolean result = false;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File에 String 데이타 저장
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(jtp.getText());
			bw.flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 현재 시간을 문자열로 반환한다. getCurrentDateTime의 형태는 yyyymmdd_hh24miss이다.
	public static String getCurrentDateTime() {
		GregorianCalendar calendar = new GregorianCalendar();
		String strDateTime = ""; 
		int year   = calendar.get(Calendar.YEAR);
		int month  = calendar.get(Calendar.MONTH) + 1;
		int day    = calendar.get(Calendar.DAY_OF_MONTH);
//		int hour   = calendar.get(Calendar.HOUR);
		int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String strYear  = String.valueOf(year);
		String strMonth = month < 10 ? "0" + month : String.valueOf(month);
		String strDay = day < 10 ? "0" + day : String.valueOf(day);
		String strHour = hour24 < 10 ? "0" + hour24 : String.valueOf(hour24);
		String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
		String strSecond = second < 10 ? "0" + second : String.valueOf(second);
		
		strDateTime = strYear + strMonth + strDay + "_" + strHour + strMinute + strSecond;
		return strDateTime;
	}
	
	// 현재 시간을 문자열로 반환한다. 형태는 yyyymmdd+delimeter+hh24miss로 반환한다.
	public static String getCurrentDateTime(String delimeter) {
		GregorianCalendar calendar = new GregorianCalendar();
		String strDateTime = ""; 
		int year   = calendar.get(Calendar.YEAR);
		int month  = calendar.get(Calendar.MONTH) + 1;
		int day    = calendar.get(Calendar.DAY_OF_MONTH);
//				int hour   = calendar.get(Calendar.HOUR);
		int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String strYear  = String.valueOf(year);
		String strMonth = month < 10 ? "0" + month : String.valueOf(month);
		String strDay = day < 10 ? "0" + day : String.valueOf(day);
		String strHour = hour24 < 10 ? "0" + hour24 : String.valueOf(hour24);
		String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
		String strSecond = second < 10 ? "0" + second : String.valueOf(second);
		
		strDateTime = strYear + strMonth + strDay 
				    + delimeter 
				    + strHour + strMinute + strSecond;
		return strDateTime;
	}	
	
	// 파일을 저장할 때 사용한다.  JTextPane의 데이터를 yyyymmdd_hh24miss.txt 에 저장한다. 
	// return은 저장된 파일명으로 ("./ + yyyymmdd_hh24miss.txt") 이다.
	public static String saveFile(JTextPane jtp) {
		String filename = "";
		String strFile = "./" + getCurrentDateTime() + ".txt";
		File file = new File(strFile);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File에 String 데이타 저장
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(jtp.getText());
			bw.flush();
			filename = strFile;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	
	// 파일을 저장할 때 사용하고 JTextArea 의 데이터를 yyyymmdd_hh24miss.txt 에 저장한다.
	// return은 저장된 파일명으로 ("./ + yyyymmdd_hh24miss.txt")이다.
	public static String saveFile(JTextArea jta) {
		String filename = "";
		String strFile = "./" + getCurrentDateTime() + ".txt";
		File file = new File(strFile);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File에 String 데이타 저장
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(jta.getText());
			bw.flush();
			filename = strFile;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	
	// 파일을 저장할 때 사용하고 문자열 데이터를 yyyymmdd_hh24miss.txt 에 저장한다.
	// return은 저장된 파일명으로 ("./ + yyyymmdd_hh24miss.txt")이다.
	public static String saveFile(String str) {
		String filename = "";
		String strFile = "./" + getCurrentDateTime() + ".txt";
		File file = new File(strFile);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File에 String 데이타 저장
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(str);
			bw.flush();
			filename = strFile;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	
	// 파일을 읽어올 때 사용한다. File에 데이터를 읽어와서 String 문자열을 반환한다.
	public static String loadFile(File file) {
		String result = null;
		// 파일에서 내용 가져오기
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer(4096);
		
		try {
			br = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str +"\n");
			}
			result = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 파일을 읽어올 때 사용한다. 파일명(dir + filename)을 String으로 받아 내용을 String으로 반환한다.
	// filename은 디렉토리 + 파일명 + 확장자 정보가 포함된 문자열이다. 
	// return은 filename의 내용을 String으로 return한다.
	public static String loadFile(String filename) {
		if (filename == "") return "";
		String result = null;
		// 파일에서 내용 가져오기
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer(4096);
		File file = new File(filename);
		
		try {
			br = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
			result = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 파일을 가져올 때(열 때) 사용한다. 파일다이얼로그를 이용하여 파일명(디렉토리 + 파일)을 가져온다.
	// currDir은 파일 검색할 디렉토리 위치이다.
	// return은 파일 다이얼로그를 통해 선택한 파일명(디렉토리 + 파일)을 리턴한다.
	public static String getFilenameFromFileOpenDialog(String currDir) {
		String filename = null;
		FileDialog fd = null;
		fd = new FileDialog(fd, "파일열기", FileDialog.LOAD);
		fd.setVisible(true);
		String dir = fd.getDirectory();
		String fname = fd.getFile();
		filename = dir + fname;
		return filename;
	}
}
