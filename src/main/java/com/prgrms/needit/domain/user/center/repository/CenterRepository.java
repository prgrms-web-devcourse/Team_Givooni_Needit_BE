package com.prgrms.needit.domain.user.center.repository;

import com.prgrms.needit.domain.user.center.entity.Center;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterRepository extends JpaRepository<Center, Long> {

	List<Center> findAllByIsDeletedFalseAndNameContaining(String centerName);

	Optional<Center> findByIdAndIsDeletedFalse(Long centerId);

	Optional<Center> findByEmailAndIsDeletedFalse(String email);

	boolean existsByEmail(String email);

}
