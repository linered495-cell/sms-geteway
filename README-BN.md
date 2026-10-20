# SMS Gateway Starter — Cloud APK Build

এই প্রজেক্টটি একটি **ম্যানুয়াল SMS পরীক্ষার prototype**। এটি এখনো Firebase-এ যুক্ত নয় এবং কোনো HTTP/API Gateway দেয় না। এটি শুধু Android ফোনের SIM দিয়ে ব্যবহারকারীর নিজে দেওয়া নম্বরে SMS পাঠানোর UI ও permission flow রাখে।

## ফোন থেকেই APK বানানোর উপায় (GitHub Actions)

1. ZIP ফাইল Extract করো।
2. GitHub-এ লগইন করে নতুন **public বা private repository** তৈরি করো।
3. Extract করা ফোল্ডারের ভেতরের সব ফাইল repository-তে upload করো। `.github/workflows/build-apk.yml` ফাইলটি অবশ্যই একই path-এ থাকবে।
4. GitHub repository-এর **Actions** ট্যাবে যাও।
5. **Build SMS Gateway APK** workflow বেছে **Run workflow** চাপো।
6. কাজ শেষ হলে সফল run খুলে নিচে **Artifacts** অংশ থেকে `SmsGateway-debug-apk` ডাউনলোড করো।
7. Artifact ZIP Extract করে `app-debug.apk` ফোনে নাও এবং ইনস্টল করো। ফোন চাইলে ওই ফাইল ম্যানেজারের জন্য “Install unknown apps” অনুমতি দাও।

## যদি Actions workflow না দেখা যায়
- repository-তে `.github/workflows/build-apk.yml` ঠিক path-এ আছে কিনা দেখো।
- repository-এর Actions enabled আছে কিনা যাচাই করো।
- প্রথম build-এ কয়েক মিনিট সময় লাগতে পারে।

## সীমাবদ্ধতা ও নিরাপত্তা
- APK debug build; এটি Play Store release signing নয়।
- SMS পাঠাতে সক্রিয় SIM, SMS ব্যালেন্স/প্যাকেজ, ফোন নেটওয়ার্ক ও Android `SEND_SMS` permission লাগবে।
- SMS delivery নিশ্চিত নয়।
- অনাকাঙ্ক্ষিত/বাল্ক SMS পাঠানোর জন্য ব্যবহার কোরো না।
- Google Play-তে প্রকাশ করলে SMS permission নীতি প্রযোজ্য হতে পারে।
- এই prototype-এ Firebase, OTP generation/verification, queue, API key বা background auto-send নেই।
