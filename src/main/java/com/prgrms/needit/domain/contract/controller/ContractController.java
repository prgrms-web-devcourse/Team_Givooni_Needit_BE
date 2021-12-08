package com.prgrms.needit.domain.contract.controller;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.contract.controller.bind.ContractRequest;
import com.prgrms.needit.domain.contract.controller.bind.ContractStatusRequest;
import com.prgrms.needit.domain.contract.entity.response.ContractResponse;
import com.prgrms.needit.domain.contract.entity.response.DonationContractResponse;
import com.prgrms.needit.domain.contract.entity.response.WishContractResponse;
import com.prgrms.needit.domain.contract.exception.IllegalContractStatusException;
import com.prgrms.needit.domain.contract.service.ContractService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

	@GetMapping("/donation/{donationId}/{contractId}")
	public ResponseEntity<ApiResponse<DonationContractResponse>> readDonationContract(
		@PathVariable("donationId") long donationId,
		@PathVariable("contractId") long contractId
	) {
		DonationContractResponse donationContractResponse = contractService
			.readDonationContract(donationId, contractId);
		return ResponseEntity.ok(ApiResponse.of(donationContractResponse));
	}

	@GetMapping("/wish/{wishId}/{contractId}")
	public ResponseEntity<ApiResponse<WishContractResponse>> readDonationWishContract(
		@PathVariable("wishId") long wishId,
		@PathVariable("contractId") long contractId
	) {
		WishContractResponse wishContractResponse = contractService
			.readDonationWishContract(wishId, contractId);
		return ResponseEntity.ok(ApiResponse.of(wishContractResponse));
	}

	@PostMapping("/donation/{donationArticleId}/comment/{donationCommentId}")
	public ResponseEntity<ApiResponse<DonationContractResponse>> createDonationContract(
		@PathVariable("donationArticleId") long donationArticleId,
		@PathVariable("donationCommentId") long donationCommentId,
		@AuthenticationPrincipal Object user, // set usertype by jwt subject.
		ContractRequest request
	) {
		DonationContractResponse donationContract = contractService.createDonationContract(
			request.getContractDate(), donationArticleId, donationCommentId, null);
		return ResponseEntity
			.created(URI.create(String.format("/contract/%d", donationContract.getId())))
			.body(ApiResponse.of(donationContract));
	}

	@PostMapping("/wish/{wishArticleId}/comment/{wishCommentId}")
	public ResponseEntity<ApiResponse<WishContractResponse>> createDonationWishContract(
		@PathVariable("wishArticleId") long wishArticleId,
		@PathVariable("wishCommentId") long wishCommentId,
		@AuthenticationPrincipal Object user, // set usertype by jwt subject.
		ContractRequest request
	) {
		WishContractResponse donationWishContract = contractService.createDonationWishContract(
			request.getContractDate(), wishArticleId, wishCommentId, null);
		return ResponseEntity
			.created(URI.create(String.format("/contract%d", donationWishContract.getId())))
			.body(ApiResponse.of(donationWishContract));
	}

	@PatchMapping("/{contractId}")
	public ResponseEntity<ApiResponse<ContractResponse>> updateContractStatus(
		@PathVariable("contractId") long contractId,
		@RequestBody @Valid ContractStatusRequest request) {
		ContractResponse contractResponse;
		switch(request.getContractStatus()) {
			case ACCEPTED:
				contractResponse = contractService.acceptContract(contractId);
				break;

			case REFUSED:
				contractResponse = contractService.refuseContract(contractId);
				break;

			default:
				throw new IllegalContractStatusException(ErrorCode.NOT_SUPPORTED_STATUS);
		}
		return ResponseEntity.ok(ApiResponse.of(contractResponse));
	}

}