package com.zoom.multichannelsurvey.dataload.service;

import au.com.bytecode.opencsv.CSVReader;
import com.zoom.multichannelsurvey.dataload.rest.model.MsisdnListRequest;
import com.zoom.multichannelsurvey.dataload.rest.model.RangeListRequest;
import com.zoom.multichannelsurvey.dataload.rest.model.RangeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataLoadService {

	Logger logger = LoggerFactory.getLogger(DataLoadService.class);

	private static final String IMPORT_MSISDNS = "MSISDNS";

	private static final String IMPORT_RANGE = "RANGES";

	@Autowired
	private RestTemplate restTemplate;

	@Value("${rest.api.uri.add.blacklisted.msisdns}")
	private String addBlacklistedMsisdnsRestUri;

	@Value("${rest.api.uri.add.blacklisted.ranges}")
	private String addBlacklistedRangesRestUri;

	public void readFile(String path) {
		CSVReader lines = null;
		int importedRows = 0;

		try {
			logger.info("Trying to read CSV file from path: " + path);
			lines = new CSVReader(new FileReader(path), ';');

			// Read and skip first line
			String[] header = lines.readNext();

			// Determinate CSV file type
			String importType = getImportType(header);

			switch (importType) {
				case IMPORT_MSISDNS:
					loadMsisdns(lines);
					break;
				case IMPORT_RANGE:
					loadRanges(lines);
					break;
			}
			logger.info("Import process completed successfully. Number of imported rows: " + importedRows);
		} catch (Exception e) {
			logger.error("Error while reading and parsing CSV file.", e);
			e.printStackTrace();
		} finally {
			try {
				if (lines != null) {
					lines.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int loadMsisdns(CSVReader lines) throws Exception {
		int rows = 0;
		String[] nextLine;
		MsisdnListRequest msisdns = new MsisdnListRequest();
		List<String> msisdnList = new ArrayList<String>();

		logger.info("Parsing MSISNDs from CSV file...");

		while ((nextLine = lines.readNext()) != null) {
			String msisdn = nextLine[0].trim();
			msisdnList.add(msisdn);
			rows++;
		}

		logger.info("CSV file parsed successfully. Rows to import: " + rows);

		msisdns.setMsisdns(msisdnList);
		restTemplate.postForObject(addBlacklistedMsisdnsRestUri, msisdns, MsisdnListRequest.class);
		return rows;
	}

	public int loadRanges(CSVReader lines) throws Exception {
		int rows = 0;
		String[] nextLine;
		RangeListRequest rangeListRequest = new RangeListRequest();
		List<RangeRequest> rangeRequests = new ArrayList<RangeRequest>();

		logger.info("Parsing ranges from CSV file...");

		while ((nextLine = lines.readNext()) != null) {
			String msisdnFromStr = nextLine[0].trim();
			String msisdnToStr = nextLine[1].trim();

			Long msisdnFrom = Long.parseLong(msisdnFromStr);
			Long msisdnTo = Long.parseLong(msisdnToStr);

			RangeRequest rangeRequest = new RangeRequest();
			rangeRequest.setMsisdnFrom(msisdnFrom);
			rangeRequest.setMsisdnTo(msisdnTo);
			rangeRequests.add(rangeRequest);
			rows++;
		}

		logger.info("CSV file parsed successfully. Rows to import: " + rows);

		rangeListRequest.setRangeRequests(rangeRequests);
		restTemplate.postForObject(addBlacklistedRangesRestUri, rangeListRequest, RangeListRequest.class);
		return rows;
	}

	public String getImportType(String[] header) {
		if (header == null) {
			throw new IllegalArgumentException(
							"CSV without header provided. CSV file must have header with one or two columns.");
		}

		switch (header.length) {
			case 1:
				logger.info("Import file type: " + IMPORT_MSISDNS);
				return IMPORT_MSISDNS;
			case 2:
				logger.info("Import file type: " + IMPORT_RANGE);
				return IMPORT_RANGE;
			default:
				logger.error("Unparsable CSV file passed. File must have one or two columns.");
				throw new IllegalArgumentException("Unparsable CSV file passed. File must have one or two columns.");
		}
	}
}
