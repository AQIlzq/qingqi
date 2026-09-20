package com.qingqi.adskip.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * 开屏广告“跳过”判定引擎（与 Android 无关，可单测）。
 * 规则：遍历整棵 UI 树，找到文字（text 或 contentDescription）包含任一关键词的节点，
 * 再向上回溯其“可点击祖先”作为最终点击目标；若自身已可点击则直接返回自身。
 */
public final class SkipEngine {
    private SkipEngine() {
    }

    public static UiNode findSkipTarget(UiNode root, List<String> keywords) {
        if (root == null || keywords == null) return null;
        List<UiNode> all = new ArrayList<>();
        collect(root, all);
        for (UiNode node : all) {
            String t = textOf(node);
            if (t == null) continue;
            String low = t.toLowerCase();
            for (String kw : keywords) {
                if (kw == null || kw.isEmpty()) continue;
                if (low.contains(kw.toLowerCase())) {
                    UiNode target = clickableAncestor(node);
                    return target != null ? target : node;
                }
            }
        }
        return null;
    }

    private static String textOf(UiNode n) {
        if (n.text != null && !n.text.isEmpty()) return n.text;
        if (n.contentDescription != null && !n.contentDescription.isEmpty()) return n.contentDescription;
        return null;
    }

    private static UiNode clickableAncestor(UiNode n) {
        UiNode cur = n;
        int depth = 0;
        while (cur != null && depth < 6) {
            if (cur.clickable) return cur;
            cur = cur.parent;
            depth++;
        }
        return null;
    }

    private static void collect(UiNode node, List<UiNode> out) {
        if (node == null) return;
        out.add(node);
        for (UiNode c : node.children) collect(c, out);
    }
}
