package br.com.ivogoncalves.ms_credit_appraiser.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
}
