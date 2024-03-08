package study.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
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
    @Query("select m from Member m where m.username=:username and m.age=:age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);

    Member findMemberByUsername(String username);

    Optional<Member> findOptionalByUsername(String username);

    //TotalCount 쿼리 자체가 비싸기 때문에, 비즈니스 로직과 순수 카운트 쿼리를 분리할 수 있음.
    //아우터 조인 의 전체 카운트는 기준 row 수와 같기 때문에 카운트에서도 조인할 필요없이 기준 row 수만 구하면 된다
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying  //데이터변경 어노테이션
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //Override -> 이미 findAll 메서드가 상위에 있기 때문
    //EntityGraph -> JPQL 작성하지 않아도 객체 그래프 엮어서 가져오는 기능(결국 내부에서는 fetch join 사용)
    // -> JPA 표준 스택임
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //쿼리도 짜고 fetch join 붙이고 싶을 때
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //findBy 기능에 fetch join 붙이고 싶을 때
//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

}
