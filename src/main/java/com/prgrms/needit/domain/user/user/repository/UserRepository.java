package com.prgrms.needit.domain.user.user.repository;

import com.prgrms.needit.domain.user.user.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByEmailAndIsDeletedFalse(String email);
}
