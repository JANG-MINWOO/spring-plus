package org.example.expert.domain.log.service;

import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {
	private final LogRepository logRepository;

	//이 메서드는 옵션에 의해 다른 트랜잭션에 들어가도 별도의 트랜잭션으로 흐름
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveLog(Long requestUserId, String message) {
		Log log = new Log(requestUserId, message);
		log.setRequestUserId(requestUserId);
		log.setMessage(message);
		logRepository.save(log);
	}
}
