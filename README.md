# ArcTextView
[![](https://jitpack.io/v/gq97a6/arc_textview.svg)](https://jitpack.io/#gq97a6/arc_textview)
![GitHub](https://img.shields.io/github/license/gq97a6/arc_textview)
![GitHub top language](https://img.shields.io/github/languages/top/gq97a6/arc_textview)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/gq97a6/arc_textview)
![GitHub all releases](https://img.shields.io/github/downloads/gq97a6/arc_textview/total)

<img src="/README_SCREENSHOOT.jpg" alt="ArcTextView Screenshot" width="320" height="auto">

## Setup

### Gradle
First build and publish the project
```
./gradlew arcTextView:publishToMavenLocal
```

Use in separate project
``` Gradle
dependencies {
    implementation("org.labcluster.arctextview:arctextview:2.0.0")
}
```

### Source

Copy sources and `attrs.xml` in module `arctextview` to your project.

## Usage

```
<org.labcluster.arctextview.ArcTextView
    android:layout_width="dimension"
    android:layout_height="dimension"
    app:text="string"
    app:textSize="dimension" />
```

ArcTextView supports following attributes:
```
app:anchorAngle="float"       || Angle at which anchor will be placed
app:anchorType="enum"         || Type of anchor in relation to text
app:drawDebugCircle="boolean" || Controls debug circle drawing
app:fontFamily="string"       || ¯\_(ツ)_/¯
app:text="string"             || ¯\_(ツ)_/¯
app:textColor="color"         || ¯\_(ツ)_/¯
app:textDirection="enum"      || Text rotation direction
app:textOrientation="enum"    || Side to witch text will be facing
app:textPlacement="enum"      || Side of circle on which text will be drawn
app:textSize="dimension"      || ¯\_(ツ)_/¯
app:textStyle="enum"          || Default text typeface style
```

To change ArcTextView properties during runtime use:
```
arcTextView.text = "string"
arcTextView.invalidate()
```

To prevent view clipping ether add padding or include following in parent xml layout.
If necessary, include it in its parent too.
```
android:clipChildren="false"
```
