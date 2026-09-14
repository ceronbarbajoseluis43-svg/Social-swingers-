# Estado del proyecto

Esta copia está preparada para sincronizar y compilar en Android Studio.

Cambios aplicados:
- Se eliminó la firma debug/release personalizada que apuntaba a archivos de keystore inexistentes.
- Se dejó la firma debug estándar de Android Studio.
- Se dejó la firma release para configurarla desde Android Studio cuando se vaya a publicar.
- Se actualizó Java/Kotlin JVM target a Java 17, requerido por las versiones modernas de Android Gradle Plugin.
- Se normalizó compileSdk a 36.
- Se eliminó la carpeta duplicada accidental `app/~$`.
- Se incorporó el `app/google-services.json` del proyecto Firebase `swingersfb`, cuyo package es `com.aistudio.swingersfb.kx7a9`.

## Siguiente paso
Abrir la carpeta raíz en Android Studio, permitir la sincronización de Gradle y ejecutar `app`.

## Compilación desde GitHub Actions
- Se añadió `.github/workflows/build-apk.yml` para generar un APK release firmado sin necesitar Android Studio en el teléfono.
- El workflow instala Gradle 9.3.1, Java 17 y Android SDK 36, compila `assembleRelease` y verifica la firma.
- El primer build genera una llave de firma y la guarda como artefacto independiente. **Debe conservarse** para futuras actualizaciones de la aplicación.
