# One App Android

## Description
*What is the purpose of this project?*

This project is a mobile application for hackers/organizers/mentors/sponsors at the hackathon. Hackers would be able to get announcements, get a QR code for checking/food/t-shirts, as well as see the schedule and map for the hackathon. Organizers would be able to scan for checkin/food/t-shirts for analytics that can be used after or even during the hackathon. Any more ideas to expand this project are always welcome.

## Inspiration
*How did this project come to be?*

We had started using an inhouse hybrid mobile application to keep track of analytics to get a better idea of how certain aspects of the hackathon were running such as food consumption and optimization for checkin. This project expanded into a public native mobile application so hackers had easier access to their QR code as well as organizers with their scanners. Additional information of the hackathon were incorporated so that everyone would be able to stay up to date on events that are happeneing wherever they may be in the venue.

## Installation Guide

### For Architects

1. Download and install [Android Studio](https://developer.android.com/studio/)
2. Download and install [Git](https://git-scm.com/downloads)
2. Open Terminal on your machine (on windows this is called Command Prompt)
3. Navigate to the directory you want to download the repository to by doing `cd SomeFolder/SomeOtherFolder/SomeOtherFolder`
4. Clone the repository by doing `git clone https://github.com/HackRU/one-app-android.git`
5. Open the folder you created (which is called `one-app-android`) in Android Studio

### For Users

Download the app from the Google Play store [here](https://play.google.com/store/apps/details?id=org.hackru.oneapp.hackru)

## Example Uses

List of features goes here...

## Style Guide
#### General
* Contributors should follow our [Git Style Guide](https://github.com/agis/git-style-guide)
* Contributors should follow Google's [Android Kotlin Style Guide](https://android.github.io/kotlin-guides/style.html)

#### Gradle
* When you add a dependency, put it underneath its relevant category. If there is no category that is specific enough, make a new one with a multi-line comment. Categories should be for specific functionality
    * For example: `/* QR Codes */` would encapsulate all the dependencies that are needed for generating and scanning QR codes

* When you add a dependency, briefly describe its purpose in a single-line comment above it
	* For example:
      ```
      // Converts json to Java objects and vise versa
      implementation 'com.google.code.gson:gson:2.8.5'
      ```

#### Resource Files
* Drawable and Layout resource files should be prefixed with what type of UI element they are
	* A drawable example: `ic_settings_black_24dp.xml` is prefixed `ic_` for icon
	* A layout example: `rv_item_announcement.xml` is the layout for an announcement list item that is displayed using a RecyclerView (`rv_item` for RecyclerView item)

* Since Kotlin allows us to reference views without using `findViewById(Int)`, IDs for views should be lowercase with words separated by underscores. This allows us to quickly identify in Kotlin if an object is a reference to a view or not. In most cases, IDs are prefixed with the type of view they are.
	* For example: `rv_announcements` is the ID of the RecyclerView for announcements
	* For example: `button_retry` is the ID of a retry button

## TO-DO List

#### Easy
* Convert the timer, announcements, login page, and QR floating action button to Kotlin
* Implement info activity

#### Medium
* Convert events to Kotlin and implement a ViewPager for Saturday and Sunday
* Implement the map with [picasso](http://square.github.io/picasso/)

#### Hard
* Re-implement networking
* Re-implement the scanner
* Coordinate with the backend (lcs) about announcement notifications

## Links to Further docs

TBA
