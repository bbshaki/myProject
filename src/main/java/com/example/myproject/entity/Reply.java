package com.example.myproject.entity;

import com.example.myproject.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.MediaSize;

@Entity
@Table(name = "reply")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply extends BaseTimeEntity {

    @Id
    @Column(name = "rno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY) // 댓글이 달릴 글 번호
    @JoinColumn(name = "fno")
    private Festival festival;

    @ManyToOne(fetch = FetchType.LAZY) // 댓글이 달릴 글 번호
    @JoinColumn(name = "ano")
    private Attraction attraction;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String replier;

    public void changeCom(String comment){
        this.comment = comment;
    }

}
