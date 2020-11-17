package tw.elliot.single.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tw.elliot.single.service.BuildService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BuildServiceImplTest {

	@Autowired
	private BuildService buildService;

	@Test
	public void test() {
		this.buildService.build();
	}
}