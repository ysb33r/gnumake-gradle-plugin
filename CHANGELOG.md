# CHANGELOG

## Version 1.0
- A default task called `make` is added when the plugin is applied.
- Add file monitors for input or output so that Gradle will known when to run the `make` tasks
- DSL improvements
- Add rules such that `gradle makeClean` will run `make clean`
- If you do `:a:b:c:makeClean` Gradle will execute the `make` task in `a/b/c`.
- If no Makefile is provided, make will use startand rules to look for a Makefile.

## Version 0.3
- Add `gnumake` extension

## Version 0.2
- Change plugin id to `org.ysb33r.gnumake`

## Version 0.1
- Initial release