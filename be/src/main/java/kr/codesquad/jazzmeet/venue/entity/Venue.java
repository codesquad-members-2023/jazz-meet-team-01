package kr.codesquad.jazzmeet.venue.entity;

import org.springframework.data.geo.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Venue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String roadNameAddress; // 도로명
	@Column(nullable = false)
	private String lotNumberAddress; // 지번
	private String phoneNumber;
	private String description;
	@Column(nullable = false, columnDefinition = "point")
	private Point location;
	private Long adminId;
	@Embedded
	private Images images;
	@Embedded
	private Links links;
}
