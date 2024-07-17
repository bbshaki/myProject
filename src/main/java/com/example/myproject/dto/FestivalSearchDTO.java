package com.example.myproject.dto;

import com.example.myproject.constant.Category;
import com.example.myproject.constant.Progress;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FestivalSearchDTO {

    private String searchDate;

    private Category category;

    private Progress progress;

    private String searchBy;

    private String searchQuery = "";
}
