package com.prgrms.needit.domain.message.repository;

import com.prgrms.needit.domain.message.entity.ContractStatus;
import com.prgrms.needit.domain.message.entity.MessageContract;
import com.prgrms.needit.domain.message.entity.MessageType;
import com.prgrms.needit.domain.message.entity.PostType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageContractRepository extends
	PagingAndSortingRepository<MessageContract, Long> {

	List<MessageContract> findAllByPostIdAndPostTypeAndCommentId(
		long postId,
		PostType postType,
		long commentId,
		Pageable pageable
	);

	List<MessageContract> findFirst100ByPostIdAndPostTypeAndCommentIdAndIdGreaterThanEqual(
		long postId,
		PostType postType,
		long commentId,
		long messageId
	);

	Optional<MessageContract> findByPostIdAndPostTypeAndCommentIdAndIdAndMessageTypeAndStatus(
		long postId,
		PostType postType,
		long commentId,
		long messageId,
		MessageType messageType,
		ContractStatus status);

}
