# Swingers FB — Firebase Edition

Versión ampliada de la aplicación Android con:

- Registro e inicio de sesión con Firebase Authentication (correo/contraseña).
- Perfiles reales almacenados en Cloud Firestore.
- Foto de perfil subida a Firebase Storage.
- Descubrimiento de perfiles registrados.
- Chat privado en tiempo real con Cloud Firestore.
- Persistencia local Room para el contenido existente.
- Control 18+ y modo discreto.

## Antes de compilar
Lee `FIREBASE_SETUP.md`. Debes crear tu proyecto Firebase y colocar el archivo real `app/google-services.json`.

El proyecto usa `applicationId = com.aistudio.swingersfb.kx7a9`, por lo que la app Android de Firebase debe registrarse con ese package name.
