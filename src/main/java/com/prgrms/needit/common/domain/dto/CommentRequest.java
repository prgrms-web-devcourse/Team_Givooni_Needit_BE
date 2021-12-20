package com.prgrms.needit.common.domain.dto;

import com.prgrms.needit.domain.board.activity.entity.Activity;
import com.prgrms.needit.domain.board.activity.entity.ActivityComment;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.entity.DonationComment;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.entity.DonationWishComment;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
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

	public ActivityComment toEntity(Member member, Activity activity) {
		return ActivityComment.builder()
							  .activity(activity)
							  .comment(comment)
							  .member(member)
							  .build();
	}

	public ActivityComment toEntity(Center center, Activity activity) {
		return ActivityComment.builder()
							  .activity(activity)
							  .comment(comment)
							  .center(center)
							  .build();
	}

}
