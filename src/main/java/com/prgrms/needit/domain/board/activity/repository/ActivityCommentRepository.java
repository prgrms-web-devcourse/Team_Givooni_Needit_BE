package com.prgrms.needit.domain.board.activity.repository;

import com.prgrms.needit.domain.board.activity.entity.ActivityComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityCommentRepository extends CrudRepository<ActivityComment, Long> {

}
