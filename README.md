# Le2LeJosEv3-ColorSorter
This is the LEGO® Mindstorms EV3 **Color Sorter** example program in the Java programming language that uses a Java Implementation of _LEGO® Mindstorms EV3 Programming Blocks (icons)_ on LeJOS EV3. 

You can find the building instructions and the LEGO® icon-based program of the _Color Sorter_ in the LEGO® icon based (LabView-based) Programming Environment (Education Edition).
The Color Sorter is one of the Core Set Models.
You can download the LEGO® Programming Environment at https://education.lego.com/en-us/downloads/mindstorms-ev3/software (or one of the other language pages).

## Dependencies
This project depends on the **Le2LeJosEv3** Library (see https://github.com/robl0377/Le2LeJosEv3) that sits on top of the current version of the LeJOS EV3 framework. 
Please add the JAR file of the _Le2LeJosEv3_ Library _(le2lejosev3.jar)_ into this project's classpath before running it. The LeJOS Eclipse plugin will take care of the transfer of the library and the program JAR files to the EV3 brick..

In this project I am using the **LeJOS EV3 v0.9.1beta** framework (see https://sourceforge.net/projects/ev3.lejos.p/) and a standard LEGO® Mindstorms EV3 Brick.

## Resources
The program uses several sound and image files that are in the project's _resources_ directory. 
Please upload them to your EV3 Brick via SCP to the directory _/home/lejos/lib_.

The **image files** are converted from those found in the installation directory of the LEGO® Programming Environment (Home Edition):
_c:\Program Files (x86)\LEGO Software\LEGO MINDSTORMS EV3 Home Edition\Resources\BrickResources\Retail\Images\files\_. 

You can convert them with the LeJOS EV3 Image Converter program that is part of the EV3 Control Center or can run stand-alone from the bin directory of the LeJOS EV3 installation (ev3image.bat). Alas, the Image Converter is still adapted to the old Mindstorms NXT Brick and displays a warning if the loaded image exceeds 100x64 pixels. However, it correctly converts images for the Mindstorms EV3 with up to 178x128 pixels.

The **sound files** are converted from those MP3 sound files found at www.thumb.com.tw/images/blog/lego_ev3_sound_files/ev3_sounds.zip.
There are several ways to convert the MP3 sound files to WAV. 
I was using the online converter at https://audio.online-convert.com/convert-to-wav. 
Be sure to set _Change audio channels:_ to _mono_.

---
LEGO® is a trademark of the LEGO Group of companies which does not sponsor, authorize or endorse this site.
