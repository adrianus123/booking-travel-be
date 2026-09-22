package com.travel.booking.domain.schedule.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "routes", schema = "public")
public class Route implements Serializable {

    @Serial
    private static final long serialVersionUID = -5982667305012286803L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dep_city", length = 100)
    private String depCity;

    @Column(name = "dest_city", length = 100)
    private String destCity;

    @Column(name = "price", precision = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "route")
    private List<Schedule> schedules = new LinkedList<>();
}
