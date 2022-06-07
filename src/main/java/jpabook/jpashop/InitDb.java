package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.OrderService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * UserA
 * - JPA1 BOOK
 * - JPA2 BOOK
 *
 * userB
 * - SPRING1 BOOK
 * - SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitDbService initDbService;

    @PostConstruct
    public void init(){
        initDbService.initDb1();
        initDbService.initDb2();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitDbService{

        private final EntityManager em;

        private final OrderService orderService;

        public void initDb1(){
            Member member = createMember("userA", "서울", "여의도", "111");

            Book book1 = createItem("JPA1 BOOK", 10000, 100);

            Book book2 = createItem("JPA2 BOOK", 20000, 100);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 4);

            Delivery delivery = createDeliver(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);


        }

        @NotNull
        private Delivery createDeliver(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        @NotNull
        private Book createItem(String s, int i, int i2) {
            Book book1 = new Book();
            book1.setName(s);
            book1.setPrice(i);
            book1.setStockQuantity(i2);
            em.persist(book1);
            return book1;
        }

        @NotNull
        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            em.persist(member);
            return member;
        }

        public void initDb2(){
            Member member = createMember("userB", "부산", "송정", "222");

            Book book1 = createItem("SPRING1 BOOK", 20000, 200);

            Book book2 = createItem("SPRING2 BOOK", 30000, 300);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 5);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 10);

            Delivery delivery = createDeliver(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);


        }
    }
}
