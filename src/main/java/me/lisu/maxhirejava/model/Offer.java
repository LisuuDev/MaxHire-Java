package me.lisu.maxhirejava.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "offers")
@Data
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 40)
    private String title;

    @Column(length = 40)
    private String company;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String tech;

    @Column(columnDefinition = "TEXT")
    private String links;

    @Column(length = 50)
    private String updated;

    @Column(name = "user_id", length = 36)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private User user;
}
