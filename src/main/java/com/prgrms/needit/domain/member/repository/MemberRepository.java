package com.prgrms.needit.domain.member.repository;

import com.prgrms.needit.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByIdAndIsDeletedFalse(Long memberId);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);
}
