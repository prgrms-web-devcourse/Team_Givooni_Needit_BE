package com.prgrms.needit.domain.member.repository;

import com.prgrms.needit.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
