RangeBar
=======
The RangeBar is similar to an enhanced SeekBar widget, though it doesn't make use of the SeekBar. It provides for the selection of a range of values rather than a single value. The selectable range values are discrete values designated by tick marks; the thumb (handle) will snap to the nearest tick mark.

Developers can customize the following attributes (both via XML and programmatically):

- bar color
- bar thickness
- tick height
- number of ticks
- connecting line thickness
- connecting line color
- thumb normal image
- thumb pressed image

If any of the following attributes are specified, the thumb images will be ignored and be replaced with a circle whose properties can be specified as follows:
- thumb radius
- thumb normal color
- thumb pressed color

Finally, the following property can be set programmatically, but not via XML:
- thumb indices (the location of the thumbs on the RangeBar)

Supported on API Level 7 and above.

For more information, see the linked Github Wiki page.

https://github.com/edmodo/range-bar/wiki

![ScreenShot](http://i.imgur.com/q85GhRjl.png)

Installation
=======

**build.gradle**

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.edmodo:rangebar:1.0.0'
}
```

License
=======
Copyright 2013, Edmodo, Inc. 

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Contributions
=======

We'd love for you to participate in the development of our project. Before we can accept your pull request, please sign our Individual Contributor License Agreement. It's a short form that covers our bases and makes sure you're eligible to contribute. Thank you!

http://goo.gl/gfj6Mb
