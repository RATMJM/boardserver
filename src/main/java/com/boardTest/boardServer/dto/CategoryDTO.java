package com.boardTest.boardServer.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor //
public class CategoryDTO {

    public enum SortStatus{
        CATEGORIES, NEWEST, OLDEST, HIGHPRICE, LOWPRICE, GRADE
    }

    private int id;
    private String name;
    private SortStatus sortStatus;
    private int searchCount;
    private int pagingStartOffset;
}
