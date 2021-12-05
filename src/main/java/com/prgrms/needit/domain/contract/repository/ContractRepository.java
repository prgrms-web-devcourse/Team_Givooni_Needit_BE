package com.prgrms.needit.domain.contract.repository;

import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.center.entity.Center;
import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ContractRepository extends CrudRepository<Contract, Long> {

	Optional<Contract> findByIdAndDonation(long id, Donation donation);

	Optional<Contract> findByIdAndDonationWish(long id, DonationWish donationWish);

	List<Contract> findAllByDonation(Donation donation);

	List<Contract> findAllByDonationWish(DonationWish donationWish);

	List<Contract> findAllByCenter(Center center);

	List<Contract> findAllByMember(Member member);

}
