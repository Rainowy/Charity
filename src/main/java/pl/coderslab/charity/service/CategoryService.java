package pl.coderslab.charity.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.Repository.CategoryRepository;
import pl.coderslab.charity.dto.CategoryDto;
import pl.coderslab.charity.entity.Category;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {

//        Pageable pageableRequest = PageRequest.of(page, limit);
//        Page<UserEntity> users = userRepository.findAll(pageableRequest);
//        List<UserEntity> userEntities = users.getContent();

        List<Category> categoriesDto = categoryRepository.findAll();

// Create Conversion Type
        Type listType = new TypeToken<List<CategoryDto>>() {
        }.getType();

// Convert List of Entity objects to a List of DTOs objects
        return new ModelMapper().map(categoriesDto, listType);
    }
}
