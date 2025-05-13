package br.com.ivogoncalves.ms_credit_appraiser.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationData {
	
	private String cpf;
	private BigDecimal income;
}
