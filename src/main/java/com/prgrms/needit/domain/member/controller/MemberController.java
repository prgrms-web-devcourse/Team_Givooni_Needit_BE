package com.prgrms.needit.domain.member.controller;

import com.prgrms.needit.common.domain.dto.IsUniqueResponse;
import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.member.dto.MemberRequest;
import com.prgrms.needit.domain.member.dto.MemberResponse;
import com.prgrms.needit.domain.member.dto.MemberSelfResponse;
import com.prgrms.needit.domain.member.service.MemberService;
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

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createMember(
		@RequestBody @Valid MemberRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.createMember(request))
		);
	}

	// TODO: 2021-12-03 Security 적용 후 수정
	@GetMapping
	public ResponseEntity<ApiResponse<MemberSelfResponse>> getMember() {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.getMember(1L))
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
			ApiResponse.of(memberService.updateMember(4L, request)));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteMember() {
		memberService.deleteMember(4L);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/email-check/{email}")
	public ResponseEntity<ApiResponse<IsUniqueResponse>> checkEmail(
		@PathVariable String email
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.checkEmailIsUnique(email)));
	}

	@GetMapping("/nickname-check/{nickname}")
	public ResponseEntity<ApiResponse<IsUniqueResponse>> checkNickname(
		@PathVariable String nickname
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.checkNicknameIsUnique(nickname)));
	}

}
