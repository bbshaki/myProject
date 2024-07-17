package com.example.myproject.entity;

import com.example.myproject.constant.Category;
import com.example.myproject.dto.AttractionDTO;
import com.example.myproject.dto.FAImgDTO;
import com.example.myproject.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attraction")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attraction extends BaseTimeEntity {

    @Id
    @Column(name = "ano")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    private int viewCount; // 조회수

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<FAImage> faImgs = new ArrayList<>();

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "attraction" , cascade = CascadeType.ALL, orphanRemoval = true
            , fetch = FetchType.LAZY)
    private List<Reply> replies = new ArrayList<>();

    public void addFAimg(FAImage faImage) {
        faImgs.add(faImage);
        faImage.setAttraction(this);
    }

    public void removeFAimg(FAImage faImage) {
        faImgs.remove(faImage);
        faImage.setAttraction(null);
    }

    public void updateAtt(AttractionDTO attractionDTO){
        this.title = attractionDTO.getTitle();
        this.category = attractionDTO.getCategory();
        this.content = attractionDTO.getContent();
        this.writer = attractionDTO.getWriter();
        this.viewCount = attractionDTO.getViewCount();
    }
}
