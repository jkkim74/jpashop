package jpabook.jpashop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.QItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static jpabook.jpashop.domain.item.QItem.item;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    private final JPAQueryFactory jpaQueryFactory;

    public void save(Item item){
        if(item.getId() == null){
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return jpaQueryFactory.selectFrom(item).where(item.id.eq(id)).fetchOne();
    }

    public List<Item> findAll(){
        return jpaQueryFactory.selectFrom(item).fetch();
    }
}
