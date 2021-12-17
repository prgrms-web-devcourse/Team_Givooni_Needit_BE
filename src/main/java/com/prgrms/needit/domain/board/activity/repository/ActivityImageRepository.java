package com.prgrms.needit.domain.board.activity.repository;

import com.prgrms.needit.domain.board.activity.entity.ActivityImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityImageRepository extends CrudRepository<ActivityImage, Long> {

}
