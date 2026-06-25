# ProGuard / R8 rules.
#
# Retrofit, OkHttp, Coil, Room, kotlinx.serialization and Firebase all ship their own
# consumer rules, so only app-specific keeps live here.

# Keep source/line info so Crashlytics stack traces stay readable, but hide the
# original file name.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# --- Room ---
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# --- kotlinx.serialization ---
# @Serializable classes (network DTOs + navigation routes) need their generated serializers.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
