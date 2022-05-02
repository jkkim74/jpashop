package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class BookForm {

    private Long id;

    @NotEmpty(message ="상품명은 필수 값입니다.")
    private String name;

    @Min(value = 10, message = "가격은 1000원 이상이어야 합니다.")
    private int price;

    @Min(value = 1, message = "재고수량은 최소 1이상이어야 합니다.")
    private int stockQuantity;

    private String author;
    private String isbn;
}
