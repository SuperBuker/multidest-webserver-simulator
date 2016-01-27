package es.stuker.buker.ipv6multidest.webserver;

import java.util.Date;

/**
 * Request Normal, line fully parseable.
 * @author Jorge D'iez de la Fuente
 */
class Request_N implements Request{
	private boolean type;
	private Date timestamp;
	private String httpMethod, uri, httpVer;
	private int id, httpStatus, ansLength;

	/**
	 * Request_N constructor.
	 * @param id Line number.
	 * @param timestamp incoming Date timestamp.
	 * @param httpMethod Request method int.
	 * @param uri Request URI string.
	 * @param httpVer Request HTTP version string.
	 * @param httpStatus HTTP Status int.
	 * @param ansLength Answer length int.
	 */
	public Request_N(int id, Date timestamp, String httpMethod, String uri, String httpVer, int httpStatus, int ansLength) {
		this.type = true; //Normal type
		this.timestamp = timestamp;
		this.httpMethod = httpMethod;
		this.uri = uri;
		this.httpVer = httpVer;
		this.httpStatus = httpStatus;
		this.ansLength = ansLength;
		this.id = id;
	}
	
	/**
	 * Request identificator.
	 * @return number (int) of line in logfile.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Difference between Request_N and Request_W, simplifies downcasting.
	 * @return Boolean, true.
	 */
	public boolean getType() {
		return type;
	}
	
	/**
	 * Incoming Timestamp.
	 * @return Timestamp Date.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Request.
	 * @return Request String.
	 */
	public String getRequest(){
		return httpMethod + " " + uri + " " + httpVer;
	}

	/**
	 * HTTP Status.
	 * @return HTTP Status int.
	 */
	public int getHTTPStatus() {
		return httpStatus;
	}

	/**
	 * Answer length.
	 * @return Answer Length int.
	 */
	public int getAnsLength() {
		return ansLength;
	}

	/**
	 * toString().
	 * @return Stringified object.
	 */
	public String toString() {
		return "Timestamp: " + timestamp.toString() + "\nReq: " + httpMethod + " " + uri + " " + httpVer + "\nHTTPStatusCode: "
				+ httpStatus + "\nAns: " + ansLength + " bytes";
	}

}
