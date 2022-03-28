# ArcTextView
[![](https://jitpack.io/v/gq97a6/ArcTextView.svg)](https://jitpack.io/#gq97a6/ArcTextView)
![GitHub](https://img.shields.io/github/license/gq97a6/ArcTextView)
![GitHub top language](https://img.shields.io/github/languages/top/gq97a6/ArcTextView)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/gq97a6/ArcTextView)
![GitHub all releases](https://img.shields.io/github/downloads/gq97a6/ArcTextView/total)

<img src="/README_SCREENSHOOT.jpg" alt="ArcTextView Screenshot" width="320" height="auto">

## Setup

### Gradle

``` Gradle
dependencies {
    implementation 'com.github.gq97a6:ArcTextView:1.0.0'
}
```

### Source

Copy sources and `attrs.xml` in module `arctextview` to your project.

## Usage

To change ArcTextView properties during runtime use:
```
arcTextView.text = "string"
arcTextView.invalidate()
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
