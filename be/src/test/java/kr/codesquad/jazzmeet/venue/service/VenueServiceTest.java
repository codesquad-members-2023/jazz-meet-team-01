package kr.codesquad.jazzmeet.venue.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import kr.codesquad.jazzmeet.IntegrationTestSupport;
import kr.codesquad.jazzmeet.venue.dto.response.VenueAutocompleteResponse;
import kr.codesquad.jazzmeet.venue.dto.response.VenuePinsBySearchResponse;
import kr.codesquad.jazzmeet.venue.entity.Venue;
import kr.codesquad.jazzmeet.venue.repository.VenueRepository;
import kr.codesquad.jazzmeet.venue.util.VenueUtil;

class VenueServiceTest extends IntegrationTestSupport {

	@Autowired
	private VenueService venueService;

	@Autowired
	private VenueRepository venueRepository;

	@AfterEach
	void dbClean() {
		venueRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("검색어를 입력하면 공연장 이름에 검색어를 포함하는 공연장 목록을 조회한다.")
	void searchVenueNameAutocompleteList() {
		// given
		String word = "블루";
		Venue venue = createVenue("플랫나인", "서울시 강남구");
		Venue venue2 = createVenue("블루밍", "서울시 서초구");

		venueRepository.save(venue);
		venueRepository.save(venue2);

		// when
		List<VenueAutocompleteResponse> foundVenues = venueService.searchAutocompleteList(word);

		// then
		int size = foundVenues.size();
		assertThat(foundVenues).filteredOn(
				venueAutocompleteResponse -> venueAutocompleteResponse.name().contains(word))
			.hasSize(size);
	}

	@Test
	@DisplayName("검색어를 입력하면 주소에 검색어를 포함하는 공연장 목록을 조회한다.")
	void searchAddressNameAutocompleteList() {
		// given
		String word = "강남";
		Venue venue = createVenue("플랫나인", "서울시 강남구");
		Venue venue2 = createVenue("블루밍", "서울시 서초구");

		venueRepository.save(venue);
		venueRepository.save(venue2);

		// when
		List<VenueAutocompleteResponse> foundVenues = venueService.searchAutocompleteList(word);

		// then
		int size = foundVenues.size();
		assertThat(foundVenues).filteredOn(
				venueAutocompleteResponse -> venueAutocompleteResponse.address().contains(word))
			.hasSize(size);
	}

	@Test
	@DisplayName("공연장 이름과 주소에 검색어를 포함하는 공연장이 없으면 목록이 조회되지 않는다.")
	void searchEmptyListAutocompleteList() {
		// given
		String word = "안양";
		Venue venue = createVenue("플랫나인", "서울시 강남구");
		Venue venue2 = createVenue("블루밍", "서울시 서초구");

		venueRepository.save(venue);
		venueRepository.save(venue2);

		// when
		List<VenueAutocompleteResponse> foundVenues = venueService.searchAutocompleteList(word);

		// then
		assertThat(foundVenues).hasSize(0);
	}

	Venue createVenue(String name, String roadNameAddress) {
		Point point = VenueUtil.createPoint(123.11111, 123.123123);

		return Venue.builder()
			.name(name)
			.roadNameAddress(roadNameAddress)
			.lotNumberAddress("지번주소")
			.location(point)
			.thumbnailUrl("thumbnail.url")
			.build();
	}

	@DisplayName("이름에 검색어가 포함되는 공연장의 위치 데이터 목록을 조회한다.")
	@Test
	public void findVenuesPinsByWordInName() throws Exception {
		//given
		String word = "부기우기";
		Venue venue1 = createVenue("부기우기", "서울 용산구 회나무로 21 2층");
		Venue venue2 = createVenue("Entry55", "서울 동작구 동작대로1길 18 B-102");
		venueRepository.saveAll(List.of(venue1, venue2));

		//when
		List<VenuePinsBySearchResponse> venuePinsList = venueService.findVenuePinsList(word);

		//then
		assertThat(venuePinsList).hasSize(1)
			.extracting("name", "latitude", "longitude")
			.contains(tuple("부기우기", 123.11111, 123.123123));
	}

	@DisplayName("주소에 검색어가 포함되는 공연장의 위치 데이터 목록을 조회한다.")
	@Test
	public void findVenuesPinsByWordInAddress() throws Exception {
		//given
		String word = "서울";
		Venue venue1 = createVenue("부기우기", "서울 용산구 회나무로 21 2층");
		Venue venue2 = createVenue("Entry55", "서울 동작구 동작대로1길 18 B-102");
		venueRepository.saveAll(List.of(venue1, venue2));

		//when
		List<VenuePinsBySearchResponse> venuePinsList = venueService.findVenuePinsList(word);

		//then
		assertThat(venuePinsList).hasSize(2)
			.extracting("name", "latitude", "longitude")
			.contains(
				tuple("부기우기", 123.11111, 123.123123),
				tuple("Entry55", 123.11111, 123.123123));
	}

}