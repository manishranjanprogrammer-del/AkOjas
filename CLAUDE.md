# AstroSage Kundli Android App Integration Guide

## What is AstroSage Kundli?
AstroSage Kundli (Brand Name: "AstroSage AI") is a comprehensive astrology mobile application built on Android with package name `com.ojassoft.astrosage`. The app enables users to:

**Core Features:**
- **Kundli Generation**: Create detailed birth charts with planetary positions and astrological calculations using native calculation libraries
- **Horoscope Matching**: Marriage compatibility analysis between couples using Kundli matching algorithms
- **Daily Predictions**: Daily, weekly, monthly, and yearly horoscope forecasts
- **Panchang**: Daily Hindu calendar with tithi, nakshatra, yoga, and karana details
- **Astrologer Consultations**: Connect with verified astrologers through multiple communication channels
- **Real-time Communication**: Chat, voice calls, video calls, and live streaming sessions powered by Firebase and Agora
- **Automated Reports**: Access detailed astrological reports (Brihat Kundli, Rajyoga, Shani Sade Sati, etc.)
- **AI Astrology**: Consult with AI-powered astrologers for instant guidance
- **Numerology**: Name and number analysis with numerological predictions
- **Learn Astrology**: Educational content and courses for astrology learning
- **AstroSage Magazine**: Articles, insights, and astrological content
- **Product Store**: Purchase gemstones, yantras, and other astrological products
- **Cloud Storage**: Save unlimited Kundlis in the cloud with subscription plans
- **Multi-Language Support**: Full localization for 10+ Indian languages

**Supported Languages:**
- **Hindi** (hi) - Primary Indian language
- **English** (default) - Global language
- **Bengali** (bn) - West Bengal, Bangladesh
- **Gujarati** (gu) - Gujarat region
- **Tamil** (ta) - Tamil Nadu region
- **Telugu** (te) - Andhra Pradesh, Telangana
- **Marathi** (mr) - Maharashtra region
- **Malayalam** (ml) - Kerala region
- **Kannada** (kn) - Karnataka region
- **Assamese** (as) - Assam region
- **Odia** (or) - Odisha region

**Consultation Types:**
- **Text Chat**: Real-time messaging with astrologers via Firebase Realtime Database
- **Voice Calls**: Traditional phone calls and internet-based audio calls via Agora SDK
- **Video Calls**: Face-to-face consultations via Agora video calling
- **Live Sessions**: Join astrologer live streams using Agora RTC with audience participation

**Monetization Model:**
- **Wallet System**: Users recharge wallet via RazorPay/Paytm to pay for consultations (with bonus credits)
- **Subscription Plans**: Kundli AI+, Unlimited AI Pass, and premium Dhruv plans via Google Play Billing and RazorPay
- **Free Offers**: New users get free consultation sessions and hourly AI sessions
- **Per-minute Billing**: Consultations charged based on duration and astrologer rates

## Project Overview

## Android App Architecture

### Project Structure
```
app/src/main/java/com/ojassoft/astrosage/
├── ui/act/                    # Main activities (splash, login, dashboard)
├── varta/                     # Chat/consultation module
│   ├── ui/activity/          # Chat activities
│   ├── service/              # Background services
│   ├── model/                # Data models
│   └── adapters/             # UI adapters
├── vartalive/                 # Live streaming module (Agora integration)
├── networkcall/               # API client and network layer
├── beans/                     # Data models
├── utils/                     # Utility classes
├── misc/                      # Background services
└── fcmservice/               # Firebase messaging
```

### Key Classes
- **`AstrosageKundliApplication.java`** - Main application class with Firebase and global state management
- **`ActAppSplash.java`** - Splash screen with app initialization and permissions
- **`DashBoardActivity.java`** - Primary dashboard for Varta (consultation) features
- **`ChatWindowActivity.java`** / **`AIChatWindowActivity.java`** - Main chat interfaces
- **`LiveActivityNew.java`** - Live streaming interface using Agora SDK
- **`RetrofitClient.java`** - API client configuration with 10s connection timeout
- **`ApiRepository.java`** - Repository pattern for API calls

## API Integration

### Base URL Structure
**Primary API Endpoints:**
- **Main API**: `https://api2.astrosage.com/`
- **Varta (Chat/Call)**: `https://vartaapi.astrosage.com/sdk/` (this is primary consultation backend)
- **AI Services**: `https://ai.astrosage.com` (AI astrologers) & `https://ai2.astrosage.com` (Kundli AI)
- **PDF Services**: `https://pdf.astrosage.com/`
- **Panchang**: `https://panchang.astrosage.com/`
- **Cloud Services**: `https://ascloud.astrosage.com/`

### Core API Endpoints

#### Authentication & Registration
- `/sdk/registerAS` - User registration with OTP
- `/sdk/submitASotp` - OTP verification
- `/sdk/registerwithtruecallerASv3` - TrueCaller authentication
- `/sdk/userlogin` - Background login for app resume

#### Astrologer Discovery
- `/sdk/astrologers-list-v3` - Main astrologer list with pagination
- `/sdk/astrologer-details` - Individual astrologer profiles
- `/sdk/live-astrologers-list` - Live streaming astrologers

#### Consultation Services
- `/sdk/connect-call` - Primary consultation connection
- `/sdk/connect-chat-v2` - Enhanced chat with Firebase integration
- `/sdk/connect-chat-ai` - AI astrologer chat
- `/sdk/connect-video` - Video call connections
- `/sdk/connect-internet-audio-call` - Internet audio calls via Agora
- `/sdk/connect-internet-video-call` - Internet video calls via Agora

#### Payment & Wallet
- `/sdk/wallet-recharge` - Wallet recharge operations
- `/sdk/create-order-id` - RazorPay order generation
- `/sdk/validate-coupon-code` - Coupon validation
- `/sdk/generate-variable-amount` - Custom amount orders

#### Live Features
- `/sdk/join-audience` - Join live session as audience
- `/sdk/fetch-token` - Agora live streaming tokens
- `/sdk/send-gift` - Send gifts during live sessions

## Business Logic Implementation

### Wallet System
```
Two Balance Types (Hidden from UI):
1. Paid Balance - User recharged amount
2. Bonus Balance - Free/promotional amount

Sources of Bonus Balance:
- Recharge pack bonuses
- Coupon code extras  
- Promotional credits from AstroSage

Important: When user spends only bonus balance, astrologer gets fixed ₹1/min from AstroSage
```

### Subscription Plans
```
1. Kundli AI+ (₹X/month):
   - Unlimited Kundli generation (not astrologer consultation)
   - 1 Automated Report per month
   - Unlimited cloud Kundli storage
   - 10% discount on all services

2. Unlimited AI Pass (₹Y/month):
   - Unlimited chat with AI astrologers

3. Dhruv (₹Z/month):
   - Everything in Kundli AI+ & Unlimited AI Pass
   - Bigger AI Model access
   - 100 Automated Reports per month
```

### Free Offer System
```
New User Journey:
1. Initial Free Session:
   - Human Astrologers: 4 minutes
   - AI Astrologers: 100 seconds
   
2. NEWUSER Offer (after free session):
   - Human Astrologers: ₹10/min (reduced rate)
   - AI Astrologers: ₹1/min (reduced rate)
   
3. Regular Pricing (after NEWUSER offer used)

4. Retention Strategy:
   - Non-converted users get hourly free AI sessions

CRITICAL: All offers require user login - no anonymous consultations
```

### Recharge Bonus Structure
```
One-time Bonuses (First Purchase):
- ₹50 → ₹25 extra
- ₹100 → ₹100 extra  
- ₹199 → ₹101 extra

Recurring Bonuses:
- ₹500 → 50% extra (first time), 7% extra (subsequent)
- Fixed amounts available: ₹50, ₹100, ₹199, ₹500... up to ₹100,000
- Variable amounts also supported
```

## User Experience Flows

### Registration Flow Options
```
1. OTP Registration:
   register → chooseSMSCarrier → submitotp
   
2. TrueCaller Registration:
   registerwithtruecaller* → automatic verification
   
3. Social Media Registration:
   registerwithVRinAs → social platform integration

Spam Prevention: 
- Device, IP, phone frequency validation
- Critical for preventing abuse
```

### Consultation Connection Flow
```
All consultation types delegate to /sdk/connectcall:
1. Extract astrologer profile
2. validateUserAstrologerStatus:
   - User not already on call
   - Astrologer available/free
   - User not blocked
3. checkBalanceAndDuration:
   - Sufficient wallet balance
4. CallConnectionService.connectCall:
   - Network Calls: Exotel/Tata/Twilio
   - Chat: Firebase
   - Internet Calls: Agora

User initiates consultation → App validates → Backend connects → Real-time communication starts
```

### Rating System
```
Rating Types:
1. Free Rating - After free consultation sessions
2. Client Rating - After <5 minutes paid consultation  
3. Verified Rating - After >5 minutes paid consultation

Eligibility: Minimum 180 seconds consultation duration required
```

## Third-Party Integrations

### Firebase Integration
**Project Configuration:**
- **Project ID**: `stoked-virtue-769`
- **Database URL**: `https://stoked-virtue-769.firebaseio.com`
- **API Key**: `AIzaSyCtqFRfVoZLaBpXHfJX5tgUQ5p1ZzHPDK4`

**Services Used:**
- **Firebase Realtime Database**: Chat messaging and real-time updates
- **Firebase Cloud Messaging**: Push notifications via `OjasFirebaseMessagingService`
- **Firebase Analytics**: User behavior tracking
- **Firebase Crashlytics**: Crash reporting
- **Firebase Performance Monitoring**: App performance tracking
- **Firebase Authentication**: User authentication flows

### Agora RTC Integration
**Configuration:**
- **Live Streaming**: Multi-resolution support (320x240 to 1280x720)
- **Voice/Video Calls**: Internet-based calling via `VoiceCallActivity` & `VideoCallActivity`
- **Token Authentication**: Dynamic token fetching via `/sdk/fetch-token`
- **Beauty Effects**: Real-time video enhancement features
- **Live Session Management**: Audience participation in astrologer live streams

### Payment Gateway Integrations

#### RazorPay (Primary)
- **API Key**: `rzp_live_zfWj9iOryaE83d`
- **Order Creation**: `/sdk/create-order-id` endpoint
- **Live Environment**: Production keys configured

#### Paytm (Alternative)
- **Merchant ID**: `Ojasso36077880907527`
- **Integration**: Checksum-based secure transactions

#### Google Play Billing
- **Subscription Plans**: Kundli AI+, Dhruv, Unlimited AI Pass
- **In-app Products**: Report purchases and premium features

### Communication Architecture
```
1. Network Call - Traditional phone calls via Exotel (India) /Tata (India) /Twilio (international)
2. Chat - Firebase Realtime Database messaging
3. Internet Audio Call - Agora RTC audio-only
4. Internet Video Call - Agora RTC video calling
5. Live Audio Session - Agora live streaming with audio participation
6. Live Video Session - Agora live streaming with video participation
```

## Local Data Storage

### SQLite Database (`SqliteDbHelper.java`)
**Chat Data Management:**
```
- Chat message storage and retrieval
- Consultation history tracking
- User profile caching
- Offline kundli data storage
```

### Shared Preferences
**Configuration Storage:**
```
- User authentication tokens
- App settings and preferences
- Language selection (11 supported languages)
- Notification settings
- Theme preferences
```

### File Storage
**Local File Management:**
```
- Generated kundli PDFs
- Downloaded astrological reports
- Cached astrologer images
- User profile images
```

## Key Android Components

### Activities Structure
**Main App Flow:**
1. **`ActAppSplash`** → App initialization and permissions
2. **`ActivityLoginAndSignin`** → User authentication
3. **`ActAppModule`** → Main home screen
4. **`DashBoardActivity`** → Varta consultation dashboard
5. **`ChatWindowActivity`** → Chat interface
6. **`LiveActivityNew`** → Live streaming interface

### Services & Background Processing
**Foreground Services:**
- **`OnGoingChatService`** - Active chat session management
- **`AgoraCallOngoingService`** - Voice/video call management
- **`AstroAcceptRejectService`** - Chat request handling

**Background Services:**
- **`Loginservice`** - Session management
- **`PreFetchDataservice`** - Data preloading
- **`CallStatusCheckService`** - Call status monitoring

### Receivers & Notifications
**Broadcast Receivers:**
- **`ChatStatusReciever`** - Chat state changes
- **`AgoraCallStatusReceiver`** - Call state management
- **`CustomAIBroadcastReceiver`** - AI notification handling

**Push Notifications:**
- **`OjasFirebaseMessagingService`** - FCM message handling
- **`MyCloudRegistrationService`** - FCM token management

## Performance & Error Handling

### Network Configuration
**Retrofit Client Settings:**
```
- Connection Timeout: 10 seconds
- Write Timeout: 10 seconds
- Read Timeout: 30 seconds (60s for AI services)
- Cookie Management: JavaNetCookieJar with CookieManager
- HTTP Logging: Enabled for debugging
```

### Error Handling Patterns
**Network Resilience:**
```
- Graceful API timeout handling
- Retry logic with exponential backoff
- Offline mode for kundli generation
- Cached data fallback mechanisms
- Connection state monitoring
```

### Performance Optimizations
**Mobile-Specific:**
```
- Agora SDK: Multi-resolution video support
- Firebase: Real-time database for chat
- Image Loading: Glide library for efficient caching
- Background Services: Foreground service management
- Memory Management: Large heap enabled, MultiDex support
```

**Build Optimizations:**
```
- ProGuard enabled for release builds
- ABI splits enabled (armeabi-v7a, arm64-v8a, x86, x86_64)
- Density splits for APK size optimization
- Language splits disabled (to maintain all 11 language localizations)
```

## Internationalization (i18n)

### Multi-Language Architecture
**Localization Support:**
```
- 11 Indian Languages: Complete string localization
- Regional Content: Language-specific astrological content
- RTL Support: Proper layout handling for different scripts
- Font Support: Custom fonts for Indian languages
- Cultural Adaptation: Regionally appropriate UI elements
```

**Language Implementation:**
```
- Resource Directories: values-{language_code}/strings.xml
- Dynamic Language Switching: Runtime language change capability
- Server Content: Multi-language API responses
- Astrological Terms: Localized terminology for each language
```

## Automated Reports
```
Available Reports:
- Brihat Kundli
- Rajyoga Report  
- Shani Sade Sati Report
- Personalized Horoscope
- Many others

Purchase Options:
- Direct purchase with cash
- Purchase from wallet balance
- Subscription-based access (Dhruv plan: 100 reports/month)
```

## Development Guidelines

### Authentication Implementation
**Multi-Method Support:**
```
- OTP Registration: registerAS → submitASotp flow
- TrueCaller Integration: registerwithtruecallerASv3 endpoint
- Social Login: Facebook and Google Sign-in
- Session Management: userlogin for background authentication
- Token Persistence: Shared preferences for auth tokens
```

### Consultation Flow Implementation
**Connection Management:**
```
- Astrologer Availability: Check via astrologers-list-v3
- Connection Types: Chat (Firebase), Audio/Video (Agora), Live (Agora)
- Session State: Foreground services for active consultations
- Payment Integration: Wallet validation before connection
- Rating System: Minimum 180-second consultation for ratings
```

### Real-time Features
**Firebase Integration:**
```
- Database Path Structure: /users/{userId}/chats/{chatId}
- Message Sync: Real-time listener for chat updates
- Push Notifications: FCM via OjasFirebaseMessagingService
- Offline Support: Local SQLite database backup
```

**Agora Implementation:**
```
- Token Management: Dynamic token fetching per session
- Video Configurations: Multiple resolution support
- Live Streaming: Audience participation with gifts
- Call State: Proper cleanup on session end
```

### UI/UX Best Practices
**Performance Considerations:**
```
- Image Loading: Glide for astrologer profile images
- List Management: RecyclerView with efficient adapters
- Background Tasks: Use services for long-running operations
- Memory Management: Proper lifecycle handling in activities
```

### Security Implementation
**Data Protection:**
```
- API Authentication: Package name and signed string validation
- Payment Security: RazorPay signature verification
- User Privacy: No sensitive data in logs
- Session Security: Proper token invalidation on logout
```

## Project Libraries & Dependencies

### Core Android Libraries
```
- AndroidX: AppCompat, Material Design, ConstraintLayout
- Firebase: Database, Messaging, Analytics, Crashlytics
- Retrofit: HTTP client with OkHttp interceptors
- Glide: Image loading and caching
- Agora: RTC SDK for live streaming and calls
```

### Payment & Authentication
```
- RazorPay: checkout:1.6.41
- Google Play Billing: billing:7.0.0
- TrueCaller SDK: truecaller-sdk:3.0.0
- Facebook Login: facebook-login:latest.release
```

### Custom Libraries
```
- HoroN: Astrological calculations
- LibOjasKpExtension: KP astrology extensions
- libojassoft1.0: Core astrology computation
- libmsv: Additional calculation modules
```

## Critical Security Information

### 🚨 Security Vulnerabilities Identified
**HIGH-RISK ISSUES:**
1. **Disabled Certificate Validation**: `HttpsTrustManager.java` accepts ALL SSL certificates
2. **Weak Encryption**: AES ECB mode usage (cryptographically insecure)
3. **Clear Text Traffic**: App allows unencrypted HTTP communication
4. **No Certificate Pinning**: Vulnerable to man-in-the-middle attacks

**Security Implementation Details:**
- **AES Encryption**: `AesHelper.java` (uses weak ECB mode)
- **RSA Encryption**: `RSAUtility.java` for asymmetric encryption
- **Payment Security**: PayTM checksum verification via `GetCheckSum.java`
- **Session Management**: Custom authentication via `Credentials.java`

### 🔐 Authentication Security
- **API Authentication**: Custom date-based security keys
- **OTP Verification**: Multiple verification endpoints
- **Payment Keys**: RazorPay `rzp_live_zfWj9iOryaE83d`, PayTM `Ojasso36077880907527`

## Database Architecture

### SQLite Schema
**Primary Database**: `OjassoftMKDb` (Version 12)

**Core Tables:**
```sql
-- Personal Kundli Information
tblHoroPersonalInfo (id, M_UserId, M_PName, M_Sex, M_Day, M_Month, M_Year, 
                     M_Hr, M_Min, M_Sec, M_Place, coordinates, timestamps)

-- Location Data
tblCity (id, cityName, coordinates, timeZoneId)
tblTimeZone (id, timezoneName, zonevalue)

-- Chat & AI History
tblKundaliAiChatHistory (conversation_id, astrologer_id, local_chart_id)
tblKundaliChatMessage (question_text, answer_text, timestamps)
```

**Data Synchronization:**
- **Local-to-Server**: `SyncKundliService` handles batch uploads
- **Conflict Resolution**: Server timestamp precedence
- **Offline Support**: Full functionality without internet

## Build Configuration Details

### ProGuard/R8 Rules
**Critical Keep Rules:**
```proguard
# Agora SDK Protection
-keep class io.agora.**{*;}

# Payment Gateway Protection  
-keep class com.razorpay.** {*;}

# App Data Models
-keep public class com.ojassoft.astrosage.beans.** { *; }
-keep public class com.ojassoft.astrosage.varta.model.** { *; }
```

### Native Libraries (JNI/NDK)
**Agora SDK Components:**
- `libagora-core.so` - Core RTC functionality
- `libagora-rtc-sdk.so` - Real-time communication
- **AI Extensions**: echo cancellation, noise suppression, face detection
- **Architectures**: arm64-v8a, armeabi-v7a, x86, x86_64

**Custom Astrology Libraries:**
- `panchangjava.jar` - Panchang calculations
- `suntimes.jar` - Astronomical computations

## Deep Links & URL Handling

### Supported URL Patterns
**Primary Domains:**
- `www.astrosage.com` - Kundli, matching, horoscopes
- `varta.astrosage.com` - Consultations and live streams
- `marriage.astrosage.com` - Marriage portal
- `horoscope.astrosage.com` - Horoscope content

**URL Processing**: Central handling in `ActAppModule.actionOnIntent()`

**Android App Links**: Configured with domain verification for `www.astrosage.com`

## Background Services & Notifications

### Foreground Services
**Real-time Communication:**
- `OnGoingChatService` - Active chat monitoring
- `AgoraCallOngoingService` - Voice/video call management
- `AstroAcceptRejectService` - Call/chat request handling

**Data Services:**
- `PreFetchDataservice` - Background data synchronization
- `SyncKundliService` - Kundli cloud synchronization

### FCM Implementation
**Primary Service**: `OjasFirebaseMessagingService`
**Notification Types**: Chat, calls, AI notifications, birthday reminders
**Firebase Config**: Project `stoked-virtue-769`

## UI Architecture & Theming

### Theming System
**Dual Theme Support**: Light/Dark mode with 387+ defined colors
**Primary Colors**: `#ffc107` (amber), `#e65100` (deep orange)
**Font System**: Roboto, Open Sans, Poppins + regional language fonts

### Custom Components
- **CircularImageView**: Custom profile image view
- **MaterialSearchView**: Voice-enabled search
- **Custom Rating Bars**: Astrological rating displays
- **50+ Custom Adapters**: Specialized for astrology content

### Responsive Design
- **Multi-screen Support**: Tablet layouts, orientation handling
- **Accessibility**: High contrast, scalable text, screen reader support

## Testing & Debugging

### Debug Configuration
**Debug Build Features:**
- Firebase Performance monitoring disabled
- No code obfuscation
- Extended logging enabled

**Testing Infrastructure:**
- Custom test harnesses for astrology calculations
- Integration tests for Agora SDK
- Payment gateway testing environments

## Performance & Monitoring

### Performance Optimizations
**Network**: 10s timeout, cookie management, retry logic
**Memory**: Large heap enabled, MultiDex support
**Image Loading**: Glide library with caching
**Background Tasks**: Foreground service management

### Analytics Integration
- **Firebase Analytics**: User behavior tracking
- **Crashlytics**: Crash reporting and analysis
- **Performance Monitoring**: Real-time performance metrics

## Development Warnings

### Critical Issues to Address
1. **Fix Certificate Validation**: Remove `HttpsTrustManager` that accepts all certificates
2. **Upgrade Encryption**: Replace AES ECB with GCM/CBC mode
3. **Add Certificate Pinning**: Implement SSL pinning for API endpoints
4. **Root Detection**: Add security for compromised devices
5. **Code Obfuscation**: Implement proper R8/ProGuard for release builds

### 🚨 Hardcoded Credentials Analysis
**ANDROID SECURITY REALITY - All APK contents are public:**

**🟢 SAFE (Standard for Android Apps):**
1. **RazorPay Key**: `rzp_live_zfWj9iOryaE83d` - Public client key by design
2. **Firebase API Key**: `AIzaSyCtqFRfVoZLaBpXHfJX5tgUQ5p1ZzHPDK4` - Public client key
3. **Facebook App ID**: `213654749671068` - Public identifier
4. **TrueCaller Client ID**: `pr3qjxy9lsaninfusk128b5a5aj8dg7cqbt4jzek94s` - Client identifier

**🟡 REVIEW NEEDED:**
5. **Facebook Client Token**: `3191aaa51249d6494ccad34e4fc01871` - Consider server-side handling

**🔴 PROBLEMATIC:**
6. **PayTM Merchant ID**: `Ojasso36077880907527` - Could enable merchant impersonation

**ANDROID APP SECURITY NOTES:**
- APKs are public - anyone can decompile and extract all strings/resources
- BuildConfig values also end up in APK (not secure)
- Only true secrets should be server-side or use Android Keystore
- Client-side keys (RazorPay, Firebase) are designed to be public

### Best Practices
- Use BuildConfig for sensitive configuration
- Store secrets in environment variables or secure vaults
- Implement automated secret scanning
- Regular security audits and key rotation
- Test on various device configurations
- Validate all astrological calculations independently

## Development Environment Setup

### Required Dependencies
- **Minimum SDK**: 23 (Android 6.0)
- **Target SDK**: 34 (Android 14)
- **Gradle**: 8.7 with Android Gradle Plugin 8.5.1
- **Java**: Version 8 compatibility required

### External Service Requirements
- **Firebase Project**: Configured for messaging, analytics, crashlytics
- **Agora Account**: For real-time communication features
- **Payment Gateways**: RazorPay, PayTM, Google Play Billing setup
- **TrueCaller SDK**: Version 3.0.0 integration

This document provides comprehensive Android development context for the AstroSage Kundli application, covering architecture, integrations, security considerations, and critical implementation guidelines.