package study.querydsl;


import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QHello.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import study.querydsl.entity.Hello;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		Hello saveHello = new Hello();
		em.persist(saveHello);

		JPAQueryFactory query = new JPAQueryFactory(em);

		Hello result = query
			.selectFrom(hello)
			.fetchOne();

		assertThat(result).isEqualTo(saveHello);
		assertThat(result.getId()).isEqualTo(hello.id);
	}
}
