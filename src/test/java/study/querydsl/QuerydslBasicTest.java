package study.querydsl;

import static study.querydsl.entity.QMember.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

@SpringBootTest
@Transactional
@Commit
public class QuerydslBasicTest {
	@Autowired
	private EntityManager em;
	private JPAQueryFactory queryFactory;

	@BeforeEach
	public void before() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);

		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
	}

	@Test
	@DisplayName("JPQL 예제")
	public void startJpql() {
		String query = "select m from Member m where m.username = :username";
		Member findMember = em.createQuery(query, Member.class)
			.setParameter("username", "member1")
			.getSingleResult();

		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}

	@Test
	@DisplayName("Querydsl 예제")
	public void startQuerydsl() {
		Member findMember = queryFactory.selectFrom(member)
			.where(
				member.username.eq("member1")
			)
			.fetchOne();

		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}

	@Test
	public void search() {
		Member findMember = queryFactory
			.selectFrom(member)
			.where(
				member.username.eq("member1")
					.and(member.age.eq(10))
			)
			.fetchOne();

		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}
}
