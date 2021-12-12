package com.prgrms.needit.domain.message.service;

import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.needit.common.enums.DonationCategory;
import com.prgrms.needit.common.enums.DonationQuality;
import com.prgrms.needit.common.enums.DonationStatus;
import com.prgrms.needit.common.enums.UserType;
import com.prgrms.needit.domain.board.donation.entity.Donation;
import com.prgrms.needit.domain.board.donation.repository.DonationRepository;
import com.prgrms.needit.domain.board.wish.entity.DonationWish;
import com.prgrms.needit.domain.board.wish.repository.DonationWishRepository;
import com.prgrms.needit.domain.message.entity.ChatMessage;
import com.prgrms.needit.domain.message.entity.response.ChatMessageResponse;
import com.prgrms.needit.domain.message.repository.ChatMessageRepository;
import com.prgrms.needit.domain.user.center.entity.Center;
import com.prgrms.needit.domain.user.center.repository.CenterRepository;
import com.prgrms.needit.domain.user.member.entity.Member;
import com.prgrms.needit.domain.user.member.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChatMessageServiceTest {

	Center center1, center2;
	Member member1, member2;
	Donation donation1, donation2;
	DonationWish donationWish1, donationWish2;
	@Autowired
	private DonationRepository donationRepository;
	@Autowired
	private DonationWishRepository donationWishRepository;
	@Autowired
	private CenterRepository centerRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired
	private ChatMessageService chatMessageService;

	@BeforeEach
	void init() {
		center1 = centerRepository.save(Center.builder()
											  .address("CENTER1_ADDRESS")
											  .name("CENTER1_NAME")
											  .contact("CENTER1_CONTACT")
											  .email("CENTEr1_EMAIL")
											  .owner("CENTER1_OWNER")
											  .password("P")
											  .profileImageUrl("A")
											  .registrationCode("CENTER1_REGICODE")
											  .build());
		center2 = centerRepository.save(Center.builder()
											  .address("CENTEr2_ADDR")
											  .name("CENTER2_NAME")
											  .contact("CENTEr2_CONTACT")
											  .email("CENTER2_EMAIL")
											  .owner("CENTER2_OWNER")
											  .password("P")
											  .profileImageUrl("A")
											  .registrationCode("CENTER2_REGICODE")
											  .build());

		member1 = memberRepository.save(Member.builder()
											  .address("A")
											  .contact("C")
											  .email("MEMBER1_EMAIL")
											  .password("P")
											  .profileImageUrl("AAA")
											  .nickname("MEMBER1_NIckNAME")
											  .build());
		member2 = memberRepository.save(Member.builder()
											  .address("A")
											  .contact("C")
											  .email("MEMBER2_EMAIL")
											  .password("P")
											  .profileImageUrl("AAA")
											  .nickname("MEMBER2_NICKNAME	")
											  .build());

		donation1 = donationRepository.save(
			Donation.builder()
					.member(member1)
					.content("CONTENT")
					.category(DonationCategory.GOODS)
					.status(DonationStatus.DONATING)
					.quality(DonationQuality.HIGH)
					.title("TIT")
					.build());
		donation2 = donationRepository.save(
			Donation.builder()
					.member(member1)
					.content("CONTENT")
					.category(DonationCategory.GOODS)
					.status(DonationStatus.DONATING)
					.quality(DonationQuality.HIGH)
					.title("TIT")
					.build());
		donationWish1 = donationWishRepository.save(
			DonationWish.builder()
						.center(center1)
						.content("CONTENT")
						.category(DonationCategory.GOODS)
						.status(DonationStatus.DONATING)
						.title("TIT")
						.build());
		donationWish2 = donationWishRepository.save(
			DonationWish.builder()
						.center(center1)
						.content("CONTENT")
						.category(DonationCategory.GOODS)
						.status(DonationStatus.DONATING)
						.title("TIT")
						.build());

		chatMessageRepository.save(
			ChatMessage
				.builder()
				.center(center1)
				.content("Center: Member, reply to me.")
				.member(member1)
				.senderType(UserType.CENTER)
				.contract(null)
				.donationWish(null)
				.donation(donation1)
				.build());
		chatMessageRepository.save(
			ChatMessage
				.builder()
				.center(center1)
				.content("Member: Center, I replied.")
				.member(member1)
				.senderType(UserType.MEMBER)
				.contract(null)
				.donationWish(null)
				.donation(donation1)
				.build());
		chatMessageRepository.save(
			ChatMessage
				.builder()
				.center(center1)
				.content("Center: Member, acknowledged.")
				.member(member1)
				.senderType(UserType.CENTER)
				.contract(null)
				.donationWish(null)
				.donation(donation1)
				.build());

		chatMessageRepository.save(
			ChatMessage
				.builder()
				.center(center2)
				.content("Center: Member, can you donate me?")
				.member(member1)
				.senderType(UserType.CENTER)
				.contract(null)
				.donationWish(donationWish1)
				.build());
		chatMessageRepository.save(
			ChatMessage
				.builder()
				.center(center2)
				.content("Member: Center, I can.")
				.member(member1)
				.senderType(UserType.MEMBER)
				.contract(null)
				.donationWish(donationWish1)
				.build());
		chatMessageRepository.save(
			ChatMessage
				.builder()
				.center(center2)
				.content("Center: Member, acknowledged.")
				.member(member1)
				.senderType(UserType.CENTER)
				.contract(null)
				.donationWish(donationWish1)
				.build());
		// 채팅 메시지를 여러 종류에 여러개 만들고 쿼리 메서드를 실행했을때 원하는대로 나오느지 테스트.
	}

	@Test
	@DisplayName("Message grouping test.")
	void messageGroups() {
		List<ChatMessageResponse> memberChats = chatMessageService.getMemberChats(member1.getId());
		assertFalse(memberChats.isEmpty());
		assertEquals(2, memberChats.size());
		ChatMessageResponse chat1 = memberChats.get(0);
		System.out.printf("Member: %s / Center: %s / Content: %s%n",
						  chat1.getMember(), chat1.getCenter(), chat1.getContent()
		);
	}

}
