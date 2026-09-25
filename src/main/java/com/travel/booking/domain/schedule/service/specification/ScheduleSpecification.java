package com.travel.booking.domain.schedule.service.specification;

import com.travel.booking.common.util.DateUtil;
import com.travel.booking.common.util.StringUtil;
import com.travel.booking.domain.schedule.model.Schedule;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class ScheduleSpecification {

    private ScheduleSpecification() {
        /* This utility class should not be instantiated */
    }

    public static Specification<Schedule> fleetPlateNoContains(String plateNo) {
        return ((root, query, cb) -> {
            if (!StringUtils.hasText(plateNo)) {
                return null;
            }

            return cb.like(cb.lower(root.get("fleet").get("plateNo")), StringUtil.formattingStringLikeParams(plateNo));
        });
    }

    public static Specification<Schedule> fleetModelContains(String model) {
        return ((root, query, cb) -> {
            if (!StringUtils.hasText(model)) {
                return null;
            }

            return cb.like(cb.lower(root.get("fleet").get("model")), StringUtil.formattingStringLikeParams(model));
        });
    }

    public static Specification<Schedule> routeDepCityContains(String depCity) {
        return ((root, query, cb) -> {
            if (!StringUtils.hasText(depCity)) {
                return null;
            }

            return cb.like(cb.lower(root.get("route").get("depCity")), StringUtil.formattingStringLikeParams(depCity));
        });
    }

    public static Specification<Schedule> routeDestCityContains(String destCity) {
        return ((root, query, cb) -> {
            if (!StringUtils.hasText(destCity)) {
                return null;
            }

            return cb.like(cb.lower(root.get("route").get("destCity")), StringUtil.formattingStringLikeParams(destCity));
        });
    }

    public static Specification<Schedule> hasScheduleDepTime(LocalDate depTime) {
        return ((root, query, cb) -> {
            if (depTime == null) {
                return null;
            }

            OffsetDateTime startTime = DateUtil.getEarliestTime(depTime);
            OffsetDateTime endTime = DateUtil.getLatestTime(depTime);

            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("depTime"), startTime),
                    cb.lessThan(root.get("depTime"), endTime)
            );
        });
    }

    public static Specification<Schedule> hasScheduleArrTime(LocalDate arrTime) {
        return ((root, query, cb) -> {
            if (arrTime == null) {
                return null;
            }

            OffsetDateTime startTime = DateUtil.getEarliestTime(arrTime);
            OffsetDateTime endTime = DateUtil.getLatestTime(arrTime);

            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("arrTime"), startTime),
                    cb.lessThan(root.get("arrTime"), endTime)
            );
        });
    }
}
