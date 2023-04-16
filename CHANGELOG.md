Changelog
===

# 15th April 2023

- Improved Javadoc for:
  - [X] Utility classes
  - [X] Placeholder API
  - [X] Soft dependencies classes
- Added Javadoc for
  - [X] Custom tab API
- [X] Renamed `Line` to `Text`
- [X] Renamed `ErrorHandler` to `Debugger`
- [X] Improvements to `Text`
  - [X] Instead of having `Text#build` with different parameters for Text with placeholders and samples. Now the class has the methods `#isSample`, `#hasPlaceholders` to check and `#setSample` and `#setHasPlaceholders` to set the values.
  - [X] Moved static utility methods to `TextUtility` class.
- Moved `Versions#getObjectiveAdapter` to `Players#getObjectiveAdapter`