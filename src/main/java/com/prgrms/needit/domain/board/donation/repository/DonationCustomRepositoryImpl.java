package com.prgrms.needit.domain.board.donation.repository;

import static com.prgrms.needit.domain.board.donation.entity.QDonation.*;
import static com.prgrms.needit.domain.board.donation.entity.QDonationHaveTag.*;

import com.prgrms.needit.common.enums.DonationCategory;
import com.prgrms.needit.domain.board.donation.dto.DonationFilterRequest;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Repository
public class DonationCustomRepositoryImpl implements DonationCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public DonationCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public Page<Donation> searchAllByFilter(
		DonationFilterRequest request, Pageable pageable
	) {
		QueryResults<Donation> result = jpaQueryFactory
			.selectFrom(donation)
			.join(donation.tags, donationHaveTag)
			.where(
				donation.isDeleted.eq(false),
				containTitle(request.getTitle()),
				eqCategory(request.getCategory()),
				inTag(request.getTags())
			)
			.groupBy(donation.id)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	private BooleanExpression containTitle(String title) {
		if (ObjectUtils.isEmpty(title)) {
			return null;
		}
		return donation.title.contains(title);
	}

	private BooleanExpression eqCategory(String category) {
		if (ObjectUtils.isEmpty(category)) {
			return null;
		}
		return donation.category.eq(DonationCategory.of(category));
	}

	private BooleanExpression inTag(List<Long> tags) {
		if (ObjectUtils.isEmpty(tags)) {
			return null;
		}
		return donationHaveTag.themeTag.id.in(tags);
	}

}
