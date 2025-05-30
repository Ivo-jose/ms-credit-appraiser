package br.com.ivogoncalves.ms_credit_appraiser.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.ivogoncalves.ms_credit_appraiser.domain.ApprovedCard;
import br.com.ivogoncalves.ms_credit_appraiser.domain.CardIssuanceRequestData;
import br.com.ivogoncalves.ms_credit_appraiser.domain.CardRequestProtocol;
import br.com.ivogoncalves.ms_credit_appraiser.domain.CustomerEvaluation;
import br.com.ivogoncalves.ms_credit_appraiser.domain.CustomerSituation;
import br.com.ivogoncalves.ms_credit_appraiser.domain.exceptions.CardRequestErrorException;
import br.com.ivogoncalves.ms_credit_appraiser.domain.exceptions.MicroservicesCommunicationError;
import br.com.ivogoncalves.ms_credit_appraiser.domain.exceptions.ResourceNotFoundException;
import br.com.ivogoncalves.ms_credit_appraiser.infra.clients.feign.CardsResourceClient;
import br.com.ivogoncalves.ms_credit_appraiser.infra.clients.feign.CustomerResourceClient;
import br.com.ivogoncalves.ms_credit_appraiser.infra.clients.mqueue.CardIssuanceProducer;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditAppraiserService {
	
	private final CustomerResourceClient customerResourceClient;
	private final CardsResourceClient cardsResourceClient;
	private final CardIssuanceProducer cardIssuanceProducer;  
	
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
	
	public CardRequestProtocol requestCardIssuance(CardIssuanceRequestData data) {
		try {
			cardIssuanceProducer.requestCard(data);
			var protocol = UUID.randomUUID().toString();
			return new CardRequestProtocol(protocol);
		}
		catch (Exception e) {
			throw new CardRequestErrorException(e.getMessage());
		}
	}
}
