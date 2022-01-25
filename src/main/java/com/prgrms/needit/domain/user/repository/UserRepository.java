package com.prgrms.needit.domain.user.repository;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.user.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByEmailAndIsDeletedFalse(String email);

	Optional<Users> findByIdAndIsDeletedFalse(Long id);

	List<Users> findAllByNicknameContainingAndUserRoleAndIsDeletedFalse(String name, UserType role);

	boolean existsByEmail(String checkEmail);

	boolean existsByNickname(String nickname);

}
