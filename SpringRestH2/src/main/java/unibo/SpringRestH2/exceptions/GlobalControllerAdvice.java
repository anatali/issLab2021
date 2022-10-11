package unibo.SpringRestH2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import unibo.SpringRestH2.controller.CategoryApi;

@ControllerAdvice(assignableTypes = CategoryApi.class)
public class GlobalControllerAdvice {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handle(Exception ex) {
        String msg = ex.getMessage();
        return new ResponseEntity<String>( msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
