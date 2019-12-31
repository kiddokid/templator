package com.example.templator;

import com.example.templator.model.cv.impl.SoftServeCVDetails;
import com.example.templator.model.cv.Certification;
import com.example.templator.modificator.SoftServeTemplateModificator;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SoftServeTemplateModificatorTest {

    private CVSubstituter cvSubstituter = new CVSubstituter();


    @Test
    public void test() {
        Certification certification1 = new Certification("Oracle", "1990");
        Certification certification2 = new Certification("Java 11", "2020");
        Certification certification3 = new Certification("Python", "2017");

        List<Certification> certifications = List.of(certification1, certification2, certification3);

        SoftServeCVDetails softServeCvDetails = new SoftServeCVDetails();
        softServeCvDetails.setCertificationList(certifications);

        //templateModificator.process(softServeCvDetails);

        assert true;
    }
}
