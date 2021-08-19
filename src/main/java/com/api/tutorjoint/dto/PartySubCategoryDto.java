package com.api.tutorjoint.dto;

import com.api.tutorjoint.model.SubCategoryPartyId;

public class PartySubCategoryDto {
    private Long partyId;
    private Long id;
    private String name;
    private boolean flag;
    private PartySubCategoryDto[] subCategories;

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public PartySubCategoryDto[] getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(PartySubCategoryDto[] subCategories) {
        this.subCategories = subCategories;
    }
}
