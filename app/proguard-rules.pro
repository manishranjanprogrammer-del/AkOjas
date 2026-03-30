# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/ojas/AndroidTools/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn com.google.firebase.messaging.**
-dontwarn com.ojassoft.panchang.**
-dontwarn  com.web_tomorrow.**
-dontwarn  org.junit.**
-dontwarn com.astrosagekundli.junit.**
-dontwarn module-info
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-keepattributes *Annotation*
-keepclassmembers class **.R$* {
    public static <fields>;
}
-dontwarn android.support.v4.**
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep public class com.google.ads.** {
   public *;
}
-keep public class com.ojassoft.astrosage.notification.** {
   public *;
}

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.ojassoft.astrosage.varta.model.** { <fields>; }
   -keep class com.ojassoft.astrosage.varta.model.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep public class com.ojassoft.astrosage.beans.BeanHoroPersonalInfo { *; }
-keep public class com.ojassoft.astrosage.beans.BeanPlace { *; }
-keep public class com.ojassoft.astrosage.beans.BeanPlace { *; }
-keep public class com.ojassoft.astrosage.beans.AajKaPanchangModel { *; }
-keep public class com.ojassoft.astrosage.beans.BeanLagnaTable { *; }
-keep public class com.ojassoft.astrosage.beans.Festivalapidatum { *; }
-keep public class com.ojassoft.astrosage.beans.Festivalapidata2 { *; }

-keep public class com.ojassoft.astrosage.beans.CBookMarkItemCollection { *; }
-keep public class com.ojassoft.astrosage.beans.CScreenHistoryItemCollection { *; }
-keep public class com.ojassoft.astrosage.beans.CBookMarkItem { *; }
-keep public class com.ojassoft.astrosage.beans.SerializeAndDeserializeBeans { *; }
#-keep public com.libojassoft.android.custom.services.** {
#   public *;
#}
#
#-keep class com.libojassoft.**
#-keep openFileChooser
-keepclassmembers class * extends android.webkit.WebChromeClient { public void openFileChooser(...); }


-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}

-optimizations !method/inlining
-dontwarn android.test.**

#Joda
# All the resources are retrieved via reflection, so we need to make sure we keep them
-keep class net.danlew.android.joda.R$raw { *; }

# These aren't necessary if including joda-convert, but
# most people aren't, so it's helpful to include it.
-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

# Joda classes use the writeObject special method for Serializable, so
# if it's stripped, we'll run into NotSerializableExceptions.
# https://www.guardsquare.com/en/products/proguard/manual/examples#serializable

-keep class io.agora.**{*;}

-dontwarn kotlinx.coroutines.**
-dontwarn android.media.**
-dontwarn io.agora.**

# This is also needed for R8 in compat mode since multiple
# optimizations will remove the generic signature such as class
# merging and argument removal. See:
# https://r8.googlesource.com/r8/+/refs/heads/main/compatibility-faq.md#troubleshooting-gson-gson
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
# Optional. For using GSON @Expose annotation
-keepattributes AnnotationDefault,RuntimeVisibleAnnotations
-keep class com.google.gson.reflect.TypeToken { <fields>; }
-keepclassmembers class **$TypeAdapterFactory { <fields>; }