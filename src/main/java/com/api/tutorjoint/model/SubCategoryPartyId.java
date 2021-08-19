package com.api.tutorjoint.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubCategoryPartyId implements Serializable {

    @Column(name = "party_id")
    Long partyId;

    @Column(name = "category_id")
    Long categoryId;

    @Column(name = "sub_category_id")
    Long subCategoryId;

    public SubCategoryPartyId() {
    }

    public SubCategoryPartyId(Long partyId, Long categoryId, Long subCategoryId) {
        this.partyId = partyId;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCategoryPartyId that = (SubCategoryPartyId) o;
        return partyId.equals(that.partyId) &&
                categoryId.equals(that.categoryId) &&
                subCategoryId.equals(that.subCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, categoryId, subCategoryId);
    }
}
