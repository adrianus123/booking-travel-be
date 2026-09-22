package com.travel.booking.domain.schedule.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fleets", schema = "public")
public class Fleet implements Serializable {

    @Serial
    private static final long serialVersionUID = 5262776040732622510L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "plate_no", length = 20)
    private String plateNo;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "t_seats")
    private Integer tSeats;

    @OneToMany(mappedBy = "fleet")
    private List<Schedule> schedules = new LinkedList<>();
}
