package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "f_a_image")
@Getter
@Setter
public class FAImage {

    @Id
    @Column(name = "ino")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fno")
    private Festival festival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ano")
    private Attraction attraction;

    private String imgName;

    private String originName;

    private String imgUrl;

    private String repImgYn; // 대표 이미지 여부

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }
    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public void updateImg(String originName, String imgName, String imgUrl){
        this.originName = originName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
