package br.com.ivogoncalves.ms_credit_appraiser.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MicroservicesCommunicationError extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MicroservicesCommunicationError(String message, int code) {
		super(message + " -- Status: " + code);
	}
}
