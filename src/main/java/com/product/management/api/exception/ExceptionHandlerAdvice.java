package com.product.management.api.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.product.management.api.model.CustomApiResponse;

@RestControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomApiResponse> handleException(HttpServletResponse response, CustomException expObj) throws IOException {
		return new ResponseEntity<>(new CustomApiResponse().setMessage(expObj.getMessage()).setStatusCode(expObj.getHttpStatus()), expObj.getHttpStatus());
	}
}
