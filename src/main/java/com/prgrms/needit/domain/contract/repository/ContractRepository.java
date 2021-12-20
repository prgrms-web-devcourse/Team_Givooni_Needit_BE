package com.prgrms.needit.domain.contract.repository;

import com.prgrms.needit.domain.contract.entity.Contract;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ContractRepository extends CrudRepository<Contract, Long> {

	List<Contract> findAllByMember(Member member);

	List<Contract> findAllByCenter(Center center);

}
