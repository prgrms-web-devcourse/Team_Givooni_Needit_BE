package com.prgrms.needit.domain.user.email.repository;

import com.prgrms.needit.domain.user.email.entity.EmailCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {

	Optional<EmailCode> findByEmailAndEmailCode(String email, String code);

	Optional<EmailCode> findByEmail(String receiver);

	boolean existsByEmail(String email);
}
