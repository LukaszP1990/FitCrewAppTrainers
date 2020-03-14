package com.fitcrew.FitCrewAppTrainers.resource;

import javax.inject.Inject;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

import com.fitcrew.FitCrewAppTrainers.FitCrewAppTrainersApplication;

@TestPropertySource(locations = {"classpath:application-test.properties"})
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = FitCrewAppTrainersApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractRestResourceTest {

	@Inject
	protected TestRestTemplate restTemplate;
}
