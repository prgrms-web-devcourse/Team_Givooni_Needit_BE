package com.prgrms.needit.domain.board.activity.repository;

import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.enums.ActivityType;
import com.prgrms.needit.domain.center.entity.Center;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends PagingAndSortingRepository<Activity, Long> {

	List<Activity> findAllByCenterAndIsDeletedFalse(Center center);

	@Query("select a from Activity a "
		+ "where (a.title LIKE %?1% or a.content LIKE %?2% or a.activityType=?3)"
		+ " and a.isDeleted=false"
		+ " order by a.id desc")
	List<Activity> searchAllByTitleOrContentOrActivityType(
		String title,
		String content,
		ActivityType activityType,
		Pageable pageable
	);
}
