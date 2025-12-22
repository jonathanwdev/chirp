# Regras essenciais para Compose Desktop
-keepattributes *Annotation*, Signature, EnclosingMethod, InnerClasses
-dontwarn androidx.compose.desktop.ui.**

# KOIN: Impede que o Koin perca as definições de injeção
-keep class io.insertkoin.** { *; }

# ROOM: Necessário para o banco de dados funcionar no Desktop
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep interface * extends androidx.room.Dao
# Se estiver usando o driver SQLite nativo (comum no KMP Desktop)
-keep class androidx.sqlite.** { *; }
-dontwarn androidx.sqlite.**

# KTOR / WEBSOCKETS / SERIALIZATION
-keep class io.ktor.** { *; }
-keep class kotlinx.serialization.json.** { *; }
# Proteja seus modelos de dados para que o JSON funcione
-keep class com.plcoding.chirp.models.** { *; }

# COROUTINES
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}