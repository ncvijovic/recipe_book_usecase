package com.valcon.recipebook;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.valcon.recipebook.config.AbstractFuncTest;

@SpringBootTest
class RecipebookApplicationTests extends AbstractFuncTest {

	@Test
	void contextLoads() {
		// basic test to check if the context loads
		assertTrue(true);
	}

}
