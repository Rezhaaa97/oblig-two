package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Category;
import no.oslomet.obligtwo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category save(Category category){
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(long category_id){
        return  categoryRepository.findById(category_id).get();
    }
}
