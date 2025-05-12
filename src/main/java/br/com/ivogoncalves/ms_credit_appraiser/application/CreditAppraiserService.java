package br.com.ivogoncalves.ms_credit_appraiser.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
}
