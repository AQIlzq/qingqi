package com.qingqi.adskip.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * 纯 JVM 可测的 UI 节点模型。无障碍服务把 AccessibilityNodeInfo 转换为该模型后再交给
 * SkipEngine 判定，从而让核心“跳过”逻辑脱离 Android 运行环境、可被 JUnit 覆盖。
 */
public class UiNode {
    public String text;
    public String contentDescription;
    public boolean clickable;
    public UiNode parent;
    public Object payload;
    public final List<UiNode> children = new ArrayList<>();
}
