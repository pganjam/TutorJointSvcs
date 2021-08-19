package com.api.tutorjoint.service;

import com.api.tutorjoint.dto.PartySubCategoryDto;
import com.api.tutorjoint.exception.DataNotFoundException;
import com.api.tutorjoint.model.SubCategoryPartyData;
import com.api.tutorjoint.exception.PartyNotFoundException;
import com.api.tutorjoint.model.*;
import com.api.tutorjoint.repository.SubCategoryPartyRepository;
import com.api.tutorjoint.repository.PartyRepository;
import com.api.tutorjoint.repository.SubCategoryRepository;
import com.api.tutorjoint.dto.CategoryDto;
import com.api.tutorjoint.exception.PostNotFoundException;
import com.api.tutorjoint.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryPartyRepository subCategoryPartyRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public List<PartySubCategoryDto> getCategories(Long party_id) throws RuntimeException {
        Party party = partyRepository.findById(party_id).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found"));

        List<SubCategoryPartyData> partyCategoryList = subCategoryPartyRepository.fetchPartiesCategories(party_id);

        List<PartySubCategoryDto> partySubCategoryDtoList = new ArrayList<PartySubCategoryDto>();
        for (int i = 0; i < partyCategoryList.size(); ) {
            SubCategoryPartyData element = partyCategoryList.get(i);

            //Initialize Category
            PartySubCategoryDto categoryDto = new PartySubCategoryDto();
            categoryDto.setId(element.getCategory_id());
            categoryDto.setName(element.getCategory_desc());
            categoryDto.setFlag(false);

            List<PartySubCategoryDto> subCategoryDtoList = new ArrayList<>();

            boolean catFlag = true;

            for (int j = i; ; j++) {
                if (j == partyCategoryList.size()) {
                    i = j;
                    break;
                }
                SubCategoryPartyData element1 = partyCategoryList.get(j);
                if (element1.getCategory_id() == element.getCategory_id()) {

                    PartySubCategoryDto subCategoryDto = new PartySubCategoryDto();
                    subCategoryDto.setPartyId(element1.getParty_id());
                    subCategoryDto.setId(element1.getSub_category_id());
                    subCategoryDto.setName(element1.getSub_category_desc());
                    if (element1.getParty_id() == null) {
                        subCategoryDto.setFlag(false);
                        catFlag = catFlag && false;
                    } else {
                        subCategoryDto.setFlag(true);
                        catFlag = catFlag && true;
                    }
                    subCategoryDtoList.add(subCategoryDto);
                } else {
                    i = j;
                    break;
                }
            }

            categoryDto.setFlag(catFlag);
            PartySubCategoryDto[] arrSubCategoryDto = subCategoryDtoList.toArray(new PartySubCategoryDto[subCategoryDtoList.size()]);
            categoryDto.setSubCategories(arrSubCategoryDto);
            partySubCategoryDtoList.add(categoryDto);
        }
        return partySubCategoryDtoList;
    }

    @Transactional
    public void updateCategories(Long party_id, List<PartySubCategoryDto> partySubCategoryDtoList) throws RuntimeException {
        Party party = partyRepository.findById(party_id).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found"));

        for (PartySubCategoryDto category :
                partySubCategoryDtoList) {
            Category cat = categoryRepository.findById(category.getId()).orElseThrow(() -> new DataNotFoundException("Category Not Found"));
            PartySubCategoryDto[] subCategoryDtoList = category.getSubCategories();

            for (PartySubCategoryDto subCategory :
                    subCategoryDtoList) {

                if (subCategory.isFlag() || category.isFlag()) {
                    if (subCategory.getPartyId() == null) {
                        SubCategory subCat = subCategoryRepository.findById(subCategory.getId()).orElseThrow(() -> new DataNotFoundException("SubCategory Not Found"));

                        SubCategoryParty pc = new SubCategoryParty(party, cat, subCat);

                        subCategoryPartyRepository.save(pc);
                    }
                } else if (subCategory.getPartyId() != null) {
                    SubCategoryPartyId id = new SubCategoryPartyId(party.getId(), cat.getId(), subCategory.getId());
                    SubCategoryParty pc = subCategoryPartyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Data Not Found"));
                    subCategoryPartyRepository.delete(pc);
                }
            }
        }
    }

    public List<CategoryDto> getCategories() {
        List<CategoryDto> listCategoriesDto = new ArrayList<CategoryDto>();

        List<Category> categories = categoryRepository.findAll();

        for (Category element : categories) {
            List<SubCategory> subCategories = subCategoryRepository.findByCategory(element, Sort.by(Sort.Direction.ASC, "id"));

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setDescription(element.getDescription());
            categoryDto.setFlag(false);
            categoryDto.setId(element.getId());

            List<CategoryDto> listSubCategoriesDto = new ArrayList<CategoryDto>();
            for (SubCategory element1 : subCategories) {
                CategoryDto subCategory = new CategoryDto();
                subCategory.setDescription(element1.getDescription());
                subCategory.setId(element1.getId());
                subCategory.setFlag(false);
                listSubCategoriesDto.add(subCategory);
            }
            CategoryDto arrSubCategoriesDto[] = listSubCategoriesDto.toArray(new CategoryDto[listSubCategoriesDto.size()]);
            categoryDto.setSubCategories(arrSubCategoriesDto);

            listCategoriesDto.add(categoryDto);
        }
        return listCategoriesDto;
    }

    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setFlag(false);
        categoryDto.setDescription(category.getDescription());

        List<SubCategory> subCategories = subCategoryRepository.findByCategory(category, Sort.by(Sort.Direction.ASC, "id"));

        List<CategoryDto> listSubCategoriesDto = new ArrayList<CategoryDto>();
        for (SubCategory element1 : subCategories) {
            CategoryDto subCategory = new CategoryDto();
            subCategory.setId(element1.getId());
            subCategory.setDescription(element1.getDescription());
            subCategory.setFlag(false);
            listSubCategoriesDto.add(subCategory);
        }
        CategoryDto arrSubCategoriesDto[] = listSubCategoriesDto.toArray(new CategoryDto[listSubCategoriesDto.size()]);
        categoryDto.setSubCategories(arrSubCategoriesDto);

        return categoryDto;
    }
}
