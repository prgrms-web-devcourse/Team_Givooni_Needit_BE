package com.prgrms.needit.domain.notification.repository;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {

	List<Notification> findAllByUserIdAndUserTypeAndCheckedFalse(
		Long notifiedUserId,
		UserType notifiedUserType
	);
}
