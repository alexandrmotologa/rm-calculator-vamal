# ==========================================
# R8 & ProGuard Optimization Rules
# ==========================================

# Kotlin Coroutines
-keepattributes *Annotation*,InnerClasses,EnclosingMethod,Signature

# Kotlinx Serialization & XML Serialization
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}
-keepclassmembers class * extends kotlinx.serialization.KSerializer {
    *;
}
-keepclassmembers class **$$serializer {
    *;
}
-keepclassmembers class * {
    @nl.adaptivity.xmlutil.serialization.* <fields>;
}

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Retrofit
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
