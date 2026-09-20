package com.qingqi.adskip;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.qingqi.adskip.engine.SkipEngine;
import com.qingqi.adskip.engine.UiNode;

import java.util.List;

/**
 * 清启 核心无障碍服务：监听窗口切换 / 内容变化，扫描“跳过/Skip/关闭”等按钮后自动点击。
 */
public class SplashSkipService extends AccessibilityService {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private long lastClickTime = 0;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.DEFAULT
                | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
                | AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null) return;
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;

        List<String> keywords = Prefs.getKeywords(this);
        UiNode tree = buildTree(root, null);
        UiNode target = SkipEngine.findSkipTarget(tree, keywords);

        if (target != null && target.payload instanceof AccessibilityNodeInfo) {
            AccessibilityNodeInfo node = (AccessibilityNodeInfo) target.payload;
            long now = System.currentTimeMillis();
            if (now - lastClickTime > 800) {
                lastClickTime = now;
                final AccessibilityNodeInfo clickNode = node;
                handler.postDelayed(() -> {
                    if (clickNode != null && clickNode.isClickable()) {
                        clickNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }, Prefs.getDelay(this));
            }
        }
    }

    private UiNode buildTree(AccessibilityNodeInfo node, UiNode parent) {
        if (node == null) return null;
        UiNode ui = new UiNode();
        ui.parent = parent;
        ui.clickable = node.isClickable();
        ui.text = safeChar(node.getText());
        ui.contentDescription = safeChar(node.getContentDescription());
        ui.payload = node;
        for (int i = 0; i < node.getChildCount(); i++) {
            UiNode child = buildTree(node.getChild(i), ui);
            if (child != null) ui.children.add(child);
        }
        return ui;
    }

    private static String safeChar(CharSequence cs) {
        return cs == null ? null : cs.toString();
    }

    @Override
    public void onInterrupt() {
    }
}
