package br.com.ivogoncalves.ms_credit_appraiser.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ivogoncalves.ms_credit_appraiser.domain.CustomerSituation;

@RestController
@RequestMapping("/api/credit-appraiser")
public class CreditAppraiserController {
	
	@Autowired
	private CreditAppraiserService crediAppraiserService;

	@GetMapping
	public String getStatus() {
		return "Status Credit Appraiser OK";
	}
	
	@GetMapping(path= "/customer-status",params = "cpf")
	public ResponseEntity<CustomerSituation> customerStatusQuery(@RequestParam String cpf) {
		CustomerSituation cs = crediAppraiserService.getCustomerSituation(cpf);
		return ResponseEntity.ok(cs);
	}
}
