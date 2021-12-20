package com.prgrms.needit.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class BaseIntegrationTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

}
