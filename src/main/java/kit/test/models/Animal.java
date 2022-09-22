package artplancom.test.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "animals")
@Getter
@Setter
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long animalId;

    @Column
    private String species;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthDate;

    @Column
    private String sex;

    @Column(unique = true)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(name = "user_animals",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "animal_id"))
    @JsonIgnore
    private User user;
}
