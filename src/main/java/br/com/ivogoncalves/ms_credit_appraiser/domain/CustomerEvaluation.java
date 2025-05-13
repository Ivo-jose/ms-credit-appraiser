package br.com.ivogoncalves.ms_credit_appraiser.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerEvaluation {

	private List<ApprovedCard> cards;
}
