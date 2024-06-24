package com.example.myproject.entity;

import com.example.myproject.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "todo")
@Getter
@Setter
public class Todo extends BaseTimeEntity {

    @Id
    @Column(name = "todo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
