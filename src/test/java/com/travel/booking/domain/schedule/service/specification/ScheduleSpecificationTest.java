package com.travel.booking.domain.schedule.service.specification;

import com.travel.booking.domain.schedule.model.Fleet;
import com.travel.booking.domain.schedule.model.Route;
import com.travel.booking.domain.schedule.model.Schedule;
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

import java.time.LocalDate;
import java.time.Month;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleSpecificationTest {

    @Mock
    private Root<Schedule> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Fleet> fleetPath;

    @Mock
    private Path<Route> routePath;

    @Mock
    private Path<String> plateNoPath;

    @Mock
    private Path<String> modelPath;

    @Mock
    private Path<String> depCityPath;

    @Mock
    private Path<String> destCityPath;

    @Mock
    private Path<OffsetDateTime> depTimePath;

    @Mock
    private Path<OffsetDateTime> arrTimePath;

    @Mock
    private Expression<String> lowerExpression;

    @Mock
    private Predicate predicate;

    @Mock
    private Predicate startPredicate;

    @Mock
    private Predicate endPredicate;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void fleetPlateNoContains_returnsNullForNullOrBlank(String plateNo) {
        Specification<Schedule> specification = ScheduleSpecification.fleetPlateNoContains(plateNo);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void fleetPlateNoContains_buildsLikePredicate() {
        when(root.<Fleet>get("fleet")).thenReturn(fleetPath);
        when(fleetPath.<String>get("plateNo")).thenReturn(plateNoPath);
        when(criteriaBuilder.lower(plateNoPath)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%d 1 ar%")).thenReturn(predicate);

        Specification<Schedule> specification = ScheduleSpecification.fleetPlateNoContains("D 1 AR");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("fleet");
        verify(fleetPath).get("plateNo");
        verify(criteriaBuilder).lower(plateNoPath);
        verify(criteriaBuilder).like(lowerExpression, "%d 1 ar%");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void fleetModelContains_returnsNullForNullOrBlank(String model) {
        Specification<Schedule> specification = ScheduleSpecification.fleetModelContains(model);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void fleetModelContains_buildsLikePredicate() {
        when(root.<Fleet>get("fleet")).thenReturn(fleetPath);
        when(fleetPath.<String>get("model")).thenReturn(modelPath);
        when(criteriaBuilder.lower(modelPath)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%shuttle%")).thenReturn(predicate);

        Specification<Schedule> specification = ScheduleSpecification.fleetModelContains("Shuttle");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("fleet");
        verify(fleetPath).get("model");
        verify(criteriaBuilder).lower(modelPath);
        verify(criteriaBuilder).like(lowerExpression, "%shuttle%");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void routeDepCityContains_returnsNullForNullOrBlank(String depCity) {
        Specification<Schedule> specification = ScheduleSpecification.routeDepCityContains(depCity);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void routeDepCityContains_buildsLikePredicate() {
        when(root.<Route>get("route")).thenReturn(routePath);
        when(routePath.<String>get("depCity")).thenReturn(depCityPath);
        when(criteriaBuilder.lower(depCityPath)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%jakarta%")).thenReturn(predicate);

        Specification<Schedule> specification = ScheduleSpecification.routeDepCityContains("Jakarta");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("route");
        verify(routePath).get("depCity");
        verify(criteriaBuilder).lower(depCityPath);
        verify(criteriaBuilder).like(lowerExpression, "%jakarta%");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void routeDestCityContains_returnsNullForNullOrBlank(String destCity) {
        Specification<Schedule> specification = ScheduleSpecification.routeDestCityContains(destCity);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void routeDestCityContains_buildsLikePredicate() {
        when(root.<Route>get("route")).thenReturn(routePath);
        when(routePath.<String>get("destCity")).thenReturn(destCityPath);
        when(criteriaBuilder.lower(destCityPath)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%bandung%")).thenReturn(predicate);

        Specification<Schedule> specification = ScheduleSpecification.routeDestCityContains("Bandung");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("route");
        verify(routePath).get("destCity");
        verify(criteriaBuilder).lower(destCityPath);
        verify(criteriaBuilder).like(lowerExpression, "%bandung%");
    }

    @Test
    void hasScheduleDepTime_returnsNullForNullDate() {
        Specification<Schedule> specification = ScheduleSpecification.hasScheduleDepTime(null);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void hasScheduleDepTime_buildsDateRangePredicate() {
        OffsetDateTime start = OffsetDateTime.parse("2026-09-25T00:00:00Z");
        OffsetDateTime end = OffsetDateTime.parse("2026-09-25T23:59:59.999999999Z");
        when(root.<OffsetDateTime>get("depTime")).thenReturn(depTimePath);
        when(criteriaBuilder.greaterThanOrEqualTo(depTimePath, start)).thenReturn(startPredicate);
        when(criteriaBuilder.lessThan(depTimePath, end)).thenReturn(endPredicate);
        when(criteriaBuilder.and(startPredicate, endPredicate)).thenReturn(predicate);

        Specification<Schedule> specification = ScheduleSpecification.hasScheduleDepTime(LocalDate.of(2026, Month.SEPTEMBER, 25));

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root, times(2)).get("depTime");
        verify(criteriaBuilder).greaterThanOrEqualTo(depTimePath, start);
        verify(criteriaBuilder).lessThan(depTimePath, end);
        verify(criteriaBuilder).and(startPredicate, endPredicate);
    }

    @Test
    void hasScheduleArrTime_returnsNullForNullDate() {
        Specification<Schedule> specification = ScheduleSpecification.hasScheduleArrTime(null);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void hasScheduleArrTime_buildsDateRangePredicate() {
        OffsetDateTime start = OffsetDateTime.parse("2026-10-01T00:00:00Z");
        OffsetDateTime end = OffsetDateTime.parse("2026-10-01T23:59:59.999999999Z");
        when(root.<OffsetDateTime>get("arrTime")).thenReturn(arrTimePath);
        when(criteriaBuilder.greaterThanOrEqualTo(arrTimePath, start)).thenReturn(startPredicate);
        when(criteriaBuilder.lessThan(arrTimePath, end)).thenReturn(endPredicate);
        when(criteriaBuilder.and(startPredicate, endPredicate)).thenReturn(predicate);

        Specification<Schedule> specification = ScheduleSpecification.hasScheduleArrTime(LocalDate.of(2026, Month.OCTOBER, 1));

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root, times(2)).get("arrTime");
        verify(criteriaBuilder).greaterThanOrEqualTo(arrTimePath, start);
        verify(criteriaBuilder).lessThan(arrTimePath, end);
        verify(criteriaBuilder).and(startPredicate, endPredicate);
    }
}
