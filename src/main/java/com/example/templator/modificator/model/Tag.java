package com.example.templator.modificator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ToString(exclude = "index")
public class Tag {

    private static final String startTag = "{{";
    private static final String endTag = "}}";
    private static final String indexDelimeter = "_";

    @Getter
    @Setter
    private String tagName;

    @Getter
    @Setter
    private String value;

    @Getter
    private Integer index;

    public Tag(String tagName, String value) {
        this.value = value;
        this.index = 0;
        this.tagName = tagName;
    }

    public void increaseIndex() {
        this.index++;
        updateTagName();
    }

    public void moveIndexRight() {
        String tagFirstPart = this.tagName.substring(this.tagName.indexOf(startTag),
                this.tagName.lastIndexOf(endTag));
        this.index = 0;
        this.tagName = tagFirstPart + indexDelimeter + index + endTag;
        updateTagName();
    }

    public void moveIndexLeft() {
        String tagFirstPart = this.tagName.substring(this.tagName.indexOf(startTag),
                this.tagName.lastIndexOf(endTag));
        String withoutLastIndex = tagFirstPart.substring(0, tagFirstPart.lastIndexOf(indexDelimeter + index.toString()));
        this.index = Integer.valueOf(withoutLastIndex.substring(withoutLastIndex.lastIndexOf(indexDelimeter) + indexDelimeter.length()));
        this.tagName = withoutLastIndex + endTag;
        updateTagName();
    }

    public String getPureName() {
        String name = this.tagName.substring(this.tagName.indexOf(startTag) + startTag.length(), this.tagName.lastIndexOf(endTag));
        while (Character.isDigit(name.charAt(name.length()-1))) {
            int position = 0;
            Pattern p = Pattern.compile("[0-9]");  // insert your pattern here
            Matcher m = p.matcher(name);
            if (m.find()) {
                position = m.start();
            }
            name = name.substring(0, position-indexDelimeter.length());
        }
        return name;
    }

    private void updateTagName() {
        String tagFirstPart = this.tagName.substring(this.tagName.indexOf(startTag),
                this.tagName.lastIndexOf(indexDelimeter));

        if (tagFirstPart.isEmpty()) {
            log.warn("This tag does not have counter! {}", this.tagName);
            return;
        }
        this.tagName = tagFirstPart + indexDelimeter + this.index + endTag;
    }

}
