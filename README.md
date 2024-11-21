# SPRING PLUS

## Level 1
### 필수기능 1~5번
1. **@Transactional 의 이해**
- 전역적 트랜잭션에서 `ReadOnly` 로 되어있었기 때문에 `saveTodo 메서드`는 Transactional 따로 설정

2. **코드 추가 퀴즈 - JWT 의 이해**
- 닉네임 정보를 요청 및 반환할 수 있도록 `Request`, `Entity`, `Controller`, `Service` 부분 수정

3. **코드 개선 퀴즈 - AOP 의 이해**
- AOP 가 메서드 실행 전 동작할 수 있게 어노테이션 수정 및 올바른 메서드에 지정할 수 있도록 경로 수정

4. **테스트 코드 퀴즈 - 컨트롤러 테스트의 이해**
- 테스트 코드가 의도에 맞게 수행되서 성공할 수 있도록 테스트 코드 수정

5. **코드 개선 퀴즈 - JPA 의 이해**
- 적당한 길이의 메서드 명에 `@Query` 어노테이션을 사용해서 날씨와 수정일 기준으로 조회하는 로직 추가

## Level 2
### 필수기능 6~9번
6. **JPA Cascade**
- `PERSIST` 옵션을 지정하여 Todo 가 영속화 될 때, `managers` 도 함께 영속화 되도록 함
- `REMOVE` 옵션을 지정 하여 삭제될 때 함께 삭제될 수 있도록 함

7. **N+1**
- Comment Entity 를 todoId로 조회한 후 Comment 의 User 정보를 가져오기 때문에 Comment 갯수만큼 User Entity 를 조회하는 상황이었음
- JPA 쿼리 부분을 FETCH 를 추가하여 Comment 와 User 를 동시에 조회하는 JPQL 쿼리로 수정 후 해결

8. **QueryDSL**
- QueryDSL 을 위해 Configuration 설정
- QueryDSL 작성을 위해 커스텀 Repository 작성
- JPA Repository 에 QueryDSL 포함 Repository 를 확장
- QueryDSL 사용 및 N+1 문제 발생 않는것을 확인완료

9. **Spring Security**
- Spring Security 의존성 추가
- Spring Security 로 필요없어진 `Filter`, `Argument Resolver` 주석처리
- Spring Security 세팅을 위한 `UserDetails`, `UserDetailsService` 구현체 작성
- Spring Security 기반 인증 및 인가 처리 `Filter` 추가
- 인증, 인가 필터 순서 설정을 위한 `SecurityConfig` 클래스 작성
- Todo Controller 의 Todo 작성 메서드에 Spring Security 적용
- `@Auth` 어노테이션으로 로그인 인증인가된 유저를 받던 부분을 Spring Security 적용 후 `@AuthenticationPrincipal` 으로 `UserDetails` 를 불러와 데이터를 처리하도록 전체 수정

## Level 3
### 도전기능 10~13번

10. **QueryDSL 을 사용하여 검색기능 만들기**
- 일정 전체 조회하는 기능 향상된 버전의 API 추가
- 제목의 일부만 포함하여도 검색가능
- 담당자의 닉네임 일부로 검색가능
- 생성일 최신순 정렬 검색가능
- 반환은 일정제목, 담당자 수, 댓글 갯수만 반환(DTO 추가)
- 결과는 모두 페이징 처리

11. **Transaction 심화**
- Log 엔티티를 추가하여 매니저 등록 행위에 대한 로그를 기록
- `Transactional` 의 `REQUIRES_NEW` 키워드를 통해 `saveManager` 메서드와 개별적인 트랜잭션으로 로그기록
- 로그에는 로그메세지, 생성시간, 요청 ID 등을 기록

12. **AWS 활용 마스터**
- EC2 인스턴스에서 어플리케이션 실행(Java 17 설치 후 파일질라로 전송 및 java -jar 커멘드)
- `탄력적 IP` 설정해서 인스턴스 껐다 켜도 퍼블릭 IP 고정
- /auth 경로에 Health Check API 설정하여 서버상태 누구나 조회가능
- - [GET] /auth/health
- RDS DB 생성 후 `properties` 에 정보 넣고 연동 후 데이터 들어가는 것 확인
- S3 의존성 추가 및 S3 Config 생성하여 S3 연결, 이미지 JPEG, PNG 파일을 2MB 이내로 제한을 거는 로직으로 생성
- EC2 인스턴스 설정
![EC2](https://github.com/user-attachments/assets/0a2d9f8f-a801-42ef-9a1a-ad071bfe3c24)
![2인스턴스의 보안 인바운드규칙](https://github.com/user-attachments/assets/159a797f-c64c-453a-a0b1-e2fb3d4f25b9)
![3탄력적 IP 할당](https://github.com/user-attachments/assets/ab57d3e0-620e-4972-b15e-fe02c0c669c6)
![4health check](https://github.com/user-attachments/assets/f7c0bc0e-9e2b-46b8-b019-cd6be3ab9273)
- RDS 설정
![5RDS](https://github.com/user-attachments/assets/5a573549-3db0-45b2-8399-0d2b6e3b7854)
![6RDS 보안그룹 규칙과 연결된 인스턴스](https://github.com/user-attachments/assets/532cad66-6faa-404c-84e0-85485d82a142)
- S3 설정
![7S3연결후 사진업로드 성공](https://github.com/user-attachments/assets/6e30c4d5-52f9-4660-9f2b-68cc4eb0ce36)
![8포스트맨요청 후 S3에 업로드](https://github.com/user-attachments/assets/6968a7be-091b-4385-a659-282682ee0cba)


13. **대용량 데이터 처리**
- 테스트 코드에서 유저데이터 100만건 생성코드 작성
- UUID와 Set 을 사용해서 중복되지않은 닉네임 생성
- 닉네임을 조건으로 유저 목록 검색하는 API 추가
- 테스트코드는 1개의 Insert 쿼리에 배치 사이즈 1000 으로 한번에 1000명분의 데이터를 보냄
- 기존 방식은 전체 테이블을 조회해서 하는 방식 검색시간

![인덱스 적용전 데이터 생산 및 조회 시간](https://github.com/user-attachments/assets/8b977a03-1475-4ee2-89a4-5d387437825e)

- 인덱스 적용 후 조회하는 시간 단축

![인덱스 적용후 생성 및 조회시간2](https://github.com/user-attachments/assets/b849acb8-eab2-4b4f-8d9d-1aa99cbfb2ed)

## Level 4
### 도전기능 14번
14. **Entity 및 Repository CRUD 리팩토링(Kotlin)**
- 자바로 작성되어 있던 Entity 코틀린 문법으로 리팩토링
- Nullable 과 Non-nullable 을 구분하여 필드선언
- 기본생성자와 copy 메서드를 활용한 수정생성자 추가
- Service 로직 Kotlin 문법으로 변경

- TodoService 에 있는 모든 로직을 변경하지는 못했음
