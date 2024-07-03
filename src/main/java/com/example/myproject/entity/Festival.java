package com.example.myproject.entity;

import com.example.myproject.constant.Category;
import com.example.myproject.constant.Progress;
import com.example.myproject.dto.AttractionDTO;
import com.example.myproject.dto.FestivalDTO;
import com.example.myproject.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "festival")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Festival extends BaseTimeEntity {

    @Id
    @Column(name = "fno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    @Enumerated(EnumType.STRING)
    private Progress progress; // 현재 진행 여부

    @Enumerated(EnumType.STRING)
    private Category category; // 지역 카테고리

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    private LocalDate scheduleS;

    private LocalDate scheduleF;

    private int viewCount; // 조회수

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<FAImage> faImgs = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "festival" , cascade = CascadeType.ALL, orphanRemoval = true
            , fetch = FetchType.LAZY)
    private List<Reply> replies = new ArrayList<>();

    public void addFAimg(FAImage faImage) {
        faImgs.add(faImage);
        faImage.setFestival(this);
    }

    public void removeFAimg(FAImage faImage) {
        faImgs.remove(faImage);
        faImage.setFestival(null);
    }

    public void updateFes(FestivalDTO festivalDTO){
        this.title = festivalDTO.getTitle();
        this.category = festivalDTO.getCategory();
        this.progress = festivalDTO.getProgress();
        this.scheduleS = festivalDTO.getScheduleS();
        this.scheduleF = festivalDTO.getScheduleF();
        this.content = festivalDTO.getContent();
        this.viewCount = festivalDTO.getViewCount();
    }

}
