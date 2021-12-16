package com.prgrms.needit.domain.user.member.controller;

import com.prgrms.needit.common.response.ApiResponse;
import com.prgrms.needit.domain.user.member.dto.MemberCreateRequest;
import com.prgrms.needit.domain.user.member.dto.MemberResponse;
import com.prgrms.needit.domain.user.member.dto.MemberUpdateRequest;
import com.prgrms.needit.domain.user.member.service.MemberService;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	public MemberController(
		MemberService memberService
	) {
		this.memberService = memberService;
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Long>> createMember(
		@RequestBody @Valid MemberCreateRequest request
	) {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.createMember(request))
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
		@RequestPart(required = false) MultipartFile file,
		@RequestPart @Valid MemberUpdateRequest request
	) throws IOException {
		return ResponseEntity.ok(
			ApiResponse.of(memberService.updateMember(file, request)));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteMember() {
		memberService.deleteMember();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
