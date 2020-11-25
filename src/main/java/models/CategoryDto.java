package models;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CategoryDto {
    String name;
    String id;

    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getName(), category.getId().toString());
    }

    public static List<CategoryDto> from(List<Category> categories) {
        return categories.stream().map(CategoryDto::from).collect(Collectors.toList());
    }
}
