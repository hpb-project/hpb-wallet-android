#---------------------------------基本指令区----------------------------------
-target 1.6
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontwarn
-ignorewarning
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes *JavascriptInterface*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

#---------------------------------1.实体类---------------------------------
-keep class Open_source_Android.dapp.common.bean.**{*;}
-keep class Open_source_Android.dapp.web3.bean.**{*;}
-keep class Open_source_Android.dapp.net.bean.**{*;}

#---------------------------------2.第三方包-------------------------------
#umeng 分享
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep class com.umeng.** {*;}

-keep class Open_source_Android.dapp.web3.**{*;}

#使用Glide的混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# OKHttp混淆
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

#fastjson
-libraryjars libs/fastjson.jar
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}

#nineoldandroids
#-dontwarn com.nineoldandroids.**
#-keep class com.nineoldandroids.** { *;}

-keep class org.** {*;}
#com.fasterxml.jackson.core:jackson-databind
-keep class com.com.fasterxml.jackson.annotation.**{*;}
-keep interface com.fasterxml.jackson.annotation { *; }
-keep public class * extends com.fasterxml.jackson.annotation.**
-keep enum com.fasterxml.jackson.annotation.** { *; }

-keep class com.fasterxml.jackson.core.** {*;}
-keep interface com.fasterxml.jackson.core { *; }
-keep public class * extends com.fasterxml.jackson.core.**
-keep class com.fasterxml.jackson.databind.introspect.VisibilityChecker$Std.<clinit>
-keep class com.fasterxml.jackson.databind.ObjectMapper.<clinit>
-keep class com.fasterxml.jackson.databind.** {*;}
-keep class com.fasterxml.jackson.databind.introspect.VisibilityChecker$*{*;}
-keep interface com.fasterxml.jackson.databind { *; }
-keep public class * extends com.fasterxml.jackson.databind.**
-keep class com.madgag.spongycastle.**{*;}

# for butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}


#---------------------------------3.与js互相调用的类------------------------
-keep class android.webkit.JavascriptInterface {*;}
-keepclassmembers class Open_source_Android.dapp.ui.activity.CommonWebActivity*{
 *;
}
-keepclassmembers class Open_source_Android.dapp.ui.activity.NewsWebActivity*{
 *;
}
-keepclassmembers class Open_source_Android.dapp.ui.activity.DappsWebActivity*{
 *;
}

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace(); java.lang.Object readResolve();
}
-keep class **.R$* { *; }
-keepclassmembers class * {
    void *(**On*Event);
}

#---------------------greenDao 数据库 START------------------------
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
    public static void dropTable(org.greenrobot.greendao.database.Database, boolean);
    public static void createTable(org.greenrobot.greendao.database.Database, boolean);
}
-keep class **$Properties {*;}

# If you do not use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do not use RxJava:
-dontwarn rx.**

#sqlcipher数据库加密开始
-keep  class net.sqlcipher.** {*;}
-keep  class net.sqlcipher.database.** {*;}
#sqlcipher数据库加密结束
#---------------------greenDao 数据库 END------------------------
