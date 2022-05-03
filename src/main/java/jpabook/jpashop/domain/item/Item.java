package jpabook.jpashop.domain.item;

import jpabook.jpashop.controller.BookDto;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직 ==//

    /**
     * 재고증가
     * @param quantity
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 재고감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException ("need more stock");
        }
        this.stockQuantity = restStock;

    }

    /**
     * 수정항목: 가격,상품명,재고수량
     */
    public void modify(BookDto bookDto) {
        this.price = bookDto.getPrice();
        this.name = bookDto.getName();
        this.stockQuantity = bookDto.getStockQuantity();
    }
}
