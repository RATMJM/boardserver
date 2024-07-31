package com.boardTest.boardServer.service.impl;


import com.boardTest.boardServer.dto.CategoryDTO;
import com.boardTest.boardServer.mapper.CategoryMapper;
import com.boardTest.boardServer.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        if(accountId != null){
            categoryMapper.register(categoryDTO);
        }else{
            log.error("register error! {}" , categoryDTO);
            throw new RuntimeException("runtime error! 게시물 카테고리 등록 메서드확인하세요. "+ categoryDTO);
        }
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        if(categoryDTO!=null){
            categoryMapper.updateCategory(categoryDTO);
        }else{
            log.error("register error! {}" , categoryDTO);
            throw new RuntimeException("runtime error! 게시물 업데이트 메서드확인하세요. "+ categoryDTO);
        }
    }

    @Override
    public void delete(int categoryId) {
        if(categoryId != 0){
            categoryMapper.deleteCategory(categoryId);
        }else{
            log.error("register error! {}" , categoryId);
            throw new RuntimeException("runtime error! 게시물 삭제 메서드확인하세요. "+ categoryId);
        }
    }
}
