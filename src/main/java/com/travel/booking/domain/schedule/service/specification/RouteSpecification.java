package com.travel.booking.domain.schedule.service.specification;

import com.travel.booking.common.util.StringUtil;
import com.travel.booking.domain.schedule.model.Route;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RouteSpecification {

    private RouteSpecification() {
        /* This utility class should not be instantiated */
    }

    public static Specification<Route> depCityContains(String depCity) {
        return ((root, query, criteriaBuilder) -> {
            if (depCity == null || depCity.isBlank()) {
                return null;
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("depCity")), StringUtil.formattingStringLikeParams(depCity));
        });
    }

    public static Specification<Route> destCityContains(String destCity) {
        return ((root, query, criteriaBuilder) -> {
            if (destCity == null || destCity.isBlank()) {
                return null;
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("destCity")), StringUtil.formattingStringLikeParams(destCity));
        });
    }

    public static Specification<Route> hasPrice(String price) {
        return ((root, query, criteriaBuilder) -> {
            if (price == null || price.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(root.get("price"), new BigDecimal(price));
        });
    }
}
