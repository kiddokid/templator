package com.example.templator;

import com.example.templator.modificator.model.Block;
import com.example.templator.modificator.model.TextPosition;
import com.example.templator.modificator.model.Tag;
import com.example.templator.model.cv.Certification;
import com.example.templator.model.cv.Education;
import com.example.templator.modificator.SoftServeTemplateModificator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CVSubstituter {

    private SoftServeTemplateModificator softServeTemplateModificator;

    private static String pathToSave3 = "D:\\Java\\templator\\templator\\src\\main\\resources\\docs\\ss\\3.docx";
    private static String pathToSave = "D:\\Java\\templator\\templator\\src\\main\\resources\\docs\\ss\\2.docx";
    private static String pathDocx = "D:\\Java\\templator\\templator\\src\\main\\resources\\docs\\ss\\1.docx";

    // @PostConstruct
    public void process() throws IOException, InvalidFormatException {

        String description = "Certified professional with over 6 years of diverse IT experience in healthcare domain," +
                " which includes running business analysis processes and activities, creating enterprise systems for" +
                " pharmaceuticals, creating mobile applications for internet banking and insurance companies," +
                " improving electronic health record systems to pass government certification, managing distributed" +
                " teams, conducting lead generation, business development and consulting.\n" +
                "Experienced in all product & software development phases: from ideation, elicitation and analysis," +
                " through architecture design and construction, to solution deployment.";

        XWPFDocument doc = new XWPFDocument(OPCPackage.open(pathDocx));
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{{first_name}}", "Sarah");
        replaceMap.put("{{last_name}}", "Connor");
        replaceMap.put("var_email", "emelrouse@gmail.com");
        replaceMap.put("{{current_position}}", "Senior Software Engineer");
        replaceMap.put("{{short_description}}", description);

        XWPFDocument changedDoc = null;

        Block block = Block.builder()
                .headerTag(new Tag("Experience", null))
                .footerTag(new Tag("{{end_of_project_exp}}", null))
                .countOfCopies(4)
                .countOfRows(9)
                .build();
        try {
            doc = extendTablesByBlock(doc, block);
        } catch (XmlException e) {
            changedDoc = new XWPFDocument();
            e.printStackTrace();
        }


        Block block2 = Block.builder()
                .headerTag(new Tag("{{certificate_name_0}}", null))
                .countOfCopies(2)
                .countOfRows(2)
                .build();

        Block block3 = Block.builder()
                .headerTag(new Tag("{{school_degree_name_0}}", null))
                .countOfCopies(1)
                .countOfRows(4)
                .build();

        Block block4 = Block.builder()
                .headerTag(new Tag("{{skill_header_0}}", null))
                .countOfCopies(3)
                .countOfRows(2)
                .build();

        try {
            doc = extendParagraphsByBlock(doc, block2);
            doc = extendParagraphsByBlock(doc, block3);
        } catch (XmlException e) {
            e.printStackTrace();
        }

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

//        Map<String, String> replaceMap2 = softServeTemplateModificator.buildReplaceMapForCert(certifications);
//        Map<String, String> replaceMap3 = softServeTemplateModificator.buildReplaceMapForEdu(educationList);
//
//        log.info("MAP {}", replaceMap3);
//
//        doc = replace(doc, replaceMap);
//        doc = replace(doc, replaceMap2);
//        doc = replace(doc, replaceMap3);

        FileOutputStream fileOutputStream = new FileOutputStream(pathToSave);
        if (doc != null) {
            doc.write(fileOutputStream);
        }

        fileOutputStream.close();
    }

    public XWPFDocument extendTablesByBlock(XWPFDocument inputDoc, Block block) throws IOException, XmlException {

        // Is a register for tag objects to differentiate between newly found and existing
        Map<String, Tag> tagRegister = new HashMap<>();

        for (int tablesIndex = 0; tablesIndex < inputDoc.getTables().size(); tablesIndex++) { // tables

            XWPFTable tbl = inputDoc.getTables().get(tablesIndex);

            for (int currentRowToCheckIndex = 0; currentRowToCheckIndex < tbl.getRows().size(); currentRowToCheckIndex++) { // rows

                XWPFTableRow row = tbl.getRows().get(currentRowToCheckIndex);

                for (int tableCellIndex = 0; tableCellIndex < row.getTableCells().size(); tableCellIndex++) { // table cells
                    XWPFTableCell cell = row.getTableCells().get(tableCellIndex);

                    for (int paragIndex = 0; paragIndex < cell.getParagraphs().size(); paragIndex++) { // parags

                        XWPFParagraph paragraph = cell.getParagraphs().get(paragIndex);

                        for (XWPFRun r : paragraph.getRuns()) { // runs

                            String text = r.getText(0);
                            int numberOfNewRows = 1;

                            // if header tag of block found
                            if (text != null && text.equals(block.getHeaderTag().getTagName())) {

                                // Number of blocks we need to have as the result
                                for (int blockCount = 0; blockCount < block.getCountOfCopies(); blockCount++) {

                                    // Position of footer tag
                                    TextPosition textPosition = getFirstFoundRowIndexByTag(inputDoc, block.getFooterTag().getTagName());

                                    // Number of rows block have to have
                                    for (int rowsToCopyIndex = 1; rowsToCopyIndex < block.getCountOfRows(); rowsToCopyIndex++) {

                                        // Copying to new row
                                        XWPFTable tableToAddNewBlock = inputDoc.getTables().get(textPosition.getTableIndex());
                                        XWPFTableRow lastRow = tbl.getRow(currentRowToCheckIndex + rowsToCopyIndex);
                                        CTRow ctrow = CTRow.Factory.parse(lastRow.getCtRow().newInputStream());
                                        XWPFTableRow newRow = new XWPFTableRow(ctrow, tableToAddNewBlock);

                                        // Checking all text to rename all tags
                                        for (XWPFTableCell c : newRow.getTableCells()) {

                                            ListIterator<XWPFParagraph> listIterator = c.getParagraphs().listIterator();

                                            if (listIterator.hasNext()) {

                                                XWPFParagraph p = listIterator.next();

                                                for (XWPFRun run : p.getRuns()) {
                                                    String t = run.getText(0);
                                                    if (t != null && containsTag(t)) {
                                                        String tagString = getTagFromText(t);
                                                        log.info("Found tag {}", tagString);
                                                        Tag foundTag;
                                                        foundTag = tagRegister.get(tagString);
                                                        if (foundTag != null) {
                                                            foundTag.increaseIndex();
                                                        } else {
                                                            foundTag = new Tag(tagString, null);
                                                            foundTag.increaseIndex();
                                                            tagRegister.put(tagString, foundTag);
                                                        }

                                                        log.info("Replacing {} to {}", t, foundTag.getTagName());
                                                        t = t.replace(tagString, foundTag.getTagName());
                                                        run.setText(t, 0);
                                                    }
                                                }
                                            }
                                        }
                                        tableToAddNewBlock.addRow(newRow, textPosition.getRowIndex() + numberOfNewRows); // After
                                        numberOfNewRows++;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return inputDoc;
    }

    public XWPFDocument extendParagraphsByBlock(XWPFDocument inputDoc, Block block) throws IOException, XmlException {
        Map<String, Tag> tagRegister = new HashMap<>();

        for (int tablesIndex = 0; tablesIndex < inputDoc.getTables().size(); tablesIndex++) { // tables

            XWPFTable tbl = inputDoc.getTables().get(tablesIndex);

            for (int currentRowToCheckIndex = 0; currentRowToCheckIndex < tbl.getRows().size(); currentRowToCheckIndex++) { // rows

                XWPFTableRow row = tbl.getRows().get(currentRowToCheckIndex);

                for (int tableCellIndex = 0; tableCellIndex < row.getTableCells().size(); tableCellIndex++) { // table cells
                    XWPFTableCell cell = row.getTableCells().get(tableCellIndex);

                    for (int paragIndex = 0; paragIndex < cell.getParagraphs().size(); paragIndex++) { // parags

                        XWPFParagraph paragraph = cell.getParagraphs().get(paragIndex);

                        for (XWPFRun r : paragraph.getRuns()) { // runs
                            String text = r.getText(0);

                            //log.info("Working with text {}", text);

                            if (text != null && text.contains(block.getHeaderTag().getTagName())) {

                                for (int blockCount = 0; blockCount < block.getCountOfCopies(); blockCount++) {

                                    for (int paragraphsToCopyIndex = 0; paragraphsToCopyIndex < block.getCountOfRows(); paragraphsToCopyIndex++) {

                                        XWPFParagraph paragraphToCopy = cell.getParagraphs().get(paragIndex + paragraphsToCopyIndex);
                                        XWPFParagraph addedPar = cell.addParagraph();
                                        cloneParagraph(addedPar, paragraphToCopy);
//                                        XWPFParagraph addedPar = new XWPFParagraph((CTP) paragraphToCopy.getCTP().copy(), cell);
//                                        //cell.addParagraph(addedPar);

                                        for (XWPFRun run : addedPar.getRuns()) {
                                            String t = run.getText(0);
                                            log.info("text {}", t);
                                            if (t != null && containsTag(t)) {
                                                String tagString = getTagFromText(t);
                                                log.info("Found tag {}", tagString);
                                                Tag foundTag;
                                                foundTag = tagRegister.get(tagString);
                                                if (foundTag != null) {
                                                    foundTag.increaseIndex();
                                                } else {
                                                    foundTag = new Tag(tagString, null);
                                                    foundTag.increaseIndex();
                                                    tagRegister.put(tagString, foundTag);
                                                }

                                                log.info("Replacing {} to {}", t, foundTag.getTagName());
                                                t = t.replace(tagString, foundTag.getTagName());
                                                run.setText(t, 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return inputDoc;
    }

    public TextPosition getFirstFoundRowIndexByTag(XWPFDocument inputDoc, String tag) {

        for (int tablesIndex = 0; tablesIndex < inputDoc.getTables().size(); tablesIndex++) {

            XWPFTable tbl = inputDoc.getTables().get(tablesIndex);

            for (int rowCountIndex = 0; rowCountIndex < tbl.getRows().size(); rowCountIndex++) {

                XWPFTableRow row = tbl.getRows().get(rowCountIndex);

                for (XWPFTableCell cell : row.getTableCells()) {

                    for (int paragIndex = 0; paragIndex < cell.getParagraphs().size(); paragIndex++) {

                        XWPFParagraph paragraph = cell.getParagraphs().get(paragIndex);

                        for (XWPFRun r : paragraph.getRuns()) {

                            String text = r.getText(0);

                            if (text != null && text.equals(tag)) {

                                return new TextPosition(tablesIndex, rowCountIndex, paragIndex);
                            }
                        }
                    }
                }
            }
        }
        return new TextPosition(0, 0, 0);
    }


    public XWPFDocument replace(XWPFDocument inputDoc, Map<String, String> replaceMap) {

        Boolean added = false;

        // Regular text
        for (XWPFParagraph p : inputDoc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    replace(replaceMap, r);
                }
            }
        }

        for (int tablesIndex = 0; tablesIndex < inputDoc.getTables().size(); tablesIndex++) {

            XWPFTable tbl = inputDoc.getTables().get(tablesIndex);

            for (int rowCountIndex = 0; rowCountIndex < tbl.getRows().size(); rowCountIndex++) {

                XWPFTableRow row = tbl.getRows().get(rowCountIndex);

                for (XWPFTableCell cell : row.getTableCells()) {

                    for (int paragIndex = 0; paragIndex < cell.getParagraphs().size(); paragIndex++) {

                        XWPFParagraph paragraph = cell.getParagraphs().get(paragIndex);

                        for (XWPFRun r : paragraph.getRuns()) {

                            replace(replaceMap, r);
                        }
                    }
                }
            }
        }


        // Headers text
        for (XWPFHeader header : inputDoc.getHeaderList()) {
            for (XWPFParagraph p : header.getParagraphs()) {
                for (XWPFRun r : p.getRuns()) {
                    replace(replaceMap, r);
                }
            }
        }


        return inputDoc;
    }

    private void replace(Map<String, String> replaceMap, XWPFRun r) {
        //log.info("Text position {}", r.getTextPosition());
        String text = r.getText(0);
        //log.info("Text {}", r.text());
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            if (entry.getValue() == null) {
                entry.setValue("");
            }
            if (text != null && text.contains(entry.getKey())) {

                log.info("Found match for key {}, changing value to {}...",
                        entry.getKey(), StringUtils.left(entry.getValue(), 10));

                text = text.replace(entry.getKey(), entry.getValue());
                r.setText(text, 0);
                log.info("New text for {} is {}", entry.getKey(), r.getText(0));
            }
        }
    }

    private void cloneParagraph(XWPFParagraph clone, XWPFParagraph source) {
        CTPPr pPr = clone.getCTP().isSetPPr() ? clone.getCTP().getPPr() : clone.getCTP().addNewPPr();
        pPr.set(source.getCTP().getPPr());
        for (XWPFRun r : source.getRuns()) {
            XWPFRun nr = clone.createRun();
            cloneRun(nr, r);
        }
    }

    private void cloneRun(XWPFRun clone, XWPFRun source) {
        CTRPr rPr = clone.getCTR().isSetRPr() ? clone.getCTR().getRPr() : clone.getCTR().addNewRPr();
        rPr.set(source.getCTR().getRPr());
        clone.setText(source.getText(0));
    }

    private boolean containsTag(String string) {
        return string.contains("{{") && string.contains("}}");
    }

    private String getTagFromText(String text) {
        return text.substring(text.indexOf("{{"), text.lastIndexOf("}}") + 2);
    }
}
