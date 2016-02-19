AnimatedRangeBar
=======
The AnimatedRangeBar is similar to an enhanced SeekBar widget, though it doesn't make use of the SeekBar. It provides for the selection of a range of values rather than a single value. The selectable range values are discrete values designated by tick marks; the thumb (handle) will snap to the nearest tick mark.

![](https://lh5.googleusercontent.com/-ucRFjvgXX_g/VsbkzECFw8I/AAAAAAAACKk/lPcnT9UdajQ/w390-h539-no/arb.gif)

U can check the sample app [here](https://github.com/GIGAMOLE/AnimatedRangeBar/tree/master/RangeBarSample).

Download
------------
Just fork and download code and be ready to use it.

Android SDK Version
=========
AnimatedRangeBar requires a minimum sdk version of 7.

Sample
========
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
- thumb animate

Finally, the following property can be set programmatically, but not via XML:

- thumb indices (the location of the thumbs on the RangeBar)

Wiki
======
For more information, see the linked Github Wiki page.

https://github.com/edmodo/range-bar/wiki

Getting Help
======
To report a specific problem or feature request, send me an email: gigamole53@gmail.com

License
======
Apache 2.0. See LICENSE file for details.

Author
=======
Basil Miller - @gigamole

Fork
=======
edmodo/range-bar
