# 🚀 Guía Completa: Compilar la APK de Social Swingers

## Requisitos Previos

- **Android Studio** (versión 2022.1 o superior)
- **JDK 11 o superior**
- **Android SDK** (compilado con SDK 34)
- **Git** instalado
- **Gradle** (se descarga automáticamente con Android Studio)

---

## ✅ Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/ceronbarbajoseluis43-svg/Social-swingers-.git
cd Social-swingers-
```

---

## ✅ Paso 2: Abrir Proyecto en Android Studio

1. Abre **Android Studio**
2. Selecciona **File → Open**
3. Navega a la carpeta del proyecto clonado
4. Haz clic en **Open**
5. Android Studio sincronizará automáticamente Gradle

---

## ✅ Paso 3: Verificar Configuración de Firebase

El archivo `app/google-services.json` ya está incluido en el proyecto.

**Verifica que incluya:**
- `project_id`: swingersfb
- `package_name`: com.aistudio.swingersfb.kx7a9
- API Keys y configuraciones

---

## ✅ Paso 4: Configurar Firebase Console

### En Firebase Console (https://console.firebase.google.com):

1. **Authentication**
   - Ve a Authentication → Sign-in method
   - Activa **Email/Password**
   - Activa **Anonymous** (opcional)

2. **Firestore Database**
   - Crea una base de datos en modo desarrollo
   - Publica las reglas de seguridad:
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /profiles/{uid} {
         allow read: if request.auth != null;
         allow write: if request.auth.uid == uid;
       }
       match /conversations/{conversationId} {
         match /messages/{messageId} {
           allow read: if request.auth != null;
           allow create: if request.auth != null;
         }
       }
     }
   }
   ```

3. **Storage**
   - Activa Firebase Storage
   - Publica estas reglas:
   ```
   rules_version = '2';
   service firebase.storage {
     match /b/{bucket}/o {
       match /profilePhotos/{uid}/{allPaths=**} {
         allow read: if request.auth != null;
         allow write: if request.auth.uid == uid && request.resource.size < 5 * 1024 * 1024;
       }
     }
   }
   ```

---

## ✅ Paso 5: Compilar la APK en Android Studio

### Opción A: APK de Prueba (Debug)

1. En Android Studio, ve a **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Espera a que se complete la compilación
3. Verás un mensaje: `APK(s) generated successfully`
4. Haz clic en **Locate** para encontrar la APK

**Ruta**: `app/build/outputs/apk/debug/app-debug.apk`

### Opción B: APK de Producción (Release)

1. Ve a **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Selecciona **Release** (requiere keystore)
3. Si no tienes keystore, Android Studio te pedirá crear uno

**Ruta**: `app/build/outputs/apk/release/app-release.apk`

---

## ✅ Paso 6: Instalar la APK en tu Dispositivo

### Opción A: Desde Android Studio
1. Conecta tu dispositivo Android via USB
2. Ve a **Run → Run 'app'**
3. Selecciona tu dispositivo
4. La app se instalará y ejecutará automáticamente

### Opción B: Desde línea de comandos
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Opción C: Transferencia manual
1. Descarga la APK en tu PC
2. Conecta tu dispositivo
3. Copia la APK al dispositivo
4. Abre un gestor de archivos y ejecuta la APK

---

## ✅ Paso 7: Primeros Pasos en la App

1. **Registrarse**
   - Ingresa email, contraseña y nombre de usuario
   - La app creará automáticamente tu perfil en Firestore

2. **Cargar Foto de Perfil**
   - Selecciona una foto desde tu galería
   - Se guardará en Firebase Storage

3. **Explorar Perfiles**
   - Desplázate por los perfiles disponibles
   - Envía likes o mensajes

4. **Chatear**
   - Abre conversaciones en tiempo real
   - Los mensajes se sincronizarán con Firestore

---

## 🛠️ Solución de Problemas

### Error: "google-services.json not found"
- Verifica que el archivo esté en `app/google-services.json`
- Sincroniza Gradle nuevamente

### Error: "Compilation failed"
- Limpia el proyecto: **Build → Clean Project**
- Reconstruye: **Build → Rebuild Project**

### Error: "Firebase Authentication not enabled"
- Ve a Firebase Console
- Activa Email/Password en Authentication

### Error: "Firestore permission denied"
- Verifica las reglas de seguridad en Firestore
- Asegúrate de estar autenticado

### Error: "Package name mismatch"
- Verifica que `applicationId` en `build.gradle` sea: `com.aistudio.swingersfb.kx7a9`
- Debe coincidir con el configurado en Firebase Console

---

## 📦 Distribución de la APK

Una vez compilada la APK, puedes:

1. **Compartir via Google Play Store**
   - Crea una cuenta de desarrollador
   - Sube la APK firmada

2. **Distribución directa**
   - Sube la APK a un servidor
   - Comparte el enlace de descarga

3. **TestFlight (iOS) o Beta Testing (Android)**
   - Usa Firebase App Distribution

---

## 🔑 Información de la App

```
📱 Nombre: Social Swingers
📦 Package Name: com.aistudio.swingersfb.kx7a9
🎯 Target SDK: 34
🎯 Min SDK: 24
👥 Edad Mínima: 18+
🌐 Backend: Firebase
💾 Base de Datos Local: Room SQLite
```

---

## 📝 Características Implementadas

✅ Autenticación con Firebase (Email/Contraseña)
✅ Perfiles de usuario en Firestore
✅ Chat en tiempo real
✅ Almacenamiento de fotos en Storage
✅ Persistencia local con Room
✅ UI moderna con Jetpack Compose
✅ Material Design 3
✅ Validación de edad (18+)
✅ Modo discreto

---

## 🚀 Próximos Pasos Opcionales

1. **Agregar más funcionalidades**
   - Video llamadas con Twilio
   - Pagos con Stripe
   - Notificaciones push

2. **Optimización**
   - Minificación con ProGuard
   - Compresión de recursos
   - Caché de imágenes

3. **Seguridad**
   - Encriptación de datos
   - Verificación de dos factores
   - Rate limiting

---

## 📞 Soporte

Para problemas o preguntas:
- Revisa la documentación oficial de Firebase
- Consulta la documentación de Android
- Abre un issue en GitHub

---

**¡Tu aplicación Social Swingers está lista para usar! 🎉**
