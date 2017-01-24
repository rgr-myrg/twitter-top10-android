# Twitter Top10

Sample App with the following features:

* Fetch Tweets by most recent, popular, or both.
* Control frequency of updates via Settings
* Saves your search history.
* Swipe to refresh current feed.

## Frameworks
* [Retrofit + Gson Converter][RTF]
* [Gson][GSN]
* [Glide][GLD]
* [Material Color Library][MCL]

[RTF]: <https://square.github.io/retrofit>
[GSN]: <https://github.com/google/gson>
[GLD]: <https://github.com/bumptech/glide>
[MCL]: <http://rgr-myrg.github.io/material-color-java>

## Installation
#### Clone the repo
```java
git clone https://github.com/rgr-myrg/twitter-top10-android.git
cd twitter-top10-android/TwitterTop10
```
#### Specify Twitter API Keys
Open **gradle.properties** and provide your keys accordingly:
```java
TwitterApiKey = "APIKEY"
TwitterApiSecret = "APISECRET"
```
#### Build with gradle
```java
./gradlew assembleDebug
```
#### Install app on device
```java
adb install -r app/build/outputs/apk/app-debug.apk
```
Optionally, you can import the project on **Android Studio** and run the app.

## Running Tests
Ensure your emulator or test device is connected. From the command line:
```java
./gradlew cAT
```
### Version
0.1

### License
[MIT License] [MIT]

[MIT]: <https://opensource.org/licenses/MIT>
