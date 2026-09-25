package com.travel.booking.domain.schedule.service.specification;

import com.travel.booking.domain.schedule.model.Route;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteSpecificationTest {

    @Mock
    private Root<Route> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<String> depCityPath;

    @Mock
    private Path<String> destCityPath;

    @Mock
    private Path<BigDecimal> pricePath;

    @Mock
    private Expression<String> lowerExpression;

    @Mock
    private Predicate predicate;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void depCityContains_returnsNullForNullOrBlank(String depCity) {
        Specification<Route> specification = RouteSpecification.depCityContains(depCity);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void depCityContains_buildsLikePredicate() {
        when(root.<String>get("depCity")).thenReturn(depCityPath);
        when(criteriaBuilder.lower(depCityPath)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%jakarta%")).thenReturn(predicate);

        Specification<Route> specification = RouteSpecification.depCityContains("Jakarta");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("depCity");
        verify(criteriaBuilder).lower(depCityPath);
        verify(criteriaBuilder).like(lowerExpression, "%jakarta%");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void destCityContains_returnsNullForNullOrBlank(String destCity) {
        Specification<Route> specification = RouteSpecification.destCityContains(destCity);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void destCityContains_buildsLikePredicate() {
        when(root.<String>get("destCity")).thenReturn(destCityPath);
        when(criteriaBuilder.lower(destCityPath)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%bandung%")).thenReturn(predicate);

        Specification<Route> specification = RouteSpecification.destCityContains("Bandung");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("destCity");
        verify(criteriaBuilder).lower(destCityPath);
        verify(criteriaBuilder).like(lowerExpression, "%bandung%");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void hasPrice_returnsNullForNullOrBlank(String price) {
        Specification<Route> specification = RouteSpecification.hasPrice(price);

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isNull();

        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void hasPrice_buildsEqualPredicate() {
        BigDecimal price = new BigDecimal("150000.00");
        when(root.<BigDecimal>get("price")).thenReturn(pricePath);
        when(criteriaBuilder.equal(pricePath, price)).thenReturn(predicate);

        Specification<Route> specification = RouteSpecification.hasPrice("150000.00");

        assertThat(specification.toPredicate(root, query, criteriaBuilder)).isSameAs(predicate);

        verify(root).get("price");
        verify(criteriaBuilder).equal(pricePath, price);
    }
}
