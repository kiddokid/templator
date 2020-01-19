package com.example.templator.modificator;

import com.example.templator.model.base.ReplacableElement;
import com.example.templator.modificator.model.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplacableMapBuilder {

    public static Map<String, String> buildReplaceMapFromList(List<ReplacableElement> replacableElements) {
        Map<String, Tag> tagRegistry = new HashMap<>();
        Map<String, String> replaceMap = new HashMap<>();

        replacableElements.forEach(replacableElement -> {
            Map<String, String> attributeTagMap = replacableElement.getAttributeTagMap();
            attributeTagMap.forEach((attribute, tagName) -> {
                if (tagName != null && isTag(tagName)) {
                    Tag foundTag;
                    foundTag = tagRegistry.get(tagName);
                    if (foundTag != null) {
                        replaceMap.put(foundTag.getTagName(), attribute);
                        foundTag.increaseIndex();
                    } else {
                        foundTag = new Tag(tagName, null);
                        replaceMap.put(foundTag.getTagName(), attribute);
                        foundTag.increaseIndex();
                        tagRegistry.put(tagName, foundTag);
                    }
                }
            });
        });
        return replaceMap;
    }

    private static boolean isTag(String string) {
        return string.startsWith("{{") && string.endsWith("}}");
    }
}
