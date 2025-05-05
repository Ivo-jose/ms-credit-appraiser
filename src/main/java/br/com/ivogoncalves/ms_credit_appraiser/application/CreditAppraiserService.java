package br.com.ivogoncalves.ms_credit_appraiser.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ivogoncalves.ms_credit_appraiser.domain.CustomerSituation;
import br.com.ivogoncalves.ms_credit_appraiser.infra.clients.CardsResourceClient;
import br.com.ivogoncalves.ms_credit_appraiser.infra.clients.CustomerResourceClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreditAppraiserService {
	
	@Autowired
	private CustomerResourceClient customerResourceClient;
	@Autowired
	private CardsResourceClient cardsResourceClient;
	
	public CustomerSituation getCustomerSituation(String cpf) {
		log.info("Finding using OPENFEIGN customer situation by CPF: {}", cpf);
		var responseCustomer = customerResourceClient.findCustomerData(cpf);
		var responseCards = cardsResourceClient.getCardsByCustomer(cpf);
		return CustomerSituation.builder()
				.customer(responseCustomer.getBody())
				.cards(responseCards.getBody())
				.build();
	}
}
