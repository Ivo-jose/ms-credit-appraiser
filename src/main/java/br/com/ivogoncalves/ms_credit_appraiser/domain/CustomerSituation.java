package br.com.ivogoncalves.ms_credit_appraiser.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSituation {

	private CustomerData customer;
	private List<CustomerCard> cards;
}
