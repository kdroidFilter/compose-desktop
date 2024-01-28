# Gardez vos classes d'entrée de point avec toutes leurs méthodes et champs.
-keep public class * {
    public protected *;
}

# Ignorez les avertissements pour les bibliothèques que vous n'utilisez pas.
-dontwarn java.**
-dontwarn javax.**

# Ignorer les avertissements spécifiques (ajustez selon vos besoins).
-dontwarn kotlinx.**
-dontwarn ch.qos.logback.**

# Conservez les noms de méthodes et de champs utilisés par les classes reflétées (ajustez selon vos besoins).
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
    @com.google.gson.annotations.Expose <fields>;
    @com.google.gson.annotations.SerializedName <methods>;
    @com.google.gson.annotations.Expose <methods>;
}

# Optimisations
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Obfuscation

# Conservez les numéros de ligne pour générer des traces de pile plus informatives.
-keepattributes SourceFile,LineNumberTable

# Si vous utilisez des annotations Retrofit ou Gson.
-keepattributes *Annotation*

# Pour éviter que ProGuard ne supprime les méthodes invoquées dynamiquement, etc.
-keepclassmembers class * {
    @org.jetbrains.annotations.Nullable <methods>;
    @javax.annotation.Nullable <methods>;
}

# Ignorer les avertissements pour les fichiers de ressources en double


# Ignorer les avertissements pour les classes manquantes
-dontwarn org.jspecify.annotations.Nullable
-dontwarn org.jspecify.annotations.NullMarked

# Ignorer les avertissements pour les classes manquantes liées à JSoup
-dontwarn org.jsoup.**
