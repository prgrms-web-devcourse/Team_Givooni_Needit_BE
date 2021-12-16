package com.prgrms.needit.domain.board.wish.repository;

import static com.prgrms.needit.domain.board.wish.entity.QDonationWish.*;
import static com.prgrms.needit.domain.board.wish.entity.QDonationWishHaveTag.*;
import static com.prgrms.needit.domain.user.center.entity.QCenter.*;

import com.prgrms.needit.common.enums.DonationCategory;
import com.prgrms.needit.domain.board.wish.dto.DonationWishFilterRequest;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
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
public class WishCustomRepositoryImpl implements WishCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public WishCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public Page<DonationWish> searchAllByFilter(
		DonationWishFilterRequest request, Pageable pageable
	) {
		QueryResults<DonationWish> result = jpaQueryFactory
			.selectFrom(donationWish)
			.join(donationWish.tags, donationWishHaveTag)
			.join(donationWish.center, center)
			.where(
				containTitle(request.getTitle()),
				eqCategory(request.getCategory()),
				containCenter(request.getCenterName()),
				containLocation(request.getLocation()),
				inTag(request.getTags())
			)
			.groupBy(donationWish.id)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(result.getResults(), pageable, result.getTotal());
	}

	private BooleanExpression containTitle(String title) {
		if (ObjectUtils.isEmpty(title)) {
			return null;
		}
		return donationWish.title.contains(title);
	}

	private BooleanExpression eqCategory(String category) {
		if (ObjectUtils.isEmpty(category)) {
			return null;
		}
		return donationWish.category.eq(DonationCategory.of(category));
	}

	private BooleanExpression containCenter(String centerName) {
		if (ObjectUtils.isEmpty(centerName)) {
			return null;
		}
		return center.name.contains(centerName);
	}

	private BooleanExpression containLocation(String location) {
		if (ObjectUtils.isEmpty(location)) {
			return null;
		}
		return center.address.contains(location);
	}

	private BooleanExpression inTag(List<Long> tags) {
		if (ObjectUtils.isEmpty(tags)) {
			return null;
		}
		return donationWishHaveTag.themeTag.id.in(tags);
	}
}