package com.example.templator.model.base;

import com.example.templator.model.cv.Education;
import com.example.templator.modificator.model.Block;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class BaseCVDetails implements ReplacableElement {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String currentPosition;
    private String qualificationDescription;
    private List<Education> educationList;

    @Override
    public Map<String, String> getAttributeTagMap() {
        Map<String, String> attributeTagMap = new HashMap<>();
        attributeTagMap.put(firstName, "{{first_name}}");
        attributeTagMap.put(lastName, "{{last_name}}");
        attributeTagMap.put(email, "{{personal_email}}");
        attributeTagMap.put(phone, "{{personal_phone}}");
        attributeTagMap.put(currentPosition, "{{current_position}}");
        attributeTagMap.put(qualificationDescription, "{{short_description}}");
        return attributeTagMap;
    }

    @Override
    public Block getTemplateBlock() {
        return null;
    }

    public abstract List<List<ReplacableElement>> getExtandabeParagraphList();

    public abstract List<List<ReplacableElement>> getExtandableTableList();
}
