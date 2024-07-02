package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @JoinColumn(name = "fno")
    private Festival festival;

    @ManyToOne
    @JoinColumn(name = "ano")
    private Attraction attraction;

}
