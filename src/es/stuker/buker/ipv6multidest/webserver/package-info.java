/**
 * Webserver Multidest simulator.
 * This software loads a file with Apache or Nginx log format, and tries to answer multiple request in a single packet,
 * matching the ones with same results as would do a Multidest Web Server.
 * This software generates a report at the end of the execution, showing the amount of multiplexions and original packets for each HTTP Status Code.
 * @author Jorge D'iez de la Fuente
 */
package es.stuker.buker.ipv6multidest.webserver;