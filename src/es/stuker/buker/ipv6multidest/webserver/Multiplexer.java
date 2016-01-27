package es.stuker.buker.ipv6multidest.webserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Webserver answer multiplexer simulator.
 * Contains output list and slot parameter.
 * Receives request objects and date updates periodically.
 * Tries to match requests in an output list then pops them when new date exceeds timeout.
 * @author Jorge D'iez de la Fuente
 *
 */
class Multiplexer {
	private ArrayList<Answer> outputList;
	private static int slot = 1; //seconds
	
	Multiplexer(){
		outputList= new ArrayList<Answer>();
	}
	
	/**
	 * Clean output buffer up to new date.
	 * Pops anwers which new date exceeds their timeout.
	 * @param now new date.
	 * @return output buffer.
	 */
	ArrayList<Answer> clean(Date now){
		ArrayList<Answer> buffer = new ArrayList<Answer>();
		for (int i=0; i<outputList.size(); i++){
			if(now.after(outputList.get(i).getTimestamp())) {
				if (outputList.get(i).getDest()>1){// && outputList.get(i).getHTTPStatus()==411){
					System.out.println(outputList.get(i).toString());
				}
				buffer.add(outputList.remove(i));
			}else{
				return buffer;
			}
		}
		return buffer;
	}
	
	/**
	 * Clean output buffer with no condition.
	 * Pops all the anwers.
	 * @return output buffer.
	 */
	ArrayList<Answer> close(){
		ArrayList<Answer> buffer = new ArrayList<Answer>();
		for (int i=0; i<outputList.size(); i++){
			if (outputList.get(i).getDest()>1){
				System.out.println(outputList.get(i).toString());
			buffer.add(outputList.remove(i));
			}
		}
		return buffer;
	}
	
	/**
	 * Compares Request with Answer fiels dependening on their HTTPStatus.
	 * @param request Request to compare.
	 * @param answer Answer to compare.
	 * @return Boolean, result of the match.
	 */
	private boolean match(Request request, Answer answer){
		if (request.getHTTPStatus()==answer.getHTTPStatus()){
			switch (request.getHTTPStatus()){
			    case 304:
					return true;
			    //case 0:
					//return request.getRequest()==answer.getRequest();
			    case 400: case 401: case 403: case 404: case 405: case 411: case 416: case 500: case 501: case 502: case 503: case 504: 
					return request.getAnsLength()==answer.getAnsLength();
				default:
					return request.getRequest()==answer.getRequest() && request.getAnsLength()==answer.getAnsLength();
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Add Request to output queue.
	 * Tries to match the request with existing anwers. If no match creates a new answer and queues it.
	 * @param request Request to queue.
	 * @return Boolean, result of queueing, false if error.
	 */
	boolean queue(Request request){
		for (int i=0; i<outputList.size(); i++){
			if (match(request, outputList.get(i))){
				return outputList.get(i).addID(request);
			}
		}
		return outputList.add(new Answer(request));
	}

	/**
	 * Generate new object Date with slotTime added to it. 
	 * @param oldDate Date to add slotTime.
	 * @return Date, new Date with slotTime added.
	 */
	static Date addSlot(Date oldDate){
		Calendar cal =Calendar.getInstance();
		cal.setTime(oldDate);
		cal.add(Calendar.SECOND,slot); // add slot time
		return cal.getTime();
	}
	
	/**
	 * OutputList.size() getter.
	 * @return int, outputList.size().
	 */
	int outputListLength(){
		return outputList.size();
	}

}
