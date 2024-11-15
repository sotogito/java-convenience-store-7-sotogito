# java-convenience-store-precourse

구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.

### 입력 값

- 상품 이름 - 가격
- Y/N - 여부

## 주요 기능

- 재고 관리 : 구매시 차감하고 결제 가능 여부를 확인한다.
- 프로모션 할인 : 프로모션 상품의 기간, 수량을 고려하여 프로모션에 따라 증정품을 제공한다.

## 세부 기능

### 재고 관리

- 결제 전 수량을 확인해 구매 가능 여부를 확인한다.
- 구매시 차감한다.

### 프로모션 할인

- 각 프로모션 기간이 존재한다.
- 동일 상품에 여러 프로모션이 적용되지 않는다 ex) 콜라 상품 1개에 탄산2+1, 반짝할인
- 종류는 3개이다.
    - 탄산2+1
    - MD추천상품
    - 반짝할인

- 프로모션 할인 적용 가능 시, 상품 수량 차감은 프로모션 상품 -> 일반 상품 순으로 한다.
- 고객은 프로모션 상품의 증정품 개수까지 가져와야한다.
    - 적게 가져올 경우 메시지로 안내한다.
        - ex) 콜라는 탄산2+1이기 때문에 2개 배수로 가져올 경우 여부를 물어본다.
- 상품 전체의 재고가 부족한 경우(프로모션+일반 = 재고없음), 적용 가능 범위에서만 할인한다.
    - 일부 수량에 대해 정가로 결제하게 됨을 안내한다.
        - ex) 콜라 구매 수량 5개, 2+1 3개는 프로모션 가능, 2개는 일반가격으로 결제

### 멤버십 할인

- 할인 가격 : (프로모션 금액이 아닌 상품 총 가격) * 30%
    - 프로모션 적용 가격이란 1,000원짜리 콜라를 2+1 적용하였을 때 (3*1,000) = 3,000원이다.
- 프로모션 할인 적용 가격에 추가하여 할인해준다.
- 최대 한도는 8,000원이다.

### 결과 출력, 영수증 출력

```
1. 구매 상품 내역: 구매한 상품명, 수량, 가격
2. 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
금액 정보
    3.  총구매액: 구매한 상품의 총 수량과 총 금액
    4. 행사할인: 프로모션에 의해 할인된 금액
    5. 멤버십할인: 멤버십에 의해 추가로 할인된 금액
    6. 내실돈: 최종 결제 금액
```

```
===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000
```

---

## 플로우

1. [상품 이름-수량]을 입력받는다.
2. 수량을 확인한다. - 프로모션+일반
3. 상품을 담는다. - 프로모션 기간이라면 우선 프로모션 상품으로 담는다.
4. 프로모션 수량을 맞게 가져왔는지 확인한다.
5. 프로모션 재고가 부족한지 확인한다.
4. 멤버십 할인 적용 여부를 입력받는다.
4. 영수증을 출력한다.
5. 추가 구매 여부를 입력받는다.

## 유효 검사

- 구매 상품 이름 및 수량 입력
    - 형식이 맞는가?` [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - 존재하는 상품인가? `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`
    - 재고가 있는가? `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`
    - 기타 오류 `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

---

## 주의할 점

- 현재 날짜를 구한다.
- 추가 구매 여부를 받을 수 있다. 프로그램이 종료될때 까지는 업데이트된 차감 수량을 유지한다.
- 재고가 0개인 경우 상품 출력 목록에 `재고 없음`을 출력한다.
- 모든 숫자는 천단위 콤마로 구분한다.
- .md 파일에는 수량이 0인 경우는 존재하지 않는다. -> 두 파일 모두 내용의 형식을 유지한다면 값은 수정할 수 있다

### 추가 프로그래밍 요구 사항

- 사용자가 잘못 입력할 경우 그 지점부터 다시 입력받는다.

---

# 구체화

## 구매 상품 입력받은 후 확인 순서

1. 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)

- 상품명
    - 구매할 수량이 프로모션 재고보다 많은가?
    - 구매 수량 = 프로모션 수량일 때, 프로모션 수량에서 적용 불가능한 수량이 있는가?
        - 감자칩 재고 : 5 -> [감자칩-5] -> 재고에서 3개만 적용 가능 -> 1개는 적용 불가능
- 수량
    - (전체 구매 수량 - 프로모션 재고에서 프로모션 적용 가능한 수량)
        - 전체 구매 : 12
        - 프로모션 상품 재고 : 5 -> 적용 가능 총 수량 -3
        - result = 12-3=9개 적용 불가

1. 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)

- 상품명
    - (구매 수량 - 프로모션 증정이 포함된 수량)이 0이 아닌가?
    - (구매 수량 - 프로모션 증정이 포함된 수량)이 프로모션 적용(buy)의 최소 수량이 되는가?
    - 프로모션 증정이 포함된 수량이 프로모션 재고를 넘어서지 않가?
        - 사이다 수량 : 5 -> [사이다-5] -> 재고 내에서 3개만 프로모션 적용 가능 -> 2개 일반으로
- 수량
    - 프로모션의 get 수량

#### 일반 상품

- 구매한 수량이 프로모션의 buy에 미치지 않는다면 일반 상품으로 분류한다.
    - [콜라-1]
- 프로모션의 수량이 0개라면 사용자는 일반상품으로 구매한 것으로 판단한다.
- 프로모션 기간에서 벗어나면 일반상품으로 분류한다.
- 만약 무료로 받을 수 있는 프로모션 증정품을 받지 않을 경우, buy 수량은 일반 상품으로 분류한다.
    - [콜라-5] -> 1개 더 안받음 -> 프로모션:3,일반:2
- 프로모션 적용 수량을 제외한 수량이 최소 buy에 미치지 않는 경우 추가 여부를 묻지 않는다.
    - [콜라-4] -> 3개 프로모션, 1개는 최소 buy에 미치치 못함 -> 프로모션:3,일반:1

#### 프로모션 상품

1. 프로모션 기간이 유효한지 확인한다.
    - Y : 다음 로직(프로모션 할인 확정)
    - N : 일반 상품으로?????
2. 프로모션 종류를 파악한다.
2. 고객이 프로모션 상품의 증점품 수량까지 입력했는지 확인한다.
    - Y : 다음 로직
    - N : 안내 메시지
2. 재고에서 상품 수량을 확인한다. 프로모션 상품 -> 일반 상품 + 안내 메시지
    - Y : 다음 로직
    - N : 안내 메시지

## 여부 확인 상황

1. 프로모션 적용 → 고객이 해당 수량보다 적게 가져온 경우
    - Y: 증정 받을 수 있는 상품을 추가한다.
    - N: 증정 받을 수 있는 상품을 추가하지 않는다.

2. 프로모션 적용 → 재고 부족 → 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우
    - Y: 일부 수량에 대해 정가로 결제한다.
    - N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.

3. 멤버십 할인 적용 여부
    - Y: 멤버십 할인을 적용한다.
    - N: 멤버십 할인을 적용하지 않는다.

4. 추가 구매 여부
    - Y: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
    - N: 구매를 종료한다.