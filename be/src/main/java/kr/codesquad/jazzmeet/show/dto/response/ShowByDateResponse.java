package kr.codesquad.jazzmeet.show.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ShowByDateResponse(
	Long id,
	String posterUrl,
	String teamName,
	String description,
	LocalDateTime startTime,
	LocalDateTime endTime
) {
}
