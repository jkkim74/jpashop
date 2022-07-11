package jpabook.jpashop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static jpabook.jpashop.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    private final JPAQueryFactory jpaQueryFactory;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return jpaQueryFactory.selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    public List<Member> findAll(){
       return jpaQueryFactory.select(member)
                .from(member)
                .fetch();
    }

    public List<Member> findByName(String name){
        return jpaQueryFactory.selectFrom(member)
                .where(member.name.eq(name))
                .fetch();
    }
}
