package com.example.tollrateservice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RefreshScope
@RestController
public class TollRateController {

	private static final Logger logger = LoggerFactory.getLogger(TollRateController.class);

	@Value("${rate}")
	String rateamount;

	@Value("${lanecount}")
	String lanes;

	@Value("${tollstart}")
	String tollstart;

	@Value("${tollstop}")
	String tollstop;

	List<TollRate> tollrates;
	List<TollRatev2> tollratesv2;
	List<TollRateFromProperty> tollratesproperty = new ArrayList<TollRateFromProperty>();

	public TollRateController() {

		tollrates = new ArrayList<TollRate>();
		tollrates.add(new TollRate(1000, 0.55f, Instant.now().toString()));
		tollrates.add(new TollRate(1001, 1.05f, Instant.now().toString()));
		tollrates.add(new TollRate(1002, 0.65f, Instant.now().toString()));
		tollrates.add(new TollRate(1003, 1.05f, Instant.now().toString()));

		tollratesv2 = new ArrayList<TollRatev2>();
		tollratesv2.add(new TollRatev2(1000, 0.58f, Instant.now().toString(), "Yes"));
		tollratesv2.add(new TollRatev2(1001, 1.08f, Instant.now().toString(), "No"));
		tollratesv2.add(new TollRatev2(1002, 0.68f, Instant.now().toString(), "No"));
		tollratesv2.add(new TollRatev2(1003, 1.08f, Instant.now().toString(), "Yes"));
	}

	@Operation(summary = "Get all the toll rates v1")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Return toll rate - v1", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TollRate.class))})})
	@RequestMapping("/tollrate/{stationId}")
	public TollRate GetTollRate(@PathVariable int stationId) {
		System.out.println("Station requested: " + stationId);
		logger.info("Station requested: " + stationId);
		return tollrates.stream().filter(rate -> stationId == rate.getStationId()).findAny().orElse(new TollRate());
	}

	@Operation(summary = "Get all the toll rates v2")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Return toll rate - v2", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TollRate.class))})})
	@RequestMapping("/tollratev2/{stationId}")
	public TollRatev2 GetTollRatev2(@PathVariable int stationId) {
		System.out.println("Station v2 requested: " + stationId);
		logger.info("Station v2 requested: " + stationId);
		return tollratesv2.stream().filter(rate -> stationId == rate.getStationId()).findAny().orElse(new TollRatev2());
	}

	@Operation(summary = "Get all the toll rates slow")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Return toll rate - slow", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TollRate.class))})})
	@RequestMapping("/tollrateslow/{stationId}")
	public TollRate GetTollRateSlow(@PathVariable int stationId) throws InterruptedException {

		Thread.sleep(3000);
		System.out.println("Station slow requested: " + stationId);
		logger.info("Station slow requested: " + stationId);
		return tollrates.stream().filter(rate -> stationId == rate.getStationId()).findAny().orElse(new TollRate());
	}

	@Operation(summary = "Get all the toll rates property")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Return toll rate - property", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TollRate.class))})})
	@RequestMapping("/property/rate")
	public TollRateFromProperty GetTollRateFromProperty() throws InterruptedException {
		System.out.println("Station from property: rate: " + rateamount + ", lane: " + lanes + ", tollstart: "
				+ tollstart + ", tollstop: " + tollstop);
		logger.info("Station from property: rate: " + rateamount + ", lane: " + lanes + ", tollstart: " + tollstart
				+ ", tollstop: " + tollstop);
		return tollratesproperty.stream().filter(rate -> "100" == rate.getLanes()).findAny()
				.orElse(new TollRateFromProperty(rateamount, lanes, tollstart, tollstop));

	}
}
