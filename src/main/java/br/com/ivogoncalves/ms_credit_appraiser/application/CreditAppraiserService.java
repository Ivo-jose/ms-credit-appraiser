package br.com.ivogoncalves.ms_credit_appraiser.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.ivogoncalves.ms_credit_appraiser.domain.ApprovedCard;
import br.com.ivogoncalves.ms_credit_appraiser.domain.CustomerEvaluation;
import br.com.ivogoncalves.ms_credit_appraiser.domain.CustomerSituation;
import br.com.ivogoncalves.ms_credit_appraiser.domain.exceptions.MicroservicesCommunicationError;
import br.com.ivogoncalves.ms_credit_appraiser.domain.exceptions.ResourceNotFoundException;
import br.com.ivogoncalves.ms_credit_appraiser.infra.clients.CardsResourceClient;
import br.com.ivogoncalves.ms_credit_appraiser.infra.clients.CustomerResourceClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreditAppraiserService {
	
	@Autowired
	private CustomerResourceClient customerResourceClient;
	@Autowired
	private CardsResourceClient cardsResourceClient;
	
	/**
	 * This method is responsible for obtaining the customer situation by CPF.
	 * 
	 * @param cpf
	 * @return CustomerSituation
	 */
	public CustomerSituation getCustomerSituation(String cpf) {
		try {
			log.info("Finding using OPENFEIGN customer situation by CPF: {}", cpf);
			var responseCustomer = customerResourceClient.findCustomerData(cpf);
			var responseCards = cardsResourceClient.getCardsByCustomer(cpf);
			return CustomerSituation.builder()
					.customer(responseCustomer.getBody())
					.cards(responseCards.getBody())
					.build();
		}
		catch (FeignException.FeignClientException e) {
			if(HttpStatus.NOT_FOUND.value() == e.status()) throw new ResourceNotFoundException("Customer not found! CPF: " + cpf);
			throw new MicroservicesCommunicationError(e.getMessage(), e.status());
		}
	}
	
	
	public CustomerEvaluation performEvaluation(String cpf, BigDecimal income) {
		try {
			log.info("Carrying out Assessment by CPF:{} and Income:{}", cpf, income);
			var responseCustomer =  customerResourceClient.findCustomerData(cpf);
			var responseCards = cardsResourceClient.getAllCardsWithIncomeLessThanEqual(income);
			var cardsList = responseCards.getBody();
			log.info("Cards List: {}", cardsList);
			var list  = cardsList.stream().map(c -> {
				BigDecimal limit = c.getBasicLimit();
				BigDecimal divideAge = BigDecimal.valueOf(responseCustomer.getBody().getAge()).divide(BigDecimal.valueOf(10));
				BigDecimal cardLimit = limit.multiply(divideAge).setScale(3, RoundingMode.HALF_UP);
				return new ApprovedCard(c.getName(), c.getFlag(), cardLimit);
			}).collect(Collectors.toList());
			return new CustomerEvaluation(list);
		}
		catch (FeignException.FeignClientException e) {
			if(HttpStatus.NOT_FOUND.value() == e.status()) throw new ResourceNotFoundException("Customer not found! CPF: " + cpf);
			throw new MicroservicesCommunicationError(e.getMessage(), e.status());
		}
	}
}
