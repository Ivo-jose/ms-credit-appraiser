package br.com.ivogoncalves.ms_credit_appraiser.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCard {

	private String name;
	private String flag;
	private BigDecimal limit;
}
