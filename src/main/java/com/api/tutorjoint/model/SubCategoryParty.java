package com.api.tutorjoint.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "sub_category_party")
public class SubCategoryParty {
    @EmbeddedId
    private SubCategoryPartyId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @Column
    private Instant createdOn;

    @Column
    private Instant updatedOn;

    public SubCategoryParty() {
    }

    public SubCategoryParty(Party party, Category category, SubCategory subCategory) {
        this.party = party;
        this.category = category;
        this.subCategory = subCategory;
        this.createdOn = Instant.now();
        this.updatedOn = Instant.now();
        this.id = new SubCategoryPartyId(party.getId(), category.getId(), subCategory.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        SubCategoryParty that = (SubCategoryParty) o;
        return Objects.equals(party, that.party) &&
                Objects.equals(category, that.category) &&
                Objects.equals(subCategory, that.subCategory);

    }

    @Override
    public int hashCode() {
        return Objects.hash(party, category, subCategory);
    }

}
