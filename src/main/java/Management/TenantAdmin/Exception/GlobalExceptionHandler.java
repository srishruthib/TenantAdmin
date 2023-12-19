package Management.TenantAdmin.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {

		e.printStackTrace();
		String errorMessage = "An error occurred: " + e.getMessage();
		HttpStatus httpStatus = determineHttpStatus(e);

		return new ResponseEntity<>(errorMessage, httpStatus);
	}

	private HttpStatus determineHttpStatus(Exception e) {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
