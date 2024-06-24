package com.example.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type;
    private String keyword;
    private String link;
    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }
    public Pageable getPageable(String...props){
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }

    public String getLink(){
        if(link == null){
            StringBuffer buffer = new StringBuffer();
            buffer.append("page=" + this.page);
            buffer.append("&size=" + this.size);

            if(type != null && type.length() > 0){
                buffer.append("&type=" + type);
            }
            if(keyword != null){
                try {
                    buffer.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e){}
            }
            link = buffer.toString();
        }
        return link;
    }

}
