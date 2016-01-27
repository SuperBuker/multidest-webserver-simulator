package es.stuker.buker.ipv6multidest.webserver;

import java.util.ArrayList;
import java.util.Date;

/**
 * Answer, Request multiplexion.
 * 
 * @author buker
 *
 */
class Answer {

	private Date timestamp; // fecha de salida
	private String request;
	private int httpStatus, ansLength;
	private ArrayList<Integer> id = new ArrayList<Integer>();

	/**
	 * Answer constructor with single ID.
	 * 
	 * @param id
	 *            Line number.
	 * @param timestamp
	 *            Outgoing Date timestamp.
	 * @param request
	 *            Request String.
	 * @param httpStatus
	 *            HTTP Status int.
	 * @param ansLength
	 *            Answer length int.
	 */
	public Answer(int id, Date timestamp, String request, int httpStatus, int ansLength) {
		this.timestamp = timestamp;
		this.request = request;
		this.httpStatus = httpStatus;
		this.ansLength = ansLength;
		this.id.add(id);
	}

	/**
	 * Answer constructor with multiple IDs.
	 * 
	 * @param id
	 *            Line numbers.
	 * @param timestamp
	 *            Outgoing Date timestamp.
	 * @param request
	 *            Request String.
	 * @param httpStatus
	 *            HTTP Status int.
	 * @param ansLength
	 *            Answer length int.
	 */
	public Answer(ArrayList<Integer> id, Date timestamp, String request, int httpStatus, int ansLength) {
		this.timestamp = timestamp;
		this.request = request;
		this.httpStatus = httpStatus;
		this.ansLength = ansLength;
		this.id = id;
	}

	/**
	 * Generates new answer from request object.
	 * 
	 * @param request
	 *            Request to process.
	 */
	public Answer(Request request) {
		this.timestamp = Multiplexer.addSlot(request.getTimestamp());
		switch (request.getHTTPStatus()) {
			case 400: case 401: case 403: case 404: case 405: case 411: case 416: case 500: case 501: case 502: case 503: case 504:
				this.request = ""; // No coincidence by default
				break;
			default:
				this.request = request.getRequest();
		}
		this.httpStatus = request.getHTTPStatus();
		this.ansLength = request.getAnsLength();
		this.id.add(request.getID());
	}

	/**
	 * Outgoing Timestamp
	 * 
	 * @return Timestamp Date.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Request
	 * 
	 * @return Request String. Empty if it shouldn't match between requests.
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * HTTP Status
	 * 
	 * @return HTTP Status int.
	 */
	public int getHTTPStatus() {
		return httpStatus;
	}

	/**
	 * Answer length
	 * 
	 * @return Answer Length int.
	 */
	public int getAnsLength() {
		return ansLength;
	}

	/**
	 * Number of multiplexed answers.
	 * 
	 * @return id.size();
	 */
	public int getDest() {
		return id.size();
	}

	/**
	 * 
	 * @return Stringify ID arraylist.
	 */
	String getIDs() {
		return id.toString();
	}

	/**
	 * toString()
	 * 
	 * @return Stringify object.
	 */
	public String toString() {
		if (request.isEmpty()) {
			return "Timestamp: " + timestamp.toString() + "\nIDs: " + id.toString() + "\nHTTPStatusCode: " + httpStatus
					+ "\nAns: " + ansLength + " bytes";
		} else {
			return "Timestamp: " + timestamp.toString() + "\nIDs: " + id.toString() + "\nReq: " + request
					+ "\nHTTPStatusCode: " + httpStatus + "\nAns: " + ansLength + " bytes";
		}
	}

	/**
	 * Add matching request to existing answer.
	 * 
	 * @param request
	 *            Matched Request.
	 * @return Result of adding ID to IDs array, false if error.
	 */
	boolean addID(Request request) {
		return this.id.add(request.getID());
	}

}
