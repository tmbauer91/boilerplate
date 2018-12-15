package com.boilerplate.missionDatabase;

import static org.assertj.core.api.Assertions.*;

import com.boilerplate.missionDatabase.mission.Mission;
import com.boilerplate.missionDatabase.mission.MissionRepository;
import com.boilerplate.missionDatabase.mission.MissionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseApplicationTests {

	@Autowired
	MissionRepository repository;

	Mission DCSTest, ArmaTest;

	@Before
	public void setUp() throws IOException {
		repository.deleteAll();

		MultipartFile DCSTestFile = new MockMultipartFile("file", "DCSTest.miz",
				"application/zip", "Spring Framework".getBytes());
		DCSTest = repository.save(new Mission(DCSTestFile));

		MultipartFile armaTestFile = new MockMultipartFile("file", "Doberman.Altis.pbo",
				"application/zip", "Spring Framework".getBytes());
		ArmaTest = repository.save(new Mission(armaTestFile));

	}


	@Test
	public void findAll() {
		List<Mission> result = repository.findAll();

		assertThat(result.get(0)).extracting("name").contains("DCSTest.miz");
		assertThat(result.get(1)).extracting("name").contains("Doberman.Altis.pbo");
	}

	@Test
	public void findByName() {
		Mission result = repository.findByName("DCSTest.miz");
		assertThat(result).extracting("name").contains("DCSTest.miz");
	}

	@Test public void findByType() {
		MissionType type = MissionType.ARMA;
		List<Mission> results = repository.findByType(type);
		System.out.println(results.toString());
		System.out.println(results.get(0).toString());
		assertThat(results.get(0)).extracting("type").extracting("game").contains("ARMA");


	}

}
