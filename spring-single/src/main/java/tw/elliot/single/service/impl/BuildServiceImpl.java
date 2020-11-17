package tw.elliot.single.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tw.elliot.single.service.BuildService;

@Slf4j
@Service
public class BuildServiceImpl implements BuildService {
	@Override
	public void build() {
		log.info("OK!");
	}
}
