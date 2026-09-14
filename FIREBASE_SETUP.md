# Configuración Firebase

La aplicación ya incluye Firebase Authentication, Firestore y Storage. Por seguridad, **no se incluye un `google-services.json` real** porque pertenece a tu proyecto Firebase.

## 1. Crear proyecto
1. Entra a Firebase Console y crea un proyecto.
2. Añade una aplicación Android.
3. Usa exactamente este applicationId: `com.aistudio.swingersfb.kx7a9`.
4. Descarga `google-services.json` y colócalo en `app/google-services.json`.

## 2. Authentication
En Firebase Console → Authentication → Sign-in method, activa **Email/Password**.

## 3. Firestore
Crea una base de datos Firestore y publica las reglas de `firestore.rules`.

Colecciones utilizadas:
- `profiles/{uid}` — perfil real del usuario.
- `conversations/{conversationId}/messages/{messageId}` — mensajes privados en tiempo real.

## 4. Storage
Activa Firebase Storage y publica `storage.rules`.
Las fotos se guardan en `profilePhotos/{uid}/avatar.jpg`.

## 5. Ejecutar
Abre la carpeta raíz en Android Studio, sincroniza Gradle y ejecuta `app`.

Para generar un APK de prueba:
`Build → Build APK(s)`.

Para producción configura además tu firma release en `KEYSTORE_PATH`, `STORE_PASSWORD` y `KEY_PASSWORD`.
