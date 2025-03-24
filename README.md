# 13friday 물류 센터

## 프로젝트 개요
### 프로젝트 목적
#### MSA 기반 극대 물류 관리 및 배송 시스템 개발
이 프로젝트는 B2B 물류 관리 및 배송 시스템입니다. 각 지역에 허브 센터가  있고, 도매 업체가 필요한 물품을 물류센터로 주문을 하면 가공업체에서 생산된 물품이 저장되어 있는 허브에서 가공업체까지 배송을 합니다.

## 팀원 소개 및 역할
| 역할           | 담당자       | 설명                                              |
|----------------|--------------|---------------------------------------------------|
| **팀장** | 박동휘       | 주문 및 배송 서비스 API 설계 및 개발 |
| **팀원**  | 서현재       | jwt와 security 를 활용한 인증 및 사용자와 배송담당자 서비스 API 설계 및 개발                |
| **팀원** | 진강훈      | OPEN API를 활용한 허브와 허브간 이동경로 서비스 API 설계 및 개발                   |
| **팀원** | 김지현       | 업체와 상품 서비스 API 설계 및 개발과 gemini를 활용한 슬랙메세지 API 설계 및 개발              |

## 서비스 구성 및 실행
마이크로서비스 아키텍쳐(MSA)를 기반으로 마이크로 서비스 환경을 구축하고, eureka server를 통해 API gateway로 각 마이크로 서비스를 호출합니다. 각 마이크로서비스를 Docker를 통해 이미지를 빌드하고, 하나의 컨테이너에서 실행 및 관리를 합니다.

### 각 서비스별 endpoint
API 명세서 ☞ [여기로](https://teamsparta.notion.site/API-1b32dc3ef51480f98398fee7ba8cb596 "API 명세서로 이동")

## 테이블 명세서 및 ERD
테이블 명세서 ☞ [여기로](https://teamsparta.notion.site/1b32dc3ef51480f78a3de63b4866bb54 "테이블 명세서로 이동")
![ERD](https://github.com/user-attachments/assets/ef20355a-8f16-4b0c-bc53-22acf69f98fb)
## Trouble Shooting

### JWT 인증 인가
#### 문제: JWT 인증인가를 어떻게 해야할까?
1. 여태껏 인증인가를 JWT를 통해서 SecurityContext를 만들고 이를 통해 내부 Security를 거쳐서 인증인가 처리를 해왔다.
2. 하지만 Spring Security는 하나의 어플리케이션 안에서만 동작하기 때문에 MSA환경에서 어떻게 적용해야 할지 고민이었다.
#### 해결과정
##### 고민했던 방안
멀티 모듈 방식으로 공통모듈을 통해 중앙에서 관리하는 방향으로 고민
##### 결과 
1. 공통모듈로 인해서 각 하위 모듈에서 이슈가 발생해 이전 spring cloud 방식으로 롤백
2. 실습 과정에서는 API gateway에서 인증인가를 처리하는 방식을 사용
3. 각 도메인 마다 Security를 설정하고 인증인가를 해야되는가 의문이 생김
#### 해결방법
1. 사용자 도메인에서 Security를 설정하고 Gateway에 Filter를 작성
2. Gateway에 있는 필터에서 인증을 한 후 Request에 header를 추가하여 현재사용자의 id와 role을 넘김
3. 각 도메인에서 requestHeader로 받은 role로 권한 처리

### DDD 도메인별 서비스 분리
문제
초기 설계에서는 주문 서비스에서 주문이 생성되면 배송 서비스와 배송 경로 서비스까지 순차적으로 호출하는 구조였음. 이로 인해 주문 서비스가 배송 및 배송 경로 생성까지 직접 제어하게 되었고 결과적으로 하나의 서비스에 과도한 비즈니스 책임이 주어졌다고 생각함. 또한 배송 경로 생성에는 허브 간 거리 및 예상 시간 정보가 필요한데 이를 위해 허브 서비스까지 호출하게 되면서 서비스 간 결합도가 과도하게 높아졌다고 판단.

해결 과정
이러한 구조적 문제를 해결하기 위해 서비스간 역할을 명확히 분리하기로 함.
주문 서비스는 배송 생성까지만 책임지고, 이후 배송이 생성되면 배송 서비스 내부에서 배송 경로를 자동으로 생성하도록 설계를 변경.
이로써 API 체인이 단순화되고 각 서비스가 자신의 책임만 수행할 수 있게 됨.

### MSA에서의 API 문서 자동화
#### 오류
swagger를 사용해 api 문서 자동화를 하고 gateway로 통합 문서를 볼 수 있게 하려 했지만  각각 마이크로 서비스에서는 sawgger ui 가 정상 동작하는데 api gateway 에서는 404 not found가 뜨는 문제 발생
#### 문제를 해결하기 위한 노력
각 마이크로 서비스와 gateway의 yml 파일에 swagger 설정을 해주었지만 동일한 오류가 지속적으로 발생
#### 문제 해결 
모놀리식 아키텍쳐일때는 단일 프로젝트였기 때문에 
swagger 의존성을 webmvc로 해도 1:1 로 요청하기때문에 문서 자동화에 문제가 없었지만 MSA는 여러 마이크로서비스가 있기 때문에 N:1 요청이 필요했다.
swagger 의존성을 webflux로 바꿔주면 gateway로 swagger 접근시 통합문서로 보여주게 된다.

## Technologies & Tools
### Technologies
| Java         | Spring boot         | Spring cloud         | JWT         | Spring security        | PostgreSQL         | JPA         | QueryDSL         |
|-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|
|<img src="https://github.com/user-attachments/assets/dc8f8162-2695-4d47-8fcd-a4d395026bdc" width="100" height="100">  | <img src="https://github.com/user-attachments/assets/223e3dc4-ed3d-4aa1-97a1-fe5d90caae6d" width="100" height="100"> | <img src="https://github.com/user-attachments/assets/bf6231b6-6bc2-4741-8e51-dbe928c98670" width="100" height="100">| <img src="https://github.com/user-attachments/assets/f3cae58b-77e4-4813-bb4a-40e6bfb5e26a" width="100" height="100">| <img src="https://github.com/user-attachments/assets/23012343-7981-40b4-9eda-100611a21276" width="100" height="100"> | <img src="https://github.com/user-attachments/assets/97fdd97e-09a8-4a34-bcbd-a6f1aaeaf1c1" width="100" height="100"> | <img src="https://github.com/user-attachments/assets/2b9b919e-e615-4b0d-9976-b459480a78ed" width="100" height="100"> | <img src="https://github.com/user-attachments/assets/6d666579-7b45-4f62-b735-1f750ede06de" width="100" height="100">|
### Tools
| Redis         | Docker         | Swagger         | Zipkin         | Git        | GitHub         | Slack         | Discord         |
|-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|-------------------|
|<img src="https://github.com/user-attachments/assets/3e8f2836-045f-4d1a-85f1-0cab931032ea" width="100" height="100"> |<img src="https://github.com/user-attachments/assets/69bfa62f-716f-4723-956d-70c8f5de15d7" width="100" height="100"> |<img src="https://github.com/user-attachments/assets/a8dcc1e7-5534-4c2e-ae67-635ec5515cea" width="100" height="100"> |<img src="https://github.com/user-attachments/assets/cab5979a-df95-481f-9d77-6b381ab61ef8" width="100" height="100"> |<img src="https://github.com/user-attachments/assets/88d6c4c8-39b7-4147-a9b8-cc0e88535ec4" width="100" height="100"> |<img src="https://github.com/user-attachments/assets/feb0d43f-823d-4ca1-a1dc-d0db3de63c67" width="100" height="100"> |<img src="https://github.com/user-attachments/assets/89d4adf1-a52f-4c6a-8d20-725d113d0569" width="100" height="100"> |<img src="https://github.com/user-attachments/assets/67ac6f95-434f-4c12-85cf-20ce6a2d0c14" width="100" height="100"> |
## API docs
docker compose 실행후 API gateway 통합 문서
http://localhost:19091/swagger-ui/webjars/swagger-ui/index.html
