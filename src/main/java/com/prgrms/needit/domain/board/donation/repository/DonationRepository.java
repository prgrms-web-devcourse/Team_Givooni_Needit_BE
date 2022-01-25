package com.prgrms.needit.domain.board.donation.repository;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository
	extends JpaRepository<Donation, Long>, DonationCustomRepository {

	Optional<Donation> findByIdAndIsDeletedFalse(Long id);

	List<Donation> findAllByMemberAndIsDeletedFalse(Member member);
}
