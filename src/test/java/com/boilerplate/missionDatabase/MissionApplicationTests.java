package com.boilerplate.missionDatabase;

import static org.assertj.core.api.Assertions.*;

import com.boilerplate.missionDatabase.mission.Mission;
import com.boilerplate.missionDatabase.mission.MissionRepository;
import com.boilerplate.missionDatabase.mission.MissionType;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class MissionApplicationTests {

	@Autowired
	MissionRepository repository;

	Mission DCSTest, ArmaTest;

	static MongodExecutable mongodExecutable;

	@BeforeClass
	public static void setup() throws Exception {
		MongodStarter starter = MongodStarter.getDefaultInstance();
		String bindIp = "localhost";
		int port = 12345;
		IMongodConfig mongodConfig = new MongodConfigBuilder()
				.version(Version.Main.PRODUCTION)
				.net(new Net(bindIp, port, Network.localhostIsIPv6()))
				.build();
		mongodExecutable = null;
		try {
			mongodExecutable = starter.prepare(mongodConfig);
			mongodExecutable.start();
		} catch (Exception e){
			// log exception here
			if (mongodExecutable != null)
				mongodExecutable.stop();
		}
	}

	@AfterClass
	public static void teardown() throws Exception {
		if (mongodExecutable != null)
			mongodExecutable.stop();
	}

	@Before
	public void setUpRepo() throws IOException {

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
	public void findMissionByName() {
		Mission result = repository.findByName("DCSTest.miz");
		assertThat(result).extracting("name").contains("DCSTest.miz");
	}

	@Test
	public void findMissionByType() {
		MissionType type = MissionType.ARMA;
		List<Mission> results = repository.findByType(type);
		System.out.println(results.toString());
		System.out.println(results.get(0).toString());
		assertThat(results.get(0)).extracting("type").extracting("game").contains("ARMA");
	}

	@Test
	public void addBriefingMaterials() {
		Mission result = repository.findByName("DCSTest.miz");
		assertThat(result).extracting("name").contains("DCSTest.miz");
		Mission originalMission = result;

		MultipartFile imageTest1 = new MockMultipartFile("image.png", "image.png",
				"image/png", "A VERY pretty picture".getBytes());
		MultipartFile imageTest2 = new MockMultipartFile("image2.png", "image2.png",
				"image/png", "A VERY boring picture".getBytes());
		MultipartFile[] fileArray = {imageTest1, imageTest2};

		try {
			result.addBriefingFile(fileArray);
		} catch (IOException e) {
			System.out.println("Something went very badly + " + e);
		}

		assertThat(result).extracting("fileData").isNotEqualTo(originalMission.getFileData());
	}

	@Test
	public void deleteMissionByName() {
		Mission result = repository.findByName("DCSTest.miz");
		repository.delete(result);

		Mission result2 = repository.findByName("DCSTest.miz");
		assertThat(result2).isNull();
	}
}
