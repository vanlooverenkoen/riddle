### Riddle ### 

[ ![Download](https://api.bintray.com/packages/vanlooverenkoen/maven/riddle/images/download.svg) ](https://bintray.com/vanlooverenkoen/maven/riddle/_latestVersion)

**What does it do?**

Riddle is a library you can import in your project to send your logs to the Riddle app. The Riddle app will be available soon. An overlay will be shown in Android where you can view all the logs by your app. You can also filter on certain parameters.

**How To Use**

Import in root gradle

    allprojects {
        repositories {
            ...
            maven { url "https://dl.bintray.com/vanlooverenkoen/maven" }
            ...
        }
    }

Apply the plugin by adding next command at the top of your app/build.gradle file

    dependencies {
        implementation "be.vanlooverenkoen:riddle:0.0.2"
    }
