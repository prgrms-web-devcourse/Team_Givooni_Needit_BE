package com.prgrms.needit.domain.user.user.dto;

import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.user.entity.CenterSideInfo;
import com.prgrms.needit.domain.user.user.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CurUser {

	private Long id;
	private String email;
	private String name;
	private String contact;
	private String address;
	private String introduction;
	private String image;
	private String role;
	private String owner;
	private String registrationCode;

	private CurUser(
		Long id,
		String email,
		String name,
		String contact,
		String address,
		String introduction,
		String image,
		String type,
		String owner,
		String registrationCode
	) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.contact = contact;
		this.address = address;
		this.introduction = introduction;
		this.image = image;
		this.role = type;
		this.owner = owner;
		this.registrationCode = registrationCode;
	}

	public static CurUser toResponse(Users user, CenterSideInfo sideInfo) {
		return new CurUser(
			user.getId(),
			user.getEmail(),
			user.getNickname(),
			user.getContact(),
			user.getAddress(),
			user.getIntroduction(),
			user.getImage(),
			user.getUserRole()
				.name(),
			sideInfo != null ? sideInfo.getOwner() : null,
			sideInfo != null ? sideInfo.getRegistrationCode() : null
		);
	}

	public static CurUser toResponse(Member member) {
		return new CurUser(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			member.getContact(),
			member.getAddress(),
			member.getIntroduction(),
			member.getProfileImageUrl(),
			UserType.MEMBER.name(),
			null,
			null
		);
	}

	public static CurUser toResponse(Center center) {
		return new CurUser(
			center.getId(),
			center.getEmail(),
			center.getName(),
			center.getContact(),
			center.getAddress(),
			center.getIntroduction(),
			center.getProfileImageUrl(),
			UserType.CENTER.name(),
			center.getOwner(),
			center.getRegistrationCode()
		);
	}
}
