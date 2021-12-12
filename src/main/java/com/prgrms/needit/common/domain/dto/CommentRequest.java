package com.prgrms.needit.common.domain.dto;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.member.entity.Member;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequest {

	@NotBlank(message = "댓글을 입력해주세요")
	private String comment;

	public CommentRequest(String comment) {
		this.comment = comment;
	}

	public DonationComment toEntity(Center center, Donation donation) {
		return DonationComment.builder()
							  .comment(comment)
							  .donation(donation)
							  .center(center)
							  .build();
	}

	public DonationWishComment toEntity(Member member, DonationWish wish) {
		return DonationWishComment.builder()
								  .comment(comment)
								  .donationWish(wish)
								  .member(member)
								  .build();
	}

}
