package com.example.myproject.dto;

import com.example.myproject.constant.Category;
import com.example.myproject.constant.Progress;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttractionSearchDTO {

    private String searchDate;

    private Category category;

    private String searchBy;

    private String searchQuery = "";
}
