package br.com.ivogoncalves.ms_credit_appraiser.domain.exceptions;

public class CardRequestErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CardRequestErrorException(String message) {
		super(message);
	}
}
