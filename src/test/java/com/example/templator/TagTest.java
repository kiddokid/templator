package com.example.templator;

import com.example.templator.modificator.model.Tag;
import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void increaseIndexTest() {
        Tag tag = getBaseTag();
        tag.increaseIndex();

        assert tag.getTagName().equals("{{project_name_1}}");

        Tag tag1 = new Tag("{{end_of_project_exp_}}", null);
        tag.increaseIndex();

        assert tag1.getTagName().equals("{{end_of_project_exp_}}");
    }

    @Test
    public void pureNameWorks() {
        Tag tag = getBaseTag();

        //System.out.println(tag.getPureName());
        assert tag.getPureName().equals("project_name");

        for (int i = 0; i < 15; i++) {
            tag.increaseIndex();
        }

        assert tag.getIndex() == 15;

        assert tag.getPureName().equals("project_name");
    }

    @Test
    public void moveIndexRightWorks() {
        Tag tag = getBaseTag();
        tag.increaseIndex();

        assert tag.getTagName().equals("{{project_name_1}}");

        tag.moveIndexRight();

        assert tag.getTagName().equals("{{project_name_1_0}}");

        tag.increaseIndex();

        assert tag.getTagName().equals("{{project_name_1_1}}");

        tag.moveIndexRight();

        assert tag.getTagName().equals("{{project_name_1_1_0}}");
    }

    private Tag getBaseTag() {
        return new Tag("{{project_name_0}}",  null);
    }
}
