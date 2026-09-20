# 清启 (QingQi)

安卓端**开屏广告自动跳过**工具。基于系统「无障碍服务（AccessibilityService）」实现，无需 Root。

## 原理
监听应用窗口切换与内容变化，扫描界面中带有 `跳过 / Skip / 关闭` 等关键词的按钮，自动点击跳过开屏广告，帮助你快速进入应用。

> 说明：开屏广告属于应用内行为，清启仅做“自动点击跳过”，不篡改任何应用数据。
> 部分应用为全屏点击跳转广告（无“跳过”按钮），需其提供跳过入口方可生效。

## 使用步骤
1. 安装 APK（Release 中下载 `qingqi-debug.apk`）。
2. 打开「清启」App，点击「前往开启无障碍服务」。
3. 在系统设置里找到 **清启 · 开屏广告跳过**，开启开关。
4. 返回 App，状态变为「已开启」即生效。
5. 可在 App 内自定义跳过关键词与点击延迟。

> Android 12+ 对无障碍服务有限制：如后台失效，请将清启加入电池白名单 / 允许后台运行。

## 仓库结构
- `app/src/main/java/com/qingqi/adskip/` — 服务与界面
- `app/src/main/java/com/qingqi/adskip/engine/` — 可单测的跳过判定引擎
- `.github/workflows/build.yml` — GitHub Actions 自动出包

## 构建
CI 会在每次推送 `main` 时运行单元测试门禁并编译 Debug APK，产物挂在 Release。
本地如需构建：需 Android SDK 35 + JDK 17，执行 `./gradlew assembleDebug`。
