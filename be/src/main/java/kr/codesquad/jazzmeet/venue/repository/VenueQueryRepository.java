package kr.codesquad.jazzmeet.venue.repository;

import static com.querydsl.core.group.GroupBy.*;
import static kr.codesquad.jazzmeet.image.entity.QImage.*;
import static kr.codesquad.jazzmeet.show.entity.QShow.*;
import static kr.codesquad.jazzmeet.venue.entity.QLink.*;
import static kr.codesquad.jazzmeet.venue.entity.QVenue.*;
import static kr.codesquad.jazzmeet.venue.entity.QVenueHour.*;
import static kr.codesquad.jazzmeet.venue.entity.QVenueImage.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.codesquad.jazzmeet.venue.dto.ShowInfo;
import kr.codesquad.jazzmeet.venue.entity.Venue;
import kr.codesquad.jazzmeet.venue.vo.NearbyVenue;
import kr.codesquad.jazzmeet.venue.vo.VenueDetail;
import kr.codesquad.jazzmeet.venue.vo.VenueDetailImage;
import kr.codesquad.jazzmeet.venue.vo.VenueDetailLink;
import kr.codesquad.jazzmeet.venue.vo.VenueDetailVenueHour;
import kr.codesquad.jazzmeet.venue.vo.VenuePins;
import kr.codesquad.jazzmeet.venue.vo.VenueSearchData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class VenueQueryRepository {

	private static final int NEARBY_VENUES_COUNT = 10;
	private static final String ADDRESS = "address";
	private static final String SHOWINFO = "showInfo";

	private final JPAQueryFactory query;

	public List<NearbyVenue> findNearbyVenuesByLocation(Point point) {
		return query.select(
				Projections.constructor(NearbyVenue.class,
					venue.id,
					venue.name,
					venue.roadNameAddress,
					venue.location,
					venue.thumbnailUrl
				)
			)
			.from(venue)
			.orderBy(
				Expressions.stringTemplate("ST_DISTANCE_SPHERE(venue.location, {0})", point)
					.asc()
			)
			.limit(NEARBY_VENUES_COUNT)
			.fetch();
	}

	public List<VenuePins> findVenuePinsByWord(String word) {
		return query.select(
				Projections.constructor(VenuePins.class,
					venue.id,
					venue.name,
					venue.location)
			)
			.from(venue)
			.where(
				isContainWordInName(word).or(isContainWordInAddress(word))
			)
			.orderBy(venue.id.asc())
			.fetch();
	}

	// 쿼리를 생성하여 사각형 범위 내에 있는 장소를 찾습니다.
	public List<VenuePins> findVenuePinsByLocation(Polygon range) {
		List<VenuePins> venues = query
			.select(Projections.constructor(VenuePins.class,
				venue.id,
				venue.name,
				venue.location))
			.from(venue)
			.where(isLocationWithInRange(range))
			.fetch();

		return venues;
	}

	public Page<VenueSearchData> findVenuesByLocation(Polygon range, Pageable pageable, LocalDate curDate) {
		List<VenueSearchData> venueSearchList = query.from(venue)
			.leftJoin(show)
			.on(venue.id.eq(show.venue.id))
			.on(isStartTimeEqCurDate(curDate))
			.where(isLocationWithInRange(range))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.transform(
				groupBy(venue.id).list(
					Projections.fields(VenueSearchData.class,
						venue.id,
						venue.thumbnailUrl,
						venue.name,
						venue.roadNameAddress.as(ADDRESS),
						venue.description,
						venue.location,
						list(
							Projections.fields(ShowInfo.class,
								show.startTime,
								show.endTime
							)
						).as(SHOWINFO))
				)
			);

		JPAQuery<Long> venuesByLocationCount = getVenuesByLocationCount(range);

		return PageableExecutionUtils.getPage(venueSearchList, pageable, venuesByLocationCount::fetchOne);
	}

	private BooleanExpression isContainWordInName(String word) {
		return venue.name.contains(word);
	}

	private BooleanExpression isContainWordInAddress(String word) {
		return venue.roadNameAddress.contains(word);
	}

	private BooleanExpression isLocationWithInRange(Polygon range) {
		return Expressions.booleanTemplate("ST_Within({0}, {1})", venue.location, range);
	}

	private BooleanExpression isStartTimeEqCurDate(LocalDate curDate) {
		return Expressions.stringTemplate("DATE({0})", show.startTime)
			.eq(Expressions.stringTemplate("DATE({0})", curDate));
	}

	private JPAQuery<Long> getVenuesByLocationCount(Polygon range) {
		return query.select(venue.count())
			.from(venue)
			.where(isLocationWithInRange(range));
	}

	public Page<VenueSearchData> searchVenueList(String word, Pageable pageable, LocalDate curDate) {
		List<VenueSearchData> venueSearchDataList = query.select(venue).from(venue)
			.leftJoin(show)
			.on(venue.id.eq(show.venue.id))
			.on(isStartTimeEqCurDate(curDate))
			.where(isContainWordInName(word).or(isContainWordInAddress(word)))
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.transform(
				groupBy(venue.id).list(
					Projections.fields(VenueSearchData.class,
						venue.id,
						venue.thumbnailUrl,
						venue.name,
						venue.roadNameAddress.as(ADDRESS),
						venue.description,
						venue.location,
						list(
							Projections.fields(ShowInfo.class,
								show.startTime,
								show.endTime
							)).as(SHOWINFO)
					)
				)
			);

		JPAQuery<Long> venuesByWordCount = countSearchVenueList(word);

		return PageableExecutionUtils.getPage(venueSearchDataList, pageable, venuesByWordCount::fetchOne);
	}

	private JPAQuery<Long> countSearchVenueList(String word) {
		return query.select(venue.count())
			.from(venue)
			.where(isContainWordInName(word).or(isContainWordInAddress(word)));
	}

	public List<VenueSearchData> findVenueSearchById(Long venueId, LocalDate curDate) {
		return query.from(venue)
			.leftJoin(show)
			.on(venue.id.eq(show.venue.id))
			.on(isStartTimeEqCurDate(curDate))
			.where(venue.id.eq(venueId))
			.transform(
				groupBy(venue.id).list(
					Projections.fields(VenueSearchData.class,
						venue.id,
						venue.thumbnailUrl,
						venue.name,
						venue.roadNameAddress.as(ADDRESS),
						venue.description,
						venue.location,
						list(
							Projections.fields(ShowInfo.class,
								show.startTime,
								show.endTime
							)).as(SHOWINFO)
					)
				)
			);
	}

	public Optional<VenueDetail> findVenue(Long venueId) {

		List<VenueDetailVenueHour> venueHours = getVenueHours(venueId);
		List<VenueDetailLink> links = getLinks(venueId);
		List<VenueDetailImage> images = getImages(venueId);

		Venue result = query.select(venue)
			.from(venue)
			.where(venue.id.eq(venueId))
			.fetchOne();

		if (result == null) {
			return Optional.empty();
		}

		return Optional.of(VenueDetail.builder()
			.id(result.getId())
			.name(result.getName())
			.roadNameAddress(result.getRoadNameAddress())
			.lotNumberAddress(result.getLotNumberAddress())
			.phoneNumber(result.getPhoneNumber())
			.description(result.getDescription())
			.location(result.getLocation())
			.images(images)
			.links(links)
			.venueHours(venueHours)
			.build());
	}

	private List<VenueDetailImage> getImages(Long venueId) {
		return query.select(Projections.constructor(VenueDetailImage.class,
				image.id,
				image.url))
			.from(venueImage)
			.leftJoin(venueImage.image, image)
			.where(venueImage.venue.id.eq(venueId))
			.fetch();
	}

	private List<VenueDetailLink> getLinks(Long venueId) {
		return query.select(Projections.constructor(VenueDetailLink.class,
				link.linkType,
				link.url))
			.from(link)
			.where(link.venue.id.eq(venueId))
			.fetch();
	}

	private List<VenueDetailVenueHour> getVenueHours(Long venueId) {
		return query.select(Projections.constructor(VenueDetailVenueHour.class,
				venueHour.day,
				venueHour.businessHour))
			.from(venueHour)
			.where(venueHour.venue.id.eq(venueId))
			.fetch();
	}
}
