package jpabook.jpashop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.QItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.QDelivery.delivery;
import static jpabook.jpashop.domain.QMember.member;
import static jpabook.jpashop.domain.QOrder.order;
import static jpabook.jpashop.domain.QOrderItem.orderItem;
import static jpabook.jpashop.domain.item.QItem.item;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    public final EntityManager em;

    private final JPAQueryFactory jpaQueryFactory;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class,id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    /**
     * JPA Criteria
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    public List<Order> findAllWithItem() {
       return jpaQueryFactory.selectFrom(order)
                .join(order.member,member).fetchJoin()
                .join(order.delivery,delivery).fetchJoin()
                .join(order.orderItems,orderItem).fetchJoin()
                .join(orderItem.item,item).fetchJoin()
                .distinct()
                .fetch();

       // distinct는 쿼리의 distinct뿐만 아니라,  psql 에 distinct로 객체의 키의 중복을 피한다..
        // 1:N 패치조인에서는 페이징이 안된다..
        // 1:N:N...은 안된다.. 데이타에 정합성에 문제가 있다.. 1:N 패치조인은 하나만 해야 한다..
//        return em.createQuery(
//                        "select distinct o from Order o" +
//                                " join fetch o.member m" +
//                                " join fetch o.delivery d" +
//                                " join fetch o.orderItems oi" +
//                                " join fetch oi.item i", Order.class)
//                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return jpaQueryFactory.select(order)
                .from(order)
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    public List<Order> findAll(OrderSearch orderSearch){
        return jpaQueryFactory.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch))
                .limit(1000)
                .fetch();

    }

    private BooleanExpression nameLike(OrderSearch orderSearch) {
        if(!StringUtils.hasText(orderSearch.getMemberName())){
            return null;
        }
        return member.name.like(orderSearch.getMemberName());
    }

    private BooleanExpression statusEq(OrderStatus statusCond){
        if(statusCond == null){
            return null;
        }
        return order.status.eq(statusCond);
    }
}