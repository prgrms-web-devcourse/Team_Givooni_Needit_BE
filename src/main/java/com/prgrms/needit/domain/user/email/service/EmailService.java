package com.prgrms.needit.domain.user.email.service;

import com.prgrms.needit.common.error.ErrorCode;
import com.prgrms.needit.common.error.exception.NotMatchEmailCodeException;
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

	// 인증코드 만들기
	public static String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 6; i++) { // 인증코드 6자리
			key.append((rnd.nextInt(10)));
		}
		return key.toString();
	}

	private MimeMessage createMessage(String receiver, String code) {
		log.info("보내는 대상 : " + receiver);
		log.info("인증 번호 : " + code);
		try {

			MimeMessage message = emailSender.createMimeMessage();

			String codeWithDash = createCode(code);
			message.addRecipients(RecipientType.TO, receiver); //보내는 대상
			message.setSubject("Need!t 확인 코드: " + codeWithDash); //제목

			String msg = "";
			msg += "<img width=\"120\" height=\"120\" style=\"margin-top: 0; margin-right: 0; margin-bottom: 32px; margin-left: 32px;\" src=\"https://user-images.githubusercontent.com/63666375/144910925-ba033238-f721-47ab-89c2-c4b027ef5479.png\" alt=\"\" loading=\"lazy\">";
			msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
			msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 Need!t 가입 창이 있는 브라우저 창에 입력하세요.</p>";
			msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
			msg += code;
			msg += "</td></tr></tbody></table></div>";
			msg += "<a href=\"#\" style=\"text-decoration: none; color: #434245;\" rel=\"noreferrer noopener\" target=\"_blank\">Need!t, Inc</a>";

			message.setText(msg, "utf-8", "html"); //내용
			message.setFrom(new InternetAddress("needit.mailg@gmail.com", "needit")); //보내는 사람

			return message;
		} catch (Exception e) {
			throw new MailSendException(e.getMessage());
		}
	}

	public void sendMessage(String receiver) {
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
			.get();
		prevEmailCode.changeEmailCode(key);
		emailSender.send(message);
	}

	public void verifyCode(String email, String code) {
		emailCodeRepository.findByEmailAndEmailCode(email, code)
						   .orElseThrow(
							   () -> new NotMatchEmailCodeException(ErrorCode.NOT_MATCH_EMAIL_CODE)
						   );
	}

	public String createCode(String ePw) {
		return ePw.substring(0, 3) + "-" + ePw.substring(3, 6);
	}
}