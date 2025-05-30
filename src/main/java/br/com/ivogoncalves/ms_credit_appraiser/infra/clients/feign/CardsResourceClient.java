package br.com.ivogoncalves.ms_credit_appraiser.infra.clients.feign;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ivogoncalves.ms_credit_appraiser.domain.Card;
import br.com.ivogoncalves.ms_credit_appraiser.domain.CustomerCard;

@FeignClient(name = "ms-cards", path = "/api/cards")
public interface CardsResourceClient {

	@GetMapping(params = "cpf")
	ResponseEntity<List<CustomerCard>> getCardsByCustomer(@RequestParam String cpf);
	
	@GetMapping(params = "income")
	ResponseEntity<List<Card>> getAllCardsWithIncomeLessThanEqual(@RequestParam BigDecimal income); 
}
