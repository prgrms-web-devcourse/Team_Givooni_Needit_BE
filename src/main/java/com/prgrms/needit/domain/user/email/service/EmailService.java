package com.prgrms.needit.domain.user.email.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.DuplicatedResourceException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.domain.user.email.entity.EmailCode;
import com.prgrms.needit.domain.user.email.repository.EmailCodeRepository;
import java.util.Random;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender emailSender;
	private final EmailCodeRepository emailCodeRepository;
	private static final Random random = new Random();

	public static String createKey() {
		StringBuilder key = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			key.append((random.nextInt(10)));
		}
		return key.toString();
	}

	private MimeMessage createMessage(String receiver, String code) {
		log.info("보내는 대상 : {}", receiver);
		log.info("인증 번호 : {}", code);
		try {

			MimeMessage message = emailSender.createMimeMessage();

			String codeWithDash = createCode(code);
			message.addRecipients(RecipientType.TO, receiver);
			message.setSubject("Need!t 확인 코드: " + codeWithDash);

			String msg = "<head>\n <style>\n @font-face {\n font-family: 'Pretendard-Regular';\n"
				+ "src: url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff')"
				+ "format('woff');\n font-weight: 400;\n font-style: normal;\n }\n "
				+ "@font-face {\n font-family: 'Pretendard-ExtraBold';\n "
				+ "src: url('https://cdn.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-ExtraBold.woff') "
				+ "format('woff');\n font-weight: 800;\n font-style: normal;\n }\n </style>\n"
				+ "</head>\n<body style=\"font-family: Pretendard-Regular\">\n"
				+ "<div style=\"background-color: #FEA42A; padding-left: 10%\">\n\n  "
				+ "<div style=\"padding-top: 2%; padding-bottom: 2%; display: flex; align-items: center; gap: 3%;\">\n"
				+ "<img width=\"120px\" height=\"120px\"\n "
				+ "src=\"https://user-images.githubusercontent.com/63666375/146744018-d7d0a56e-7a58-4ea9-a80e-0e22959b0d22.png\">\n\n "
				+ "<h1 style=\"color: white; font-family: Pretendard-ExtraBold; font-size: 3em\">회원가입</h1>\n "
				+ "</div>\n\n\n</div>\n\n<div style=\"background-color: white; padding-left: 10%; margin-top: 4.5%\">\n "
				+ "<div>\n <h1 style=\"font-family: Pretendard-ExtraBold;\">이메일 인증 코드 확인</h1>\n </div>\n"
				+ "<div>\n <p style=\"font-family: Pretendard-ExtraBold;\">아래 확인 코드를 Need!t 가입 창이 있는 브라우저 창에 입력하세요.</p>\n </div>\n <div>\n "
				+ "<div\n style=\"width: fit-content; height: auto; background-color: #E7E6E6; font-size: 3em; border-radius: 5px; padding: 5px; margin-top : 2.5%; font-family: Pretendard-ExtraBold;\">\n ";
			msg += code;
			msg +=
				"\n</div>\n</div>\n</div>\n\n<div style=\"margin-top: 5%; margin-bottom: 5%;\">\n"
					+ "<hr>\n</div>\n\n<div style=\"padding-left: 10%; font-size: small;\">\n"
					+ "<div style=\"font-family: Pretendard-ExtraBold;\">\n<i>본 메일은 발신 전용입니다.</i>\n</div>\n<div>\n"
					+ "<p style=\"font-family: Pretendard-ExtraBold;\">ⓒ 2021. Need!t, Inc Co. all rights reserved.</p>\n</div>\n</div>\n</body>";

			message.setText(msg, "utf-8", "html");
			message.setFrom(new InternetAddress("needit.mailg@gmail.com", "needit"));

			return message;
		} catch (Exception e) {
			throw new MailSendException(e.getMessage());
		}
	}

	public void sendMessage(String receiver) {
		if (isEmailExist(receiver)) {
			throw new DuplicatedResourceException(ErrorCode.ALREADY_EXIST_EMAIL);
		}

		final String key = createKey();
		MimeMessage message = createMessage(receiver, key);
		emailSender.send(message);
		EmailCode emailCode = EmailCode.builder()
									   .email(receiver)
									   .emailCode(key)
									   .build();
		emailCodeRepository.save(emailCode);
	}

	public void resendMessage(String receiver) {
		final String key = createKey();
		MimeMessage message = createMessage(receiver, key);
		EmailCode prevEmailCode = emailCodeRepository
			.findByEmail(receiver)
			.orElseGet(() -> EmailCode.builder()
									  .email(receiver)
									  .build());
		prevEmailCode.changeEmailCode(key);
		emailCodeRepository.save(prevEmailCode);
		emailSender.send(message);
	}

	public void verifyCode(String email, String code) {
		if(!emailCodeRepository.existsByEmailAndEmailCode(email, code)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_EMAIL_CODE);
		}
	}

	public String createCode(String ePw) {
		return ePw.substring(0, 3) + "-" + ePw.substring(3, 6);
	}

	public boolean isEmailExist(String email) {
		return emailCodeRepository.existsByEmail(email);
	}

}
