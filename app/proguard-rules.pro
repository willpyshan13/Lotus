# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontskipnonpubliclibraryclassmembers
-printconfiguration
-keep,allowobfuscation @interface android.support.annotation.Keep

-keep @android.support.annotation.Keep class *
-keepclassmembers class * {
    @android.support.annotation.Keep *;
}

-keep public class name.huihui.example.Test { *; }
-keep public class * extends name.huihui.example.Test { *; }
-keep public class **.*model*.** {*;}
-keep class * implements name.huihui.example.TestInterface { *; }
-keepclassmembers class name.huihui.example.Test {
    public <init>();
}
-keepclassmembers class name.huihui.example.Test {
    public void test(java.lang.String);
}
-keep class name.huihui.example.Test$* {
        *;
 }
-keep class com.suchengkeji.android.ui.**
-keep class com.suchengkeji.android.ui.*
-keep class com.suchengkeji.android.bean.** { *; }
# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# lotus生成中间代码
-keep class com.lotus.** { *; }