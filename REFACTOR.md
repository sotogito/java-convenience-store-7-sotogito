### 리팩토링

- md 파일 읽어오기
- ~~재고 차감 분명히하기~~
- 중앙 어쩌구관 관리

---

### 상품의 종류

1. 일반 상품

- 일반 상품은 품목이 일반인 경우 해당.
- 재고 차감 : 일반상품

2. 프로모션 상품

- 프로모션 상품으로 저장하고 프로모션 주문 목록에 저장한다.
- 재고 차감 : 프로모션 -> 일반상품

3. 기간이 유효하지 않는 프로모션 상품

- 일반 상품으로 저장하고 프로모션 주문 목록에 저장한다.
    - 기간이 유효하지 않다면 상품은 프로모션 상품이 될 수 없다.
    - 방안 1. : 프로모션 상품으로 저장후 일반 주문에 저장 isValidDate를 falase로 , 만약 false일 경우 수량만큼 일반 상품 차감
    - ✅ 방안 2. : 일반 상품으로 저장후 일반 목록에 저장 그냥 일반 재고가 부족하면 프로모션 사용하면 됨!!!!
        - 그러기 위해서는 애초에 상품을 저장할때 기간이 유효하지 않다면
- 재고 차감 : 일반 상품 -> 프로모션