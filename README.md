# 📱 Byte Breaker — Android Image Compressor

Byte Breaker is a lightweight Android app that allows users to **select, resize, and compress images** while maintaining high visual quality.  
It’s perfect for reducing image file sizes before uploading or sharing — and saves results directly to your device’s **Pictures/ByteBreaker** folder.

---

## ✨ Features
- 🖼️ Select any image from your gallery  
- ⚙️ Adjust compression **quality**, **width**, and **height**  
- 💾 Saves compressed images to `/Pictures/ByteBreaker/` (public storage)  
- 🔍 Automatically refreshes in Gallery after saving  
- 📊 Displays original and compressed image sizes  
- 🧠 Built with [Zelory Compressor](https://github.com/zetbaitsu/Compressor)  
- 🛡️ Safe permission handling using [Dexter](https://github.com/Karumi/Dexter)

---

## 🧰 Tech Stack
- **Language:** Java ☕  
- **Framework:** Android SDK  
- **Libraries:**
  ```gradle
  implementation 'id.zelory:compressor:3.0.1'
  implementation 'com.karumi:dexter:6.2.3'
  ```
---
## 🪜 How It Works
1. Tap “Pick Image” → choose an image from your gallery.
2. Set desired quality, width, and height.
3. Tap “Compress” → the app compresses the image and saves it publicly.

---
## ⚙️ Setup & Installation
1. Clone this repository:
```git clone https://github.com/ronak1743/Byte-Breaker```
2. Open in Android Studio.
3. Ensure these dependencies exist in your build.gradle (app) file:
```
implementation 'id.zelory:compressor:3.0.1'
implementation 'com.karumi:dexter:6.2.3'
```

4. Add the following permissions in AndroidManifest.xml:
```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
5. Build and run on a real Android device (recommended).

---
## 📁 File Structure
```
app/
 └── src/
      └── main/
           ├── java/com/ronak/bytebreaker/MainActivity.java
           ├── res/layout/activity_main.xml
           └── AndroidManifest.xml
```

---
## 🔒 Permissions
This app requests:  
`READ_EXTERNAL_STORAGE` → to select images  
`WRITE_EXTERNAL_STORAGE` → to save compressed results  
Handled gracefully with Dexter permission library.  

---
## 📧 Contact  
👨‍💻 **Developer:** [Ronak Gondaliya](https://github.com/ronak1743)  
📫 **Email:** [gondaliyaronak78@gmail.com](mailto:gondaliyaronak78@gmail.com)  

Feel free to reach out for collaboration, suggestions, or questions!
