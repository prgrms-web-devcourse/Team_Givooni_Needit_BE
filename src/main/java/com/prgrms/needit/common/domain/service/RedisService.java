package com.prgrms.needit.common.domain.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, String> redisTemplate;

	public String getData(String receiver) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations.get(receiver);
	}

	public void setDataExpire(String receiver, String code) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		Duration expireDuration = Duration.ofMillis(180000);
		valueOperations.set(receiver, code, expireDuration);
	}

	public void deleteData(String receiver) {
		redisTemplate.delete(receiver);
	}

}