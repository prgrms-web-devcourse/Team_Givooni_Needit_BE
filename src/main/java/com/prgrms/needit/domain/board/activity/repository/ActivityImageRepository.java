package com.prgrms.needit.domain.board.activity.repository;

import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.ActivityImage;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityImageRepository extends CrudRepository<ActivityImage, Long> {
	List<ActivityImage> findAllByActivity(Activity activity);
}
