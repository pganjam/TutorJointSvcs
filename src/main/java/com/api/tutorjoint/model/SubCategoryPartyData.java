package com.api.tutorjoint.model;

public class SubCategoryPartyData {

    private Long category_id;

    private Long sub_category_id;

    private Long party_id;

    private String category_desc;

    private String sub_category_desc;

    public SubCategoryPartyData(Long category_id, Long sub_category_id, Long party_id, String category_desc, String sub_category_desc) {
        this.category_id = category_id;
        this.sub_category_id = sub_category_id;
        this.party_id = party_id;
        this.category_desc = category_desc;
        this.sub_category_desc = sub_category_desc;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public Long getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(Long sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public Long getParty_id() {
        return party_id;
    }

    public void setParty_id(Long party_id) {
        this.party_id = party_id;
    }

    public String getCategory_desc() {
        return category_desc;
    }

    public void setCategory_desc(String category_desc) {
        this.category_desc = category_desc;
    }

    public String getSub_category_desc() {
        return sub_category_desc;
    }

    public void setSub_category_desc(String sub_category_desc) {
        this.sub_category_desc = sub_category_desc;
    }
}
