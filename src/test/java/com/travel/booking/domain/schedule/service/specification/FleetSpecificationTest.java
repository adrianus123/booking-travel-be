package com.travel.booking.domain.schedule.service.specification;

import com.travel.booking.domain.schedule.model.Fleet;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FleetSpecificationTest {

    @Mock
    private Root<Fleet> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<String> plateNoPath;

    @Mock
    private Path<String> modelPath;

    @Mock
    private Path<Integer> tSeatsPath;

    @Mock
    private Expression<String> lowerExpression;

    @Mock
    private Predicate predicate;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void plateNoContains_returnsNullForNullOrBlank(String plateNo) {
        Specification<Fleet> specification = FleetSpecification.plateNoContains(plateNo);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void plateNoContains_buildsLikePredicate() {
        when(root.<String>get("plateNo")).thenReturn(plateNoPath);
        when(criteriaBuilder.lower(plateNoPath)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%d 1 ar%")).thenReturn(predicate);

        Specification<Fleet> specification = FleetSpecification.plateNoContains("D 1 AR");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("plateNo");
        verify(criteriaBuilder).lower(plateNoPath);
        verify(criteriaBuilder).like(lowerExpression, "%d 1 ar%");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void modelContains_returnsNullForNullOrBlank(String model) {
        Specification<Fleet> specification = FleetSpecification.modelContains(model);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void modelContains_buildsLikePredicate() {
        when(root.<String>get("model")).thenReturn(modelPath);
        when(criteriaBuilder.lower(modelPath)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%shuttle%")).thenReturn(predicate);

        Specification<Fleet> specification = FleetSpecification.modelContains("Shuttle");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("model");
        verify(criteriaBuilder).lower(modelPath);
        verify(criteriaBuilder).like(lowerExpression, "%shuttle%");
    }

    @Test
    void hasTotalSeat_returnsNullForNull() {
        Specification<Fleet> specification = FleetSpecification.hasTotalSeat(null);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void hasTotalSeat_buildsEqualPredicate() {
        Integer tSeats = 8;
        when(root.<Integer>get("tSeats")).thenReturn(tSeatsPath);
        when(criteriaBuilder.equal(tSeatsPath, tSeats)).thenReturn(predicate);

        Specification<Fleet> specification = FleetSpecification.hasTotalSeat(tSeats);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("tSeats");
        verify(criteriaBuilder).equal(tSeatsPath, tSeats);
    }
}
