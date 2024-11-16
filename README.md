# F5 - 분산 락 프로젝트

## 프로젝트 세팅
1. 로컬에 MySQL과 Redis를 설치하고 실행시킵니다. (Docker 사용 가능)
2. `/src/main/resources/db/initialize-schema.sql` 파일에 있는 SQL을 MySQL에 실행하여 초기 구조를 잡습니다.
3. `/src/main/resources/application.yml` 파일에 MySQL과 Redis의 정보를 입력합니다.
4. `F5LockApplication.kt` 파일을 실행시켜 프로젝트를 실행합니다.
5. 프로젝트를 실행시킨 후 브라우저에서 `http://localhost:8080/swagger-ui/` 로 접속하여 정상적으로 실행되는지 확인합니다. (주소 마지막에 `/`가 꼭 붙어있어야 합니다.)

## 초기 템플릿에 적혀있는 예시
1. **코틀린 문법** : `grammer` 패키지에 있는 파일을 보면 코틀린에서 동시성 관련 예약어를 어떻게 쓰는지 코드를 추가해두었습니다.
2. **레디스 사용법** : `redis` 패키지에 있는 파일을 보면 레디스를 어떻게 사용하는지 코드를 추가해두었습니다.
3. **MySQL 사용법** : `mysql` 패키지에 JdbcTemplate과 JPA로 MySQL을 이용하는 예시 코드를 추가해두었습니다.
4. **Swagger 사용법** : 문서화는 필수적이므로 `swagger` 패키지에 swagger 설정 코드가 들어있습니다.
5. **기본적인 API 구조** : `application` 패키지에 API를 만드는데에 필요한 기본 코드를 작성해두었습니다.

## 템플릿 코드를 읽을 때 유의해야할 점
어노테이션으로 설정된 부분이 꽤나 많습니다. 꼭 어떤 클래스에 어떤 어노테이션이 붙어있는지 확인해주세요. 특히 Config 관련 어노테이션은 꼭 어떤 일이 벌어지는지 알고 써야합니다. 


## Lint로 코딩 컨벤션 맞추기
코딩 컨벤션을 맞추기 위해 ktlint를 사용합니다. 아래 명령어를 CLI로 실행해주세요.

### IntelliJ에 린트 적용하기
`$ ./gradlew ktlintApplyToIdea`

### 커밋시 자동으로 린트 적용하기
`$ git config core.hooksPath .githooks`

### 명령어로 린트 자동으로 맞추기
`$ ./gradlew ktlintFormat`
