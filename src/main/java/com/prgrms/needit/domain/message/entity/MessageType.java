package com.prgrms.needit.domain.message.entity;

import com.prgrms.needit.domain.message.controller.bind.MessageContractRequest;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import org.springframework.util.Assert;

public enum MessageType {
	CHAT(request ->
		Assert.hasText(request.getContent(),
					   "Message content cannot be null or empty.")
	),
	CONTRACT(request -> {
		Assert.notNull(
			request.getContractDate(),
			"Contract date cannot be null."
		);
		Assert.isTrue(
			request.getContractDate()
				   .isAfter(LocalDateTime.now()),
			"Contract date cannot be past."
		);
	});

	private final Consumer<MessageContractRequest> validator;

	public void validate(MessageContractRequest request) {
		validator.accept(request);
	}

	MessageType(Consumer<MessageContractRequest> validator) {
		this.validator = validator;
	}
}
