package com.prod.GreenValley.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.GreenValley.repository.SubCategoryRepo;

@Service
public class SubCategoryService {
    
    @Autowired
    private SubCategoryRepo subCategoryRepo;



    public void deleteSubCategoryByParentId(Long parentId){
        subCategoryRepo.deleteSubcategoryByParentId(parentId);
    }


}
