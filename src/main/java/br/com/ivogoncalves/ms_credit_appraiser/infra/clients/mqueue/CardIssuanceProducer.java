package br.com.ivogoncalves.ms_credit_appraiser.infra.clients.mqueue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ivogoncalves.ms_credit_appraiser.domain.CardIssuanceRequestData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardIssuanceProducer {

	private final RabbitTemplate rabbitTemplate;
	private final Queue queueCardIssuance;
	
	public void requestCard(CardIssuanceRequestData payload) throws JsonProcessingException {
		var json = convertToJson(payload);
		log.debug("Payload: {}", json);
		log.info("Sending card issuance request to queue: {}", queueCardIssuance.getName());
		rabbitTemplate.convertAndSend(queueCardIssuance.getName(), json);
	}
	
	private String convertToJson(CardIssuanceRequestData payload)  throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}
}
