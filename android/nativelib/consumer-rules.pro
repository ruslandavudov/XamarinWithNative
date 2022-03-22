## Keep model classes
#-keep class com.example.nativelib.models** { *; }
#-keep class com.example.nativelib.gui** { *; }
#-keep class com.example.nativelib.models.Configuration { *; }
#-keep class com.example.nativelib.OperationResponse { *; }
#-keep class com.example.nativelib.R { *; }
#
## Gson
#-keepattributes Signature
#
## For using GSON @Expose annotation
#-keepattributes *Annotation*
#
## Gson specific classes
#-dontwarn sun.misc.**
#
## Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
## JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
#-keep class * extends com.google.gson.TypeAdapter
#-keep class * implements com.google.gson.TypeAdapterFactory
#-keep class * implements com.google.gson.JsonSerializer
#-keep class * implements com.google.gson.JsonDeserializer
#
## Prevent R8 from leaving Data object members always null
#-keepclassmembers,allowobfuscation class * {
#  @com.google.gson.annotations.SerializedName <fields>;
#}
#
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.preference.Preference
#
## Room
#-keep class androidx.coordinatorlayout.widget.CoordinatorLayout
#
#
## Kotlin
#-keepclassmembers class **$WhenMappings {
#    <fields>;
#}
#-keep class kotlin.Metadata { *; }
#-keepattributes RuntimeVisibleAnnotations
#
## WorkManager
## Rendescript
#-keepclasseswithmembernames class * {
#   native <methods>;
#}
#
#
#-keep class com.google.gson.*
#-keep class com.google.gson.Gson
#-keep class com.google.gson.Gson.*
#-keep class com.google.gson.TypeAdapter
#-keep class com.google.gson.TypeAdapter.*
#
#-keep class android.arch.** { *; }
#
#-keep class com.firebase$firebase.** { *; }