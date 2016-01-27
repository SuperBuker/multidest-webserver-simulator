package es.stuker.buker.ipv6multidest.webserver;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Webserver controller.
 * Defines the scanner and multiplexer threads and the exchange buffer between them.
 * Contains main execution method.
 * @author Jorge D'iez de la Fuente
 *
 */
public class Controller {
	// Should work with 5-10 elements
	static LinkedBlockingQueue<Request> buffer = new LinkedBlockingQueue<Request>(100);

	/**
	 * Main method of the simulator, setup and start scanner and multiplexer threads.
	 * @param args No params by the moment.
	 */
	public static void main(String[] args) {
		RunnableScanner runnableScanner = new RunnableScanner(buffer,
				"please set log file");
		RunnableMultiplexer runnableMultiplexer = new RunnableMultiplexer(buffer);

		new Thread(runnableScanner).start();
		new Thread(runnableMultiplexer).start();
	}

	/**
	 * Runnable Scanner Thread.
	 * Contains exchange buffer and Scanner object.
	 * In a loop, asks for new request to scanner, then puts it in the exchange buffer.
	 * Discards HTTP Status Code 408 and 499 and prints process and buffer status during execution.
	 * When receives a request with ID = -1 stops execution and closes scanner.
	 * @author Jorge D'iez de la Fuente
	 *
	 */
	static private class RunnableScanner implements Runnable {

		private LinkedBlockingQueue<Request> buffer;
		Scanner scanner;

		public RunnableScanner(LinkedBlockingQueue<Request> buffer, String filepath) {
			this.buffer = buffer;
			this.scanner = new Scanner(filepath);
		}

		public void run() {
			Request request;
			do {
				request = scanner.getRequest();
				if (request.getID() % 100000 == 0) {// Progress Indicator
					System.out.println("L: " + request.getID() + " BE: " + buffer.toArray().length);
				}
				// Timeout or Internal error
				if (request.getHTTPStatus() == 408 || request.getHTTPStatus() == 499) {
					continue; // get out
				}
				try {
					buffer.put(request);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// ID = -1 signals End of File
			} while (request.getID() != -1);
			scanner.close();
		}
	}

	/**
	 * Runnable Multiplexer Thread.
	 * Contains exchange buffer and Multiplexer object.
	 * In a loop, asks for new request to buffer, sets new date to multiplexer and receives multiplexed array, then pushes the new request in the multiplexer.
	 * Prints process and multiplexer status during execution.
	 * When receives a request with ID = -1 stops execution and closes multiplexer.
	 * Also counts multiplexions and prints results at the end.
	 * @author Jorge D'iez de la Fuente
	 *
	 */
	static private class RunnableMultiplexer implements Runnable {

		private LinkedBlockingQueue<Request> buffer;
		private Multiplexer multiplexer = new Multiplexer();
		private Map<Integer, Integer[]> multiplexed = new HashMap<Integer, Integer[]>();

		public RunnableMultiplexer(LinkedBlockingQueue<Request> buffer) {
			this.buffer = buffer;
		}

		public void run() {
			Request request;
			int maxOutputList = 0;
			try {
				while ((request = buffer.take()).getID() != -1) {// ID = -1 signals End of File
					if (request.getID() % 100000 == 0) {// Progress Indicator
						System.out.println("L: " + request.getID() + " OL: " + multiplexer.outputListLength());
					}
					ArrayList<Answer> sent = multiplexer.clean(request.getTimestamp());
					count(sent);
					multiplexer.queue(request);
					if (multiplexer.outputListLength() > maxOutputList) {
						maxOutputList = multiplexer.outputListLength();
						//System.out.println("L: " + request.getID() + " List Length: " + maxOutputList);
					}
				}
				// Closing thread
				ArrayList<Answer> sent = multiplexer.close();
				count(sent);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\n\n\tResults:");

			SortedSet<Integer> keys = new TreeSet<Integer>(multiplexed.keySet());
			for (Integer key: keys){
				System.out.println("HTTPStatusCode: " + key + " Multiplexions: " + multiplexed.get(key)[0]+ " Packets Multiplexed: " + multiplexed.get(key)[1]);
			}
			System.out.println("Max List Length: " + maxOutputList);
		}

		public void count(ArrayList<Answer> sent) {
			for (int i=0; i<sent.size(); i++){
				if (sent.get(i).getDest()>1){
					Integer[] count = multiplexed.get(sent.get(i).getHTTPStatus());
					if (count == null) {
						multiplexed.put(sent.get(i).getHTTPStatus(), new Integer[]{1,sent.get(i).getDest()});
					}
					else {
						multiplexed.put(sent.get(i).getHTTPStatus(), new Integer[]{count[0] + 1, count[1]+ sent.get(i).getDest()});
					}
				}
			}
		}
	}
}
