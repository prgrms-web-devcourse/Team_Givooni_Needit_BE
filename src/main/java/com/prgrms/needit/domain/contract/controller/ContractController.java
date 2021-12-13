package com.prgrms.needit.domain.contract.controller;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotFoundResourceException;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.contract.controller.bind.ContractRequest;
import com.prgrms.needit.domain.contract.controller.bind.ContractStatusRequest;
import com.prgrms.needit.domain.contract.entity.response.ContractResponse;
import com.prgrms.needit.domain.contract.exception.IllegalContractStatusException;
import com.prgrms.needit.domain.contract.service.ContractService;
import com.prgrms.needit.domain.user.login.service.UserService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

	private final ContractService contractService;
	private final UserService userService;

	public Long getCurUserId() {
		if(userService.getCurCenter().isPresent() && userService.getCurMember().isEmpty()) {
			return userService.getCurCenter().get().getId();
		}

		if(userService.getCurMember().isPresent() && userService.getCurCenter().isEmpty()) {
			return userService.getCurMember().get().getId();
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	public UserType getCurUserType() {
		if(userService.getCurCenter().isPresent() && userService.getCurMember().isEmpty()) {
			return UserType.CENTER;
		}

		if(userService.getCurMember().isPresent() && userService.getCurCenter().isEmpty()) {
			return UserType.MEMBER;
		}

		throw new NotFoundResourceException(ErrorCode.NOT_FOUND_USER);
	}

	@GetMapping("/{contractId}")
	public ResponseEntity<ApiResponse<ContractResponse>> readDonationContract(
		@NotNull @PathVariable("contractId") Long contractId
	) {
		ContractResponse contract = contractService.readContract(contractId);
		return ResponseEntity.ok(ApiResponse.of(contract));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<ContractResponse>>> readMyContracts() {
		return ResponseEntity.ok(
			ApiResponse.of(contractService.readMyContracts()));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<ContractResponse>> createContract(
		@Valid @RequestBody ContractRequest request
	) {
		ContractResponse response;
		switch (request.getBoardType()) {
			case DONATION:
				response = contractService.createDonationContract(
					request.getContractDate(),
					request.getCommentId(),
					getCurUserType()
				);
				break;

			case WISH:
				response = contractService.createDonationWishContract(
					request.getContractDate(),
					request.getCommentId(),
					getCurUserType()
				);
				break;

			default:
				throw new InvalidArgumentException(ErrorCode.INVALID_BOARD_TYPE);
		}

		return ResponseEntity
			.created(URI.create(String.format("/contract/%d", response.getId())))
			.body(ApiResponse.of(response));
	}

	@PatchMapping("/{contractId}")
	public ResponseEntity<ApiResponse<ContractResponse>> updateContractStatus(
		@NotNull @PathVariable("contractId") Long contractId,
		@Valid @RequestBody ContractStatusRequest request
	) {
		ContractResponse contractResponse;
		switch (request.getContractStatus()) {
			case ACCEPTED:
				contractResponse = contractService.acceptContract(contractId);
				break;

			case REFUSED:
				contractResponse = contractService.refuseContract(contractId);
				break;

			default:
				throw new IllegalContractStatusException(ErrorCode.INVALID_STATUS_VALUE);
		}
		return ResponseEntity.ok(ApiResponse.of(contractResponse));
	}

}
