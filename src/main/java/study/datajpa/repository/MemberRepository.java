package study.datajpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //메서드명 자체가 너무 길어질 수 있음
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername")    //없어도 실행이 된다. Entity에서 메서드명과 같은 namedQuery를 우선적으로 찾음.
//    없으면 메서드명에 따라서 쿼리 생성
    List<Member> findByUsername(@Param("username") String username);

    //메서드명 간략화 하고 JPQL 을 직접 작성
    //쿼리가 문자열이어도 애플리케이션 로딩 시점에 파싱하여 문법오류 체크
    @Query("select m from Member m where m.username=:useranme and m.age=:age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

}
