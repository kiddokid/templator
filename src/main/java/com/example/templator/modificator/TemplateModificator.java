package com.example.templator.modificator;

import com.example.templator.model.base.BaseCVDetails;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface TemplateModificator<T extends BaseCVDetails> {

    void  process(T cv, XWPFDocument templateDoc);
}
