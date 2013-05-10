A collection of Gradle plugins.



=====================
```gradle

buildscript {
  // TODO: Complete this
}

task runMake ( type : GnuMake ) {
  targets = [ 'build', 'install' ]
  flags = [ DESTDIR : '/path/somewhere', BUILD_NUMBER : 1234 ]
}
```