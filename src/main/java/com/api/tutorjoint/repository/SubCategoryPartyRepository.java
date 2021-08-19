package com.api.tutorjoint.repository;

import com.api.tutorjoint.model.SubCategoryPartyData;
import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.model.SubCategoryParty;
import com.api.tutorjoint.model.SubCategoryPartyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubCategoryPartyRepository extends JpaRepository<SubCategoryParty, SubCategoryPartyId> {

    List<SubCategoryParty> findByParty(Party party);

    @Query("SELECT new com.api.tutorjoint.model.SubCategoryPartyData(cat.id, sub.id, p.id, cat.description, sub.description) FROM Category cat JOIN cat.subCategories sub LEFT JOIN sub.partyCategories pc LEFT JOIN pc.party p ON p.id=:party_id")
    List<SubCategoryPartyData> fetchPartiesCategories(Long party_id);

}
