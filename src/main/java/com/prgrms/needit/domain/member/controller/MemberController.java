package com.prgrms.needit.domain.member.controller;

import com.prgrms.needit.common.email.EmailService;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.member.dto.MemberDetailResponse;
import com.prgrms.needit.domain.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.member.service.MemberService;
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

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createMember(
		@RequestBody MemberCreateRequest request
	) throws Exception {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.createMember(request))
		);
	}

	// TODO: 2021-12-03 Security 적용 후 수정
	@GetMapping
	public ResponseEntity<ApiResponse<MemberDetailResponse>> getMember() {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.getMember(1L))
		);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<Long>> updateMember(
		@RequestBody MemberUpdateRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.updateMember(1L, request)));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteMember() {
		memberService.deleteMember(1L);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
