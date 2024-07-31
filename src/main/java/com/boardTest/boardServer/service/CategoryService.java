package com.boardTest.boardServer.service;

import com.boardTest.boardServer.dto.CategoryDTO;

public interface CategoryService{
    void register(String accountId, CategoryDTO categoryDTO);
    void update(CategoryDTO categoryDTO);
    void delete(int categoryId);
}
