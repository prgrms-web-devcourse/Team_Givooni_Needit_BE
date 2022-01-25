package com.prgrms.needit.domain.user.repository;

import com.prgrms.needit.domain.user.entity.EmailCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {

	boolean existsByEmailAndEmailCode(String email, String code);

	Optional<EmailCode> findByEmail(String receiver);

	boolean existsByEmail(String email);
}
