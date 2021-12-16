package com.prgrms.needit.domain.user.center.repository;

import com.prgrms.needit.domain.user.center.entity.Center;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterRepository extends JpaRepository<Center, Long> {

	Optional<Center> findByIdAndIsDeletedFalse(Long centerId);

	boolean existsByEmail(String email);

	boolean existsByName(String name);

	Optional<Center> findByEmailAndIsDeletedFalse(String email);

	Optional<Center> findAllByIsDeletedFalseAndNameContaining(String center);
}
