package com.api.tutorjoint.dto;


public class CategoryDto {

    private Long id;
    private String description;
    private boolean flag;
    private CategoryDto[] subCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public CategoryDto[] getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(CategoryDto[] subCategories) {
        this.subCategories = subCategories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
