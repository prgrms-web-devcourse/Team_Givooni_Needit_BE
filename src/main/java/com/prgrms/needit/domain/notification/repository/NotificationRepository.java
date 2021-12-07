package com.prgrms.needit.domain.notification.repository;

import com.prgrms.needit.domain.notification.entity.Notification;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {

}
