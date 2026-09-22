package com.travel.booking.domain.schedule.service.specification;

import com.travel.booking.common.util.StringUtil;
import com.travel.booking.domain.schedule.model.Fleet;
import org.springframework.data.jpa.domain.Specification;

public class FleetSpecification {

    private FleetSpecification() {
        /* This utility class should not be instantiated */
    }

    public static Specification<Fleet> plateNoContains(String plateNo) {
        return ((root, query, criteriaBuilder) -> {
            if (plateNo == null || plateNo.isBlank()) {
                return null;
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("plateNo")), StringUtil.formattingStringLikeParams(plateNo));
        });
    }

    public static Specification<Fleet> modelContains(String model) {
        return ((root, query, criteriaBuilder) -> {
            if (model == null || model.isBlank()) {
                return null;
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), StringUtil.formattingStringLikeParams(model));
        });
    }

    public static Specification<Fleet> hasTotalSeat(Integer tSeats) {
        return ((root, query, criteriaBuilder) -> {
            if (tSeats == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("tSeats"), tSeats);
        });
    }
}
