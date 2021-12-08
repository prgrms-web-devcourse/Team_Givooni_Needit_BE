package com.prgrms.needit.common.email;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {

	Optional<EmailCode> findByEmailAndEmailCode(String email);

	Optional<EmailCode> findByEmail(String receiver);
}
