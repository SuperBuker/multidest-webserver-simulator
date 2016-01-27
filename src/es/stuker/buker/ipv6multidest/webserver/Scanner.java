package es.stuker.buker.ipv6multidest.webserver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Webserver log scanner.
 * Log parser class. Contains multiple regexp to match structures of the log entries.
 * Generates request object for every line or log entry.
 * @author Jorge D'iez de la Fuente
 */
class Scanner {
	static DateFormat for_date = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss Z]", Locale.ENGLISH);

	static String reg_date = "\\[[\\d]{2}/[A-Z]{1}[a-z]{2}/[\\d]{4}(\\:[\\d]{2}){3} \\+[\\d]{4}\\]";
	static String reg_req = "[A-Z]+ [\\S]* HTTP/1.[0-1]";
	static String reg_code = "[\\d]{3} [\\d ]+$";

	static Pattern pat_date = Pattern.compile(reg_date);
	static Pattern pat_req = Pattern.compile(reg_req);
	static Pattern pat_code = Pattern.compile(reg_code);

	static Matcher mat_date, mat_req, mat_code;

	static BufferedReader br = null;
	static String thisLine = null;
	static int id = 1;

	/**
	 * Scanner constructor, includes setup.
	 * @param filepath Path of the log to scan.
	 */
	public Scanner(String filepath) {
		try {
			// open input stream for reading purpose.
			thisLine = null;
			br = new BufferedReader(new FileReader(filepath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse next line and generate Request_N object if well parsed or Request_W if only partially parseable.
	 * @return Request, result of the line parsing. If error returns Request.id=0, if end of file returns Request.id=-1.
	 */
	public Request getRequest() {
		Request tmp = null;
		try {
			if ((thisLine = br.readLine()) != null) {
				mat_date = pat_date.matcher(thisLine);
				mat_req = pat_req.matcher(thisLine);
				mat_code = pat_code.matcher(thisLine);
				if (mat_date.find() && mat_code.find()) {
					// Get Date
					Date str_date = new Date();
					try { // Lang error
						str_date = for_date.parse(mat_date.group(0));
					} catch (java.text.ParseException e) {
						System.err.println(mat_date.group(0));
						tmp = new Request_W(0, null, null, 0, 0);
						id++;
						return tmp;
					}

					// Get Code
					String[] str_code = mat_code.group(0).split("\\s+");

					// Get Request
					if (mat_req.find()) {
						// Std format
						String[] str_req = mat_req.group(0).split("\\s+");

						tmp = new Request_N(id, str_date, str_req[0], str_req[1], str_req[2],
								Integer.parseInt(str_code[0]), Integer.parseInt(str_code[1]));
					} else {
						// Weird format
						String req = thisLine.split("\\\"")[1];

						tmp = new Request_W(id, str_date, req, Integer.parseInt(str_code[0]),
								Integer.parseInt(str_code[1]));
					}

				} else {
					System.err.println(thisLine);
					tmp = new Request_W(0, null, null, 0, 0);
				}
				id++;
			}else{
				tmp = new Request_W(-1, null, null, 0, 0); //EOF	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	/**
	 * Close log file.
	 * @return Result of closing file, false if error.
	 */
	public boolean close(){
		try {
			br.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
