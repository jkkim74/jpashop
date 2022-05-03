package jpabook.jpashop.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Builder
@Setter @Getter
public class BookDto {
    private String name;
    private int price;
    private int stockQuantity;
}
