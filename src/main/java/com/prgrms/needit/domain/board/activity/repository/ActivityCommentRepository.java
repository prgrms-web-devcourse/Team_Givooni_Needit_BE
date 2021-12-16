package com.prgrms.needit.domain.board.activity.repository;

import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.ActivityComment;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityCommentRepository extends CrudRepository<ActivityComment, Long> {
	List<ActivityComment> findAllByActivity(Activity activity);
}
