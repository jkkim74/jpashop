package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith (SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @Rollback(value = false)
    public void 주문() throws Exception{
        //given
        Member member = createMember("회원2");

        Item book = createBook("시골JPA", 10, 10000);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId (), book.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals("상품주문시 상태는 Order", OrderStatus.ORDER,order.getStatus ());
        assertEquals ("주문한 상품 종류 수가 정확해야 한다.",1,order.getOrderItems ().size ());
        assertEquals ("주문 가격은 가격 * 수량이다.",10000 * orderCount,order.getTotalPrice());
        assertEquals ("주문가격 만큼 재고가 줄어든다.",8,book.getStockQuantity());


    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember("회원1");
        Item book = createBook("시골JPA", 10, 10000);

        int orderCount = 11;
        //when
        orderService.order (member.getId (),book.getId (),orderCount);

        //then
        fail("재고수량 부족 에러가 발생해야한다.");


    }

    private Item createBook(String name, int stockQuantity, int price) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity (stockQuantity);
        em.persist (book);
        return book;
    }

    private Member createMember(String memberName) {
        Member member = new Member();
        member.setName(memberName);
        member.setAddress(new Address("성남","미금로","123-123"));
        em.persist (member);
        return member;
    }

    @Test
    public void 주무취소() throws Exception{
        //given
        Member member = createMember ("회원1");
        Item item = createBook("시골JPA", 10, 10000);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId (), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne (orderId);
        assertEquals ("주문취소시 상태는 CANCEL 이다.",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문취소된 상품은 취소시, 취소수량만큼 재고가 증가해야한다.",10, item.getStockQuantity());


    }

    @Test
    @Rollback(value = false)
    public void 주무취소_only() throws Exception{
        //given
//        Member member = createMember ();
//        Item item = createBook("시골JPA", 10, 10000);
//        int orderCount = 2;
//        Long orderId = orderService.order(member.getId (), item.getId(), orderCount);
        Long orderId = 8l;

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne (orderId);
        assertEquals ("주문취소시 상태는 CANCEL 이다.",OrderStatus.CANCEL,getOrder.getStatus());
        //assertEquals("주문취소된 상품은 취소시, 취소수량만큼 재고가 증가해야한다.",10, item.getStockQuantity());


    }



}