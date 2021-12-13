package com.prgrms.needit.domain.user.member.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.member.dto.MemberRequest;
import com.prgrms.needit.domain.user.member.dto.MemberResponse;
import com.prgrms.needit.domain.user.member.dto.MemberSelfResponse;
import com.prgrms.needit.domain.user.member.service.MemberService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/signUp")
	public ResponseEntity<ApiResponse<Long>> createMember(
		@RequestBody @Valid MemberRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.createMember(request))
		);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<MemberSelfResponse>> getMyInfo() {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.getMyInfo())
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<MemberResponse>> getOtherMember(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.getOtherMember(id))
		);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Long>> updateMember(
		@RequestBody @Valid MemberRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.updateMember(request)));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteMember() {
		memberService.deleteMember();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
