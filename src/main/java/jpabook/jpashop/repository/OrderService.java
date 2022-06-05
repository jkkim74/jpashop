package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // 멤버 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송설정
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문설정
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문을 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문을 저장
        orderRepository.save(order);

        return order.getId();

    }

    /**
     * 주문취소
     */
   //@Transactional
    public void cancelOrder(Long orderId){
        // 주문조회
        Order order = orderRepository.findOne(orderId);

        // 주문취소
        order.cancel();

    }

    // 검색
     public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
     }
}
