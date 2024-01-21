package study.datajpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername")    //없어도 실행이 된다. Entity에서 메서드명과 같은 namedQuery를 우선적으로 찾음.
//    없으면 메서드명에 따라서 쿼리 생성
    List<Member> findByUsername(@Param("username") String username);
}
