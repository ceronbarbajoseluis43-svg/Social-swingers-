# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.firebase.** { *; }
-dontwarn com.google.firebase.**

# Firestore
-keep class com.google.firestore.** { *; }
-keep class java.nio.MappedByteBuffer { *; }

# Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.okhttp.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**

# Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.JsonDeserializer

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Coil
-keep class coil.** { *; }
-dontwarn coil.**

# Room
-keep class androidx.room.** { *; }
