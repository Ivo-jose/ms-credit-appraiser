package br.com.ivogoncalves.ms_credit_appraiser.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * DataIntegrityViolationException
 * 
 * @author ivogoncalves
 * @since 2023-10-01
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataIntegrityViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataIntegrityViolationException(String message) {
		super(message);
	}
}
