package es.stuker.buker.ipv6multidest.webserver;

import java.util.Date;

/**
 * Interface for Request objects.
 * Every request object refers to a log line or entry.
 * @author Jorge D'iez de la Fuente
 *
 */
interface Request {
	
	/**
	 * Request identificator.
	 * @return number (int) of line in logfile.
	 */
	int getID();
	
	/**
	 * Difference between Request_N and Request_W, simplifies downcasting.
	 * @return Boolean, true if Request_N and false if Request_W.
	 */
	boolean getType();

	/**
	 * Incoming Timestamp.
	 * @return Timestamp Date.
	 */
	Date getTimestamp();

	/**
	 * Request.
	 * @return Request String.
	 */
	String getRequest();

	/**
	 * HTTP Status.
	 * @return HTTP Status int.
	 */
	int getHTTPStatus();

	/**
	 * Answer length.
	 * @return Answer Length int.
	 */
	int getAnsLength();
	 
	/**
	 * toString().
	 * @return Stringified object.
	 */
	String toString();
}
