package pet.park.controller.error;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalControllerError {
private enum LogStatus{
	STACK_TRACE, MESSAGE_ONLY
}

@Data
private class ExceptionMessage{
	private String message;
	private String statusReason; // shows the status of the server
	private int statusCode;
	private String timestamp;
	private String uri;
}

// this method error handles DELETE all contributor operation
@ExceptionHandler(UnsupportedOperationException.class)
@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public ExceptionMessage handleUnsuportedOperationException(UnsupportedOperationException ex, WebRequest webRequest) {
	return buildExceptionMessage(ex, HttpStatus.METHOD_NOT_ALLOWED, webRequest,  LogStatus.MESSAGE_ONLY);
	 // message only = log without a stack trace
}

@ExceptionHandler(NoSuchElementException.class)
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public ExceptionMessage handleNoSuchElementException(NoSuchElementException ex, WebRequest webRequest) { // exception msg, show URI
	return buildExceptionMessage(ex, HttpStatus.NOT_FOUND, webRequest, LogStatus.MESSAGE_ONLY); // return exception, 404 status that we want, and log the msg to user
}

@ExceptionHandler(DuplicateKeyException.class)
@ResponseStatus(code = HttpStatus.CONFLICT)
// handler method checks for duplicates
public ExceptionMessage handleDuplicateKeyException(DuplicateKeyException ex, WebRequest webRequest) {
	return buildExceptionMessage(ex, HttpStatus.CONFLICT, webRequest, 
			LogStatus.MESSAGE_ONLY); // exception wanted, err code for dup keys = 409
}

// returns a 500 server error
// handler for Exception
@ExceptionHandler(Exception.class)
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public ExceptionMessage handleException(Exception ex, WebRequest webRequest) {
	return buildExceptionMessage(ex, HttpStatus.INTERNAL_SERVER_ERROR, webRequest, LogStatus.STACK_TRACE); // this is an unexpected exception, so log stack trace
}

// method that builds exception msg
private ExceptionMessage buildExceptionMessage(Exception ex, HttpStatus status, WebRequest webRequest,
		LogStatus logStatus) {
	String message = ex.toString();
	String statusReason = status.getReasonPhrase();
	int statusCode = status.value();
	String uri = null;
	String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
	
	
	// URI from web req object, Servlet web req
	if(webRequest instanceof ServletWebRequest swr) {
		uri = swr.getRequest().getRequestURI();
	}
	
	if(logStatus == LogStatus.MESSAGE_ONLY) {
		log.error("Exception: {}", ex.toString());
	}else {
		log.error("Exception: ", ex);
	}
	ExceptionMessage excMsg = new ExceptionMessage();
	
	excMsg.setMessage(message);
	excMsg.setStatusCode(statusCode);
	excMsg.setStatusReason(statusReason);
	excMsg.setTimestamp(timestamp);
	excMsg.setUri(uri);
	
	return excMsg;
}

}
