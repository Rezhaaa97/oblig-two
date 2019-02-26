package no.oslomet.obligtwo.service;

import no.oslomet.obligtwo.model.Category;

import java.util.List;

public interface CategoryService {
    Category save(Category category);
    List<Category> findAll();
    Category findById(long category_id);
}
