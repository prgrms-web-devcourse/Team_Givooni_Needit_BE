package com.prgrms.needit.domain.user.email.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.DuplicatedResourceException;
import com.prgrms.needit.common.error.exception.InvalidArgumentException;
import com.prgrms.needit.common.error.exception.NotMatchResourceException;
import com.prgrms.needit.common.domain.service.RedisService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	private final RedisService redisService;

	private static String createCode() {
		StringBuilder code = new StringBuilder();

		for (int i = 0; i < 6; i++) {
			code.append(new Random().nextInt(10));
		}
		return code.toString();
	}

	private MimeMessage createMessage(String receiver, String code) {
		log.info("보내는 대상 : {}", receiver);
		log.info("인증 번호 : {}", code);
		try {

			MimeMessage message = emailSender.createMimeMessage();

			String codeWithDash = dashCode(code);
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
				+ "<div>\n <p style=\"font-family: Pretendard-ExtraBold;\">아래 확인 코드를 Need!t 가입 창이 있는 브라우저 창에 입력하세요.</p>\n"
				+ "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">(해당 코드는 ";
			msg += LocalDateTime.now()
								.plusMinutes(3)
								.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			msg += "에 만료됩니다.)</p> </div>\n <div>\n <div\n style=\"width: fit-content; height: auto; background-color: #E7E6E6; font-size: 3em; border-radius: 5px; padding: 5px; margin-top : 2.5%; font-family: Pretendard-ExtraBold;\">\n ";
			msg += code;
			msg += "\n</div>\n</div>\n</div>\n\n<div style=\"margin-top: 5%; margin-bottom: 5%;\">\n"
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
		if (redisService.getData(receiver) != null) {
			throw new DuplicatedResourceException(ErrorCode.ALREADY_EXIST_EMAIL);
		}

		final String code = createCode();
		redisService.setDataExpire(receiver, code);
		MimeMessage message = createMessage(receiver, code);
		emailSender.send(message);
	}

	public void resendMessage(String receiver) {
		redisService.deleteData(receiver);

		final String code = createCode();
		MimeMessage message = createMessage(receiver, code);
		emailSender.send(message);
	}

	public void verifyCode(String email, String code) {
		String savedCode = redisService.getData(email);
		if (savedCode == null) {
			throw new InvalidArgumentException(ErrorCode.INVALID_INPUT_VALUE);
		}
		if (!savedCode.equals(code)) {
			throw new NotMatchResourceException(ErrorCode.NOT_MATCH_EMAIL_CODE);
		}
	}

	public String dashCode(String code) {
		return code.substring(0, 3) + "-" + code.substring(3, 6);
	}

}
