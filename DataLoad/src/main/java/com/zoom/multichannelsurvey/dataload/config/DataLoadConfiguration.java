package com.zoom.multichannelsurvey.dataload.config;

import com.zoom.multichannelsurvey.dataload.service.DataLoadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DataLoadConfiguration {

	@Bean
	public RestTemplate restTemplae() {
		return new RestTemplate();
	}

	@Bean
	public DataLoadService dataLoadService() {
		return new DataLoadService();
	}
}
