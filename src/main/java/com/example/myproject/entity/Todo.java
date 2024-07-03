package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Getter
@Setter
@Table(name = "todo")
@ToString
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tno")
    private Long tno;

    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberUser memberUser;

    @ManyToOne
    @JoinColumn(name = "fno", unique = true)
    private Festival festival;

    @ManyToOne
    @JoinColumn(name = "ano", unique = true)
    private Attraction attraction;

}
