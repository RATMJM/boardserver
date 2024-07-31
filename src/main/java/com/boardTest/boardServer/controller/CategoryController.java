package com.boardTest.boardServer.controller;

import com.boardTest.boardServer.aop.LoginCheck;
import com.boardTest.boardServer.dto.CategoryDTO;
import com.boardTest.boardServer.service.impl.CategoryServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@Log4j2
public class CategoryController {
    private CategoryServiceImpl categoryService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService){
        this.categoryService = categoryService; // 생성자 주입
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.ADMIN) // 어드민일때만 가능하도록 aop
    public void registerCategory(String accountId, @RequestBody CategoryDTO categoryDTO){
        categoryService.register(accountId, categoryDTO);
    }

    @PatchMapping("{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void updateCategories(String accountId,
                                 @PathVariable(name = "categoryId") int categoryId,
                                 @RequestBody CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, categoryRequest.getName(), CategoryDTO.SortStatus.NEWEST, 10,1 );
        categoryService.update(categoryDTO);
    }

    @DeleteMapping("{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void deleteCategories(String accountId, @PathVariable(name= "categoryId") int categoryId) // 첫번째는 aop를 통해  넘어온 accountId
    {
        categoryService.delete(categoryId);
    }
    // inner class--- request 객체
    @Getter
    @Setter
    private static class CategoryRequest{
        private int id;
        private String name;
    }
}
