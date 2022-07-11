package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void findAllWithMemberDeliveryTest() {
        orderRepository.findAllWithMemberDelivery(1, 3);
    }

    @Test
    public void findAllWithItem() {
        orderRepository.findAllWithItem();
    }

}