package com.prgrms.needit.domain.board.activity.repository;

import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import com.prgrms.needit.domain.user.center.entity.Center;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends PagingAndSortingRepository<Activity, Long> {
	List<Activity> findAllByCenter(Center center);
	List<Activity> findAllByTitleContaining(String title);
	List<Activity> findAllByContentContaining(String content);
	List<Activity> findAllByActivityType(ActivityType activityType);
}
