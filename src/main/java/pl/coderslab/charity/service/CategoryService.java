package pl.coderslab.charity.service;

import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.CategoryRepository;
import pl.coderslab.charity.dto.CategoryDto;
import pl.coderslab.charity.utils.DtoUtils;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.utils.DtoEntity;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<DtoEntity> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new DtoUtils().convertToDtoList(categories, new TypeToken<List<CategoryDto>>() {
        }.getType(), null);
    }
}
