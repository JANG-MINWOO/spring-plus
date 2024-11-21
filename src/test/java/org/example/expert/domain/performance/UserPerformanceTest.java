package org.example.expert.domain.performance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
public class UserPerformanceTest {

	@Autowired
	private EntityManager entityManager;


	@Autowired
	private MockMvc mvc;

	private String savedNickname;

	@Test
	@Transactional
	void createAndSearchUser() throws Exception {
		Instant Start = Instant.now();

		createBatchUsers_million();
		Instant End = Instant.now();
		System.out.println("100만개 유저데이터 생성 시간: "+ java.time.Duration.between(Start, End)+ "ms");

		searchUserByNickname_in_million_data();

		Instant End2 = Instant.now();
		System.out.println("유저 검색 시간: "+ java.time.Duration.between(End, End2)+ "ms");
	}

	@Transactional
	void createBatchUsers_million() {
		int totalUsers = 1_000_000;  // 생성할 총 유저 수
		int batchSize = 1_000;  // 한 번에 처리할 배치 크기

		StringBuilder sql = new StringBuilder("INSERT INTO users (email, password, user_role, nickname) VALUES ");
		Set<String> emailSet = new HashSet<>();  // 이메일 중복 체크용
		Set<String> nicknameSet = new HashSet<>();  // 닉네임 중복 체크용

		int middleUserIndex = 900_000; //원하는 생성된유저의 번째수를 선택
		String middleNickname = null;

		for (int i = 0; i < totalUsers; i++) {
			String email = generateUniqueEmail(emailSet);
			String nickname = generateUniqueNickname(nicknameSet);

			if(i == middleUserIndex) {
				middleNickname = nickname;
			}

			String role = ThreadLocalRandom.current().nextBoolean() ? "ADMIN" : "USER";

			// VALUES 부분을 생성
			sql.append("(")
				.append("'").append(email).append("', ")
				.append("'password123', ")
				.append("'").append(role).append("', ")
				.append("'").append(nickname).append("'), ");

			// 배치 크기마다 한 번에 INSERT 문을 실행
			if ((i + 1) % batchSize == 0 || i == totalUsers - 1) {
				// 마지막 값 뒤에 쉼표가 있으면 제거
				if (sql.charAt(sql.length() - 2) == ',') {
					sql.setLength(sql.length() - 2);  // 마지막 쉼표 제거
				}
				entityManager.createNativeQuery(sql.toString()).executeUpdate();
				sql.setLength(0);  // SQL 초기화
				sql.append("INSERT INTO users (email, password, user_role, nickname) VALUES ");
				entityManager.flush();
				entityManager.clear();  // 캐시를 비워 충돌 방지
			}
		}
		savedNickname = middleNickname;
	}

	void searchUserByNickname_in_million_data() throws Exception {
		if(savedNickname == null) {
			throw new Exception("닉네임이 생성되지 않았습니다.");
		}

		entityManager.clear();

		mvc.perform(get("/users/search-by-nickname")
			.param("nickname", savedNickname)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nickname").value(savedNickname));
	}


	// 랜덤 이메일 생성
	private String generateRandomEmail() {
		return UUID.randomUUID().toString().substring(0, 8) + "@example.com";
	}

	// 랜덤 닉네임 생성
	private String generateRandomNickname() {
		return "User_" + UUID.randomUUID().toString().substring(0, 8);
	}

	private String generateUniqueEmail(Set<String> emailSet) {
		String email;
		do {
			email = generateRandomEmail();
		} while (!emailSet.add(email));
		return email;
	}

	private String generateUniqueNickname(Set<String> nicknameSet) {
		String nickname;
		do {
			nickname = generateRandomNickname();
		} while (!nicknameSet.add(nickname));
		return nickname;
	}
}
