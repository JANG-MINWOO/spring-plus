package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

	// 기본 닉네임으로 조회하는 JPA 쿼리
	Optional<User> findByNickname(String nickname);

	// 인덱스를 사용해서 닉네임을 조회
	@Query(value = "SELECT * FROM users USE INDEX (idx_nickname) WHERE nickname = :nickname", nativeQuery = true)
	Optional<User> findByNicknameUsingIndex(@Param("nickname") String nickname);

}
