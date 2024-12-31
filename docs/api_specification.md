### API 명세서

#### 토큰 발급 API
**Endpoint**
- ``POST /user/token``

**Description**  
유저가 서비스 이용시 사용할 토큰을 발급받는다. 해당 토큰은 이후 API 호출 시 대기열 검증에 사용된다.

**Response**
```
{
    "token": "UUID_queuePosition",
    "queuePosition": 123,
    "estimatedWaitTime": 300
}
```
- token: id와 대기순서를 포함
- queuePosition: 유저의 현재 대기열 순위.
- estimatedWaitTime: 대기 예상 시간 (초 단위).

#### 대기열 상태 확인 API
**Endpoint**
- GET ```/user/token/status```

**Description**  
현재 대기열에서 유저의 상태를 확인한다. 실시간 대기열 정보(순서, 잔여 시간 등)를 제공한다.

Request
```
{
    token : "UUID_queuePosition"
}
```
Response
```
{
"queuePosition": 123,
"estimatedWaitTime": 200,
}
```
- queuePosition: 유저의 현재 대기열 순위.
- estimatedWaitTime: 남은 대기 예상 시간 (초 단위).

---
```아래에서부터는 헤더에 대기순번 토큰 필요함```

#### 예약 가능 날짜 조회 API
**Endpoint**
- GET ```/concert/available-dates```

**Description**  
예약 가능한 날짜 목록을 조회합니다.

**Request**

**Response**
```
{
"availableDates": [
"2024-01-01",
"2024-01-02",
"2024-01-03"
]
}
```
availableDates: 예약 가능한 날짜 목록

#### 예약 가능한 좌석 조회 API
**Endpoint**
- GET ```/concert/available-seats```

**Description**
특정 날짜의 예약 가능한 좌석 정보를 조회합니다.

**Request**

```
{
    concertId : 1,
    concertDate : "YYYY-MM-DD"
}
```

- concertId : 콘서트 식별자

Response
```
{
"availableSeats": [1, 2, 3, 10, 20]
}
```
- availableSeats: 해당 날짜에 예약 가능한 좌석 번호 목록 (1 ~ 50 사이의 숫자).



#### 좌석 예약 요청 API
**Endpoint**
- POST ```/concert/reserve```

**Description**
특정 날짜와 좌석 번호를 입력받아 좌석을 임시 예약한다. 좌석은 5분(또는 설정된 시간) 동안 해당 유저에게 임시 배정되며, 배정 시간 내 결제가 완료되지 않으면 자동 해제된다.


**Request**

```
{
"userId": 1,
"concertId": 1,
"seatNumber": 12
}
```
- userId: 좌석을 예약하려는 사용자의 id
- concertId: 해당 콘서트 ID
- seatNumber: 예약 요청 좌석 번호 (1 ~ 50 범위의 정수)


Response

```
{
"concertDate": "YYYY-MM-DD",
"seatNumber": 12,
"status": "reserved",
"expireAt" : "2024-12-29 12:30:00"
}
```

- concertDate: 콘서트 예약 날짜.
- seatNumber: 요청한 좌석 번호.
- status: 현재 예약 상태
- expireAt: 임시 배정 만료 시간

#### 잔액 충전 API

**Endpoint**
- POST ``/payment/charge``

**Description**  
결제에 사용될 금액을 충전하는 API 이다.

**Request**
```
{
    userId : 1,
    amount : 30000
}
```

**Response**
```
{
"userId": 1,
"balance": 5000,
"message": "Balance charged successfully."
}
```
- userId: 충전된 사용자의 id
- balance: 충전 후 사용자 잔액
- message: 성공 메시지

#### 잔액 조회 API

**Endpoint**
- GET ``/payment/balance``

**Description**  
결제에 필요한 금액에 사용할 수 있는 잔액을 조회한다.

**Request**
```
{
    userId : 1
}
```

**Response**
```
{
    userId : 1,
    balance : 3000
}
```

#### 결제 API
**Endpoint**
- POST ```/payment```

**Description**
결제를 처리하고 결제 내역을 생성한다. 결제가 완료되면 좌석의 소유권을 해당 유저에게 배정한다.

**Request**
```
{
"userId": 1
}
```

**Response**
```
{
"userId" : 1
"status": "completed",
"message": "Payment successful and ownership assigned."
}
```