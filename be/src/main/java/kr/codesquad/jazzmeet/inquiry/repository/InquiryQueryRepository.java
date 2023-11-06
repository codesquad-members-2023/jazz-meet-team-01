package kr.codesquad.jazzmeet.inquiry.repository;

import static kr.codesquad.jazzmeet.inquiry.entity.QInquiry.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.codesquad.jazzmeet.inquiry.vo.InquirySearch;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class InquiryQueryRepository {

	private final JPAQueryFactory query;

	public Page<InquirySearch> searchInquiries(String word, String category, Pageable pageable) {
		List<InquirySearch> inquiries = query.select(
				Projections.fields(InquirySearch.class,
					inquiry.id,
					inquiry.status,
					inquiry.content,
					inquiry.nickname,
					inquiry.createdAt
				))
			.from(inquiry)
			.where(isContainWordInNickname(word).or(isContainWordInContent(word)).and(isEqualsCategory(category)))
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset()).fetch();

		JPAQuery<Long> inquiriesByWordCount = countInquiries(word);

		return PageableExecutionUtils.getPage(inquiries, pageable, inquiriesByWordCount::fetchOne);
	}

	private JPAQuery<Long> countInquiries(String word) {
		return query.select(inquiry.count())
			.from(inquiry)
			.where(isContainWordInNickname(word).or(isContainWordInContent(word)));
	}

	private BooleanExpression isContainWordInNickname(String word) {
		return inquiry.nickname.contains(word);
	}

	private BooleanExpression isContainWordInContent(String word) {
		return inquiry.content.contains(word);
	}

	private BooleanExpression isEqualsCategory(String category) {
		if (category == null || category.equals("")) {
			return null;
		}
		return inquiry.category.eq(category);
	}

}
