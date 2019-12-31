package com.example.templator.model.cv.impl;

import com.example.templator.model.base.BaseCVDetails;
import com.example.templator.model.base.ReplacableElement;
import com.example.templator.model.cv.Certification;
import com.example.templator.model.cv.WorkExperience;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SoftServeCVDetails extends BaseCVDetails {


    private List<WorkExperience> workExperienceList;
    private List<Certification> certificationList;
    private String freeFormDescription;
    private List<String> references;

    @Override
    public List<List<ReplacableElement>> getExtandabeParagraphList() {
        List<List<ReplacableElement>> listOfLists = new ArrayList<>();

        List<ReplacableElement> replacableElementList = new ArrayList<>(super.getEducationList());
        listOfLists.add(replacableElementList);

        replacableElementList = new ArrayList<>(certificationList);
        listOfLists.add(replacableElementList);

        return listOfLists;
    }

    @Override
    public List<List<ReplacableElement>> getExtandableTableList() {
        List<List<ReplacableElement>> listOfLists = new ArrayList<>();
        List<ReplacableElement> replacableElements = new ArrayList<>(workExperienceList);
        listOfLists.add(replacableElements);
        return listOfLists;
    }
}
