package com.prgrms.needit.common.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:application-dev.yml")
public class EmailConfig {

	@Value("${spring.mail.port}")
	private int port;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean auth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean starttls;

	@Value("${spring.mail.properties.mail.smtp.starttls.required}")
	private boolean starttls_required;

	@Value("${spring.mail.username}")
	private String id;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.properties.mail.smtp.socketFactory.port}")
	private int socketPort;

	@Value("${spring.mail.properties.mail.smtp.socketFactory.fallback}")
	private boolean fallback;

	@Value("${spring.mail.properties.mail.smtp.socketFactory.class}")
	private String socketClass;

	@Value("${spring.mail.properties.mail.smtp.ssl.enable}")
	private boolean ssl_enable;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com");
		javaMailSender.setUsername(id);
		javaMailSender.setPassword(password);
		javaMailSender.setPort(port);
		javaMailSender.setJavaMailProperties(getMailProperties());
		javaMailSender.setDefaultEncoding("UTF-8");
		return javaMailSender;
	}

	private Properties getMailProperties() {
		Properties pt = new Properties();
		pt.put("mail.smtp.socketFactory.port", socketPort);
		pt.put("mail.smtp.auth", auth);
		pt.put("mail.smtp.ssl.enable", ssl_enable);
		pt.put("mail.smtp.starttls.enable", starttls);
		pt.put("mail.smtp.starttls.required", starttls_required);
		pt.put("mail.smtp.socketFactory.fallback", fallback);
		pt.put("mail.smtp.socketFactory.class", socketClass);
		return pt;
	}
}