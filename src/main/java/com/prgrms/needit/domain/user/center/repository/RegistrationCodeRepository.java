package com.prgrms.needit.domain.user.center.repository;

import com.prgrms.needit.domain.user.center.entity.RegistrationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationCodeRepository extends JpaRepository<RegistrationCode, Long> {

	boolean existsByRegistrationCodeAndOwnerAndStartDate(
		String registrationCode,
		String owner,
		String startDate
	);
}
