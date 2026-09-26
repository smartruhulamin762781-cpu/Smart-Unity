# Smart Unity — Unity Ads Manager & Monitor

Android app inspired by the supplied Smart Unity mockup.

### Included
- Game ID + Banner / Interstitial / Rewarded placement settings
- Unity Ads initialization status
- Banner, Interstitial and Rewarded ad controls
- Test mode ON by default
- Local ad event counters
- Network/VPN status
- Privacy/consent guidance
- Dark blue/cyan UI matching the reference

### Live ads
This project does **not** contain somebody else's publisher credentials. Open **Configure Ads** and enter your own Unity Game ID and placement IDs.

For development, keep **Test mode ON**. For production, switch it OFF only after your Unity Monetization project and placements are configured.

The app uses the Unity Ads Android SDK dependency:
`com.unity3d.ads:unity-ads:4.20.0`

Revenue/eCPM is intentionally not fabricated inside the app; use the Unity dashboard for authoritative monetization reporting.

### Build
Open the repository in Android Studio, let Gradle sync, then build the `app` module.

The GitHub repository is:
https://github.com/smartruhulamin762781-cpu/Smart-Unity
