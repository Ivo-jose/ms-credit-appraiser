package br.com.ivogoncalves.ms_credit_appraiser.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
	
	private Long id;
	private String name;
	private String flag;
	private BigDecimal basicLimit;
}
