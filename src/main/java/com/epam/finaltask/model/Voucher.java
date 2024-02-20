package com.epam.finaltask.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private double price;

    @Enumerated(value = EnumType.STRING)
    private TourType tourType;

    @Enumerated(value = EnumType.STRING)
    private TransferType transferType;

    @Enumerated(value = EnumType.STRING)
    private HotelType hotelType;

    @Enumerated(value = EnumType.STRING)
    private VoucherStatus status;

    @Column
    private LocalDate arrivalDate;

    @Column
    private LocalDate evictionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private boolean isHot;
}
