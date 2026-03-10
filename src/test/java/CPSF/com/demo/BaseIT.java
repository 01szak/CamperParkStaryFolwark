package CPSF.com.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;

@Testcontainers
@SpringBootTest(classes = CamperparkdemoApplication.class)
@ActiveProfiles("test")
@Transactional
public class BaseIT {

    private static long eachTestStart;
    private static long testStart;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.32")
            .withDatabaseName("test_camper_park_sf")
            .withUsername("root")
            .withPassword("qwer");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

    @BeforeAll
    public static void beforeAll() {
        testStart = new Date().getTime();
    }

    @AfterAll
    public static void afterAll() {
        var testTime = (new Date().getTime() - testStart);
        System.out.printf("\nTOOK OVERALL: %s ms\n", testTime);
    }

    @BeforeEach
    public void before() {
        eachTestStart = new Date().getTime();
        System.out.println("\n-----------< TEST START >-----------");
    }

    @AfterEach
    public void after() {
        var testTime = (new Date().getTime() - eachTestStart);
        System.out.println("\n-----------< TEST END >-----------");
        System.out.printf("TOOK: %s ms", testTime);
    }

    @Test
    public void isContainerRunning() {
        Assertions.assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
    }

}
