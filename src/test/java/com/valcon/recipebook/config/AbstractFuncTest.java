package com.valcon.recipebook.config;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
public class AbstractFuncTest {

    @Container
    public static DbTestContainer testContainer = DbTestContainer.getInstance();

}
