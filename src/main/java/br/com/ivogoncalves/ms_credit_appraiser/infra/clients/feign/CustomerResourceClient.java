package br.com.ivogoncalves.ms_credit_appraiser.infra.clients.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ivogoncalves.ms_credit_appraiser.domain.CustomerData;

@FeignClient(name = "ms-customer", path = "/api/customers")
public interface CustomerResourceClient {

	@GetMapping(params = "cpf")
	ResponseEntity<CustomerData> findCustomerData(@RequestParam String cpf);
	
}
