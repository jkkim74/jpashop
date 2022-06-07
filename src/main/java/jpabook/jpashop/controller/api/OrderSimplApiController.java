package jpabook.jpashop.controller.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ManyToOne, OneToOne의 성능최적화 방안
 * Order - Member
 * Order - Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimplApiController {

    private  final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all){
            order.getMember().getName(); // Lazy 강제초기화
            order.getDelivery().getAddress();// Lazy 강제초기화
        }
        return all;
    }
}
