package CatsAndOwners.model.entity;

import CatsAndOwners.model.enums.CatBreed;
import CatsAndOwners.model.enums.CatColor;
import CatsAndOwners.model.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cats")
@Getter
@Setter
@NoArgsConstructor
public class Cat {
    @Id
    @UuidGenerator
    @Column(name = "cat_id", columnDefinition = "UUID")
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private CatBreed breed;

    @Enumerated(EnumType.STRING)
    private CatColor color;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany
    @JoinTable(
            name = "cat_friendships",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Cat> friends = new ArrayList<>();
}