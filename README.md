# YemekNET Özel WebView Kiosk APK (Android 5.0+ / E-Tab 5 Uyumlu)

Eski nesil (Android 5.1 / 6.0 - API 21-23) General Mobile E-Tab 5 tabletlerde ve modern Android cihazlarda stabil, hafif ve harici bağımlılık olmadan çalışacak şekilde tasarlanmış gömülü Yemekhane Kiosk WebView uygulamasıdır.

---

## 🚀 Temel Özellikler

1. **Geniş Platform Uyumu:**
   - `package`: `com.cinarli.yemekhane.kiosk`
   - `compileSdk: 33`
   - `minSdk: 21` (Android 5.0 Lollipop desteği - E-Tab 5 uyumlu)
   - `targetSdk: 28` (Eski tabletlerde arka plan servis kısıtlamalarına takılmadan kararlı çalışma)
2. **Kiosk & Fullscreen WebView Deneyimi:**
   - Açılışta otomatik **Immersive Sticky Fullscreen** (Status bar ve Navigation bar tamamen gizli).
   - Ekranın kapanmasını engelleyen `FLAG_KEEP_SCREEN_ON` ve `WakeLock` mekanizması.
   - Varsayılan URL: `https://kapinet.com.tr/yemekhane/attendant/index.php`
   - Donanım hızlandırması (`hardwareAccelerated="true"`), DOM Storage, LocalStorage, JavaScript tam aktif.
3. **Gömülü HTTP Dinleyici & Uzaktan Yönetim (Port 8081):**
   - Sıfır harici kütüphane bağımlılığı (Java `ServerSocket` tabanlı ultra hafif multithreaded mimari).
   - **`/kapat`**: Ekran parlaklığını sıfıra (`0.0f`) indirir ve siyah perde overlay'i aktif eder.
   - **`/ac`**: Ekran parlaklığını normale (`1.0f`) getirir, perdeyi kaldırır ve uyanma kilidini (`WakeLock`) tetikler.
   - **`/yenile`**: WebView içeriğini anında yeniler (`reload()`).
   - **`/durum`**: Cihaz IP, Uptime, RAM kullanımı, API seviyesi ve WebView durumunu JSON formatında döner.
4. **Otomatik Başlama (Boot Receiver):**
   - Cihaz yeniden başlatıldığında (`BOOT_COMPLETED`, `QUICKBOOT_POWERON`) uygulamayı otomatik olarak açar.
5. **CI/CD Entegrasyonu:**
   - `.github/workflows/build.yml` ile her `main` branch push'unda veya manuel tetiklemede otomatik `.apk` derlenir ve GitHub Artifacts olarak sunulur.

---

## 📡 Dahili HTTP Sunucu API Dokümantasyonu (Port 8081)

Uygulama başladığında tabletin yerel IP adresinde **8081** portundan dinlemeye geçer:

### 1. Ekranı Karart / Kapat
```http
GET http://<TABLET_IP>:8081/kapat
```

### 2. Ekranı Aç / Uyandır
```http
GET http://<TABLET_IP>:8081/ac
```

### 3. WebView Sayfasını Yenile
```http
GET http://<TABLET_IP>:8081/yenile
```

### 4. Cihaz Durumu Sorgulama
```http
GET http://<TABLET_IP>:8081/durum
```

---

## 🛠️ Yerel Ortamda Derleme (Build)

### Derleme Komutları:
```bash
# Debug APK Derleme:
./gradlew assembleDebug

# Release APK Derleme:
./gradlew assembleRelease
```
Üretilen APK dosyaları:
- `app/build/outputs/apk/debug/app-debug.apk`
- `app/build/outputs/apk/release/app-release.apk`

---

## ⚙️ Cihaza Kurulum ve Kiosk Olarak Ayarlama

1. **APK Yükleme:**
   ```bash
   adb install -r app-debug.apk
   ```

2. **Varsayılan Başlatıcı (Home App / Launcher) Yapma:**
   - Tablet ayarlarından **Ana Sayfa (Home App)** olarak "YemekNET Kiosk" uygulamasını seçin.
   - Böylece tablet Home tuşuna basıldığında veya yeniden başladığında doğrudan Yemekhane Kiosk ekranı açık kalır.

---

## 📂 Dizin Yapısı

```
.
├── .github/
│   └── workflows/
│       └── build.yml               # GitHub Actions CI/CD hattı
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── app/
│   ├── build.gradle                # MinSdk 21, TargetSdk 28, CompileSdk 33
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml # Kiosk izinleri ve Boot Receiver
│           ├── java/com/cinarli/yemekhane/kiosk/
│           │   ├── MainActivity.java      # Tam ekran WebView & Komut Yönetimi
│           │   ├── BootReceiver.java      # Cihaz açılışında otomatik başlama
│           │   └── KioskHttpServer.java   # Port 8081 gömülü HTTP API sunucu
│           └── res/
│               ├── layout/activity_main.xml
│               ├── values/ (colors, strings, styles)
│               ├── xml/network_security_config.xml
│               └── drawable/ic_launcher.xml
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

