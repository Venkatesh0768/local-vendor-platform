package org.localvendor.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFoundException(EmailNotFoundException exception , WebRequest request){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                exception.getMessage(),
                request.getDescription(false).replace("uri=" , "")

        );

        return new ResponseEntity<>(error , HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistException(EmailAlreadyExistException exception , WebRequest request){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "This Bad Request",
                exception.getMessage(),
                request.getDescription(false).replace("uri=" , "")

        );

        return new ResponseEntity<>(error , HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendingException(EmailSendingException ex , WebRequest request){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed To Send the Email or Otp",
                ex.getMessage(),
                request.getDescription(false).replace("uri=" , "")
        );
        return  new ResponseEntity<>(error , HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex , WebRequest request){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
              "User not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=" , "")
        );
        return  new ResponseEntity<>(error , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyVerifiedException(EmailAlreadyVerifiedException ex , WebRequest request){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Email Already Verified ",
                ex.getMessage(),
                request.getDescription(false).replace("uri=" , "")
        );
        return  new ResponseEntity<>(error , HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOtpException(InvalidOtpException ex , WebRequest request){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Otp Is Invalid ",
                ex.getMessage(),
                request.getDescription(false).replace("uri=" , "")
        );
        return  new ResponseEntity<>(error , HttpStatus.BAD_REQUEST);

    }
}
