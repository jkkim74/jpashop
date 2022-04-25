package jpabook.jpashop.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    public final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class,id);
    }
    // 검색용...
    public List<Order> findAll(OrderSearch orderSearch){
        return em.createQuery ("select o from Order o join o.member m" +
                " where o.status = :status" +
                " and m.name like :name",Order.class)
                .setParameter ("status",orderSearch.getOrderStatus())
                .setParameter ("name", orderSearch.getMemberName ())
                .getResultList ();
//        QOrder qOrder = QOrder.order;
//        QOrder member = new QOrder("member");
//        JPAQuery<?> query = new JPAQuery<Void> (em);
//        Predicate joinMethon = qOrder.member.eq (member.member).and (qOrder.member.);
//        query.select (qOrder).from (qOrder).join(member).on (joinMethon);

    }
}
