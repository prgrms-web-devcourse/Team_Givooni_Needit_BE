package com.prgrms.needit.domain.member.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.member.dto.MemberDetailResponse;
import com.prgrms.needit.domain.member.dto.MemberRequest;
import com.prgrms.needit.domain.member.service.MemberService;
import com.prgrms.needit.domain.user.email.service.EmailService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	private final EmailService emailService;

	public MemberController(MemberService memberService, EmailService emailService) {
		this.memberService = memberService;
		this.emailService = emailService;
	}

	@GetMapping
	public ResponseEntity<ApiResponse<MemberDetailResponse>> getMember() {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.getMember(1L))
		);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> signUpMember(
		@Valid @RequestBody MemberRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.signUpMember(request))
		);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Long>> updateMember(
		@Valid @RequestBody MemberRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.updateMember(1L, request))
		);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteMember() {
		memberService.deleteMember(1L);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
