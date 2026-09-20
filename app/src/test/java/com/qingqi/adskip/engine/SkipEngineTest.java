package com.qingqi.adskip.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SkipEngineTest {

    private UiNode node(String text, boolean clickable) {
        UiNode n = new UiNode();
        n.text = text;
        n.clickable = clickable;
        return n;
    }

    @Test
    public void findsClickableAncestorOfSkipText() {
        UiNode root = node("FrameLayout", false);
        UiNode button = node("Button", true);
        UiNode label = node("跳过", false);
        root.children.add(button);
        button.children.add(label);
        label.parent = button;
        button.parent = root;

        List<String> keywords = Arrays.asList("跳过", "skip");
        UiNode target = SkipEngine.findSkipTarget(root, keywords);
        assertNotNull(target);
        assertEquals("Button", target.text);
    }

    @Test
    public void returnsNullWhenNoKeyword() {
        UiNode root = node("Hello", false);
        List<String> keywords = Arrays.asList("跳过");
        assertNull(SkipEngine.findSkipTarget(root, keywords));
    }

    @Test
    public void matchesCustomKeywordCaseInsensitive() {
        UiNode root = node("SKIP AD", false);
        root.clickable = true;
        List<String> keywords = Arrays.asList("skip");
        UiNode target = SkipEngine.findSkipTarget(root, keywords);
        assertNotNull(target);
        assertEquals("SKIP AD", target.text);
    }

    @Test
    public void clicksSelfWhenNodeAlreadyClickable() {
        UiNode root = node("关闭", true);
        List<String> keywords = Arrays.asList("关闭");
        UiNode target = SkipEngine.findSkipTarget(root, keywords);
        assertNotNull(target);
        assertEquals("关闭", target.text);
    }
}
