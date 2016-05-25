package com.zoom.multichannelsurvey.dataload;

import com.zoom.multichannelsurvey.dataload.service.DataLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DataLoadApplication implements CommandLineRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DataLoadService dataLoadService;

	public static void main(String[] args) {
		SpringApplication.run(DataLoadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("Exactly 1 parameter required!");
		}

		String path = args[0];
		dataLoadService.readFile(path);
	}
}
