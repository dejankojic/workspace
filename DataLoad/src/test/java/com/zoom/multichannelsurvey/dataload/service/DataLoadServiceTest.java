package com.zoom.multichannelsurvey.dataload.service;

import au.com.bytecode.opencsv.CSVReader;
import com.zoom.multichannelsurvey.dataload.config.DataLoadConfiguration;
import com.zoom.multichannelsurvey.dataload.rest.model.MsisdnListRequest;
import com.zoom.multichannelsurvey.dataload.rest.model.RangeListRequest;
import com.zoom.multichannelsurvey.dataload.rest.model.RangeRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DataLoadConfiguration.class)
public class DataLoadServiceTest {

	@Autowired
	private DataLoadService dataLoadService;

	@InjectMocks
	private DataLoadService dataLoadServiceMocked;

	@Mock
	private RestTemplate restTemplate;

	private String[] header;

	private String msisdnsCsvFileName;

	private String rangesCsvFileName;

	private MsisdnListRequest msisdnsRequest;

	RangeListRequest rangeListRequest;

	RangeRequest rangeRequest;

	private String addBlacklistedMsisdnsRestUri;

	private String addBlacklistedRangesRestUri;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		msisdnsCsvFileName = "msisdns.csv";
		rangesCsvFileName = "ranges.csv";
		addBlacklistedMsisdnsRestUri = "http://localhost:8080/permanent-blacklist/batch";
		addBlacklistedRangesRestUri = "http://localhost:8080/permanent-blacklist/range/batch";

		msisdnsRequest = new MsisdnListRequest();
		msisdnsRequest.setMsisdns(Arrays.asList("1111", "2222"));

		rangeRequest = new RangeRequest();
		rangeRequest.setMsisdnFrom(Long.parseLong("2222"));
		rangeRequest.setMsisdnFrom(Long.parseLong("6666"));

		rangeListRequest = new RangeListRequest();
		rangeListRequest.setRangeRequests(Arrays.asList(rangeRequest));
	}

	@Test
	public void testLoadMsisdns() throws Exception {
		URL url = getClass().getClassLoader().getResource(msisdnsCsvFileName);
		CSVReader lines = new CSVReader(new FileReader(url.getPath()), ';');
		when(restTemplate.postForObject(addBlacklistedMsisdnsRestUri, msisdnsRequest, MsisdnListRequest.class)).thenReturn(null);
		int result = dataLoadServiceMocked.loadMsisdns(lines);
		assertEquals(2, result);
	}

	@Test
	public void testLoadRanges() throws Exception {
		URL url = getClass().getClassLoader().getResource(rangesCsvFileName);
		CSVReader lines = new CSVReader(new FileReader(url.getPath()), ';');
		when(restTemplate.postForObject(addBlacklistedRangesRestUri, rangeListRequest, RangeListRequest.class)).thenReturn(null);
		int result = dataLoadServiceMocked.loadMsisdns(lines);
		assertEquals(1, result);
	}

	@Test
	public void testGetImportType() {
		header = new String[]{"column1", "column2"};
		String importType = dataLoadService.getImportType(header);
		assertEquals("RANGES", importType);

		header = new String[]{"column1"};
		importType = dataLoadService.getImportType(header);
		assertEquals("MSISDNS", importType);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetImportTypeNull() {
		String importType = dataLoadService.getImportType(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetImportTypeUnexpectedColumnNumbers() {
		header = new String[]{"column1", "column2", "column3"};
		String importType = dataLoadService.getImportType(header);
	}

}
