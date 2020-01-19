package com.example.templator.modificator;

import com.example.templator.CVSubstituter;
import com.example.templator.model.base.BaseCVDetails;
import com.example.templator.model.base.ReplacableElement;
import com.example.templator.model.cv.Certification;
import com.example.templator.model.cv.Education;
import com.example.templator.model.cv.WorkExperience;
import com.example.templator.model.cv.impl.SoftServeCVDetails;
import com.example.templator.modificator.model.Block;
import com.example.templator.modificator.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.example.templator.modificator.ReplacableMapBuilder.buildReplaceMapFromList;

@Slf4j
@Component
public class SoftServeTemplateModificator implements TemplateModificator<SoftServeCVDetails> {

    private CVSubstituter cvSubstituter;

    public SoftServeTemplateModificator(CVSubstituter cvSubstituter) {
        this.cvSubstituter = cvSubstituter;
    }

    @PostConstruct
    public void init() throws InvalidFormatException, IOException {
        String description = "Certified professional with over 6 years of diverse IT experience in healthcare domain," +
                " which includes running business analysis processes and activities, creating enterprise systems for" +
                " pharmaceuticals, creating mobile applications for internet banking and insurance companies," +
                " improving electronic health record systems to pass government certification, managing distributed" +
                " teams, conducting lead generation, business development and consulting.\n" +
                "Experienced in all product & software development phases: from ideation, elicitation and analysis," +
                " through architecture design and construction, to solution deployment.";

        String pathToSave3 = "D:\\Java\\templator\\templator\\src\\main\\resources\\docs\\ss\\output.docx";
        String pathToSave = "D:\\Java\\templator\\templator\\src\\main\\resources\\docs\\ss\\2.docx";
        String pathDocx = "D:\\Java\\templator\\templator\\src\\main\\resources\\docs\\ss\\1.docx";

        XWPFDocument doc = new XWPFDocument(OPCPackage.open(pathDocx));

        Certification certification1 = new Certification("Oracle", "1990");
        Certification certification2 = new Certification("Java 11", "2020");
        Certification certification3 = new Certification("Python", "2017");

        List<Certification> certifications = List.of(certification1, certification2, certification3);

        Education education1 = Education.builder()
                .degreeName("Master of Economics")
                .graduatedYear("2002")
                .schoolDepartment("Applied economics")
                .schoolName("KNU")
                .build();

        Education education2 = Education.builder()
                .degreeName("Bachelor Information Security")
                .graduatedYear("2001")
                .schoolDepartment("Applied Kung-fu")
                .schoolName("Aalto University")
                .build();

        List<Education> educationList = List.of(education1, education2);

        WorkExperience workExperience1 = WorkExperience.builder()
                .projectDescription("description")
                .customerName("Eset")
                .involvementDuration("6 months")
                .projectName("Kernel hook processor")
                .role("Senior Assambler dev")
                .teamSize("5")
                .toolsAndTech("Brand, assambler")
                .projectResponsibilities(List.of("resp"))
                .build();


        WorkExperience workExperience2 = WorkExperience.builder()
                .projectDescription("description")
                .customerName("Senata Sport+")
                .involvementDuration("12 months")
                .projectName("Game design")
                .role("Senior Cleaner")
                .teamSize("210")
                .toolsAndTech("Java, .Net, Web Services, SQL Server/OLAP/DTS/RS")
                .projectResponsibilities(List.of("resp", "pesp"))
                .build();

        WorkExperience workExperience3 = WorkExperience.builder()
                .projectDescription("description")
                .customerName("Eset")
                .involvementDuration("6 months")
                .projectName("Kernel hook processor")
                .role("Senior Assambler dev")
                .teamSize("5")
                .toolsAndTech("Brand, assambler")
                .projectResponsibilities(List.of("resp"))
                .build();

        List<WorkExperience> workExperienceList = List.of(workExperience1, workExperience2, workExperience3);

        SoftServeCVDetails softServeCVDetails = new SoftServeCVDetails();
        softServeCVDetails.setFirstName("Sarah");
        softServeCVDetails.setLastName("Connor");
        softServeCVDetails.setQualificationDescription(description);
        softServeCVDetails.setCurrentPosition("Senior Software Engineer");
        softServeCVDetails.setCertificationList(certifications);
        softServeCVDetails.setEducationList(educationList);
        softServeCVDetails.setWorkExperienceList(workExperienceList);

        process(softServeCVDetails, doc);

        FileOutputStream fileOutputStream = new FileOutputStream(pathToSave);
        doc.write(fileOutputStream);

        fileOutputStream.close();
        XWPFDocument doc2 = new XWPFDocument(OPCPackage.open(pathToSave));

        replace(doc2, softServeCVDetails);
        FileOutputStream fileOutputStream2 = new FileOutputStream(pathToSave3);
        doc2.write(fileOutputStream2);
        fileOutputStream2.close();
    }

    @Override
    public void process(SoftServeCVDetails cv, XWPFDocument templateDoc) {
        extend(templateDoc, cv);
        replace(templateDoc, cv);
    }

    private void extend(XWPFDocument templateDoc, SoftServeCVDetails cv) {
        cv.getExtandabeParagraphList().stream().filter(Objects::nonNull).forEach(list -> {

            Block templateBlock = list.stream().filter(Objects::nonNull).findFirst().get().getTemplateBlock();
            if (templateBlock != null) {
                templateBlock.setCountOfCopies(list.size() - 1); // Cause we have default one as well
                log.info("Block to extend {}", templateBlock);
                try {
                    cvSubstituter.extendParagraphsByBlock(templateDoc, templateBlock);
                } catch (IOException | XmlException e) {
                    e.printStackTrace();
                }

            }

        });

        cv.getExtandableTableList().stream().filter(Objects::nonNull).forEach(list -> {
            Block templateBlock = list.stream().filter(Objects::nonNull).findFirst().get().getTemplateBlock();
            if (templateBlock != null) {
                templateBlock.setCountOfCopies(list.size() - 1); // Cause we have default one as well
                log.info("Block to extend {}", templateBlock);
                try {
                    cvSubstituter.extendTablesByBlock(templateDoc, templateBlock);
                } catch (IOException | XmlException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void replace(XWPFDocument templateDoc, SoftServeCVDetails cv) {
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.putAll(buildReplaceMapForWork(cv.getWorkExperienceList()));
        replaceMap.putAll(buildReplaceMapForCert(cv.getCertificationList()));
        replaceMap.putAll(buildReplaceMapForEdu(cv.getEducationList()));
        replaceMap.putAll(buildReplaceMapBasic(cv));

        log.info("MAP {}", replaceMap);

        cvSubstituter.replace(templateDoc, replaceMap);
    }

    private Map<String, String> buildReplaceMapForCert(List<Certification> certifications) {
        List<ReplacableElement> replacableElementList = new ArrayList<>(certifications);
        return buildReplaceMapFromList(replacableElementList);
    }

    private Map<String, String> buildReplaceMapForWork(List<WorkExperience> certifications) {
        List<ReplacableElement> replacableElementList = new ArrayList<>(certifications);
        return buildReplaceMapFromList(replacableElementList);
    }

    private Map<String, String> buildReplaceMapForEdu(List<Education> educationList) {
        List<ReplacableElement> replacableElementList = new ArrayList<>(educationList);
        return buildReplaceMapFromList(replacableElementList);
    }

    private Map<String, String> buildReplaceMapBasic(BaseCVDetails baseCVDetails) {
        Map<String, String> replaceMap = new HashMap<>();
        baseCVDetails.getAttributeTagMap().forEach((key, value) -> {
            replaceMap.put(value, key);
        });
        return replaceMap;
    }
}
