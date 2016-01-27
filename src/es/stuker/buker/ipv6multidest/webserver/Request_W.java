package es.stuker.buker.ipv6multidest.webserver;

import java.util.Date;

/**
 * Request Weird, partially parseable.
 * @author Jorge D'iez de la Fuente
 *
 */
class Request_W  implements Request{
	private boolean type;
	private Date timestamp;
	private String request;
	private int id, httpStatus, ansLength;

	/**
	 * Request_W constructor.
	 * @param id Line number.
	 * @param timestamp Incoming Date timestamp.
	 * @param request Request String.
	 * @param httpStatus HTTP Status int.
	 * @param ansLength Answer length int.
	 */
	public Request_W(int id, Date timestamp, String request, int httpStatus, int ansLength) {
		this.type = false; //Weird type
		this.timestamp = timestamp;
		this.request = request;
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
	 * @return Boolean, false.
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
		return request;
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
		return "Timestamp: " + timestamp.toString() + "\nReq: " + request + "\nHTTPStatusCode: "
				+ httpStatus + "\nAns: " + ansLength + " bytes";
	}

}

