Changelog
===

# 15th April 2023

- Improved Javadoc for:
  - [X] Utility classes
  - [X] Placeholder API
  - [X] Soft dependencies classes
- Added Javadoc for
  - [X] Custom tab API
  - [X] Packets adapter
- [X] Renamed `Line` to `Text`
- [X] Renamed `ErrorHandler` to `Debugger`
- [X] Improvements to `Text`
  - [X] Instead of having `Text#build` with different parameters for Text with placeholders and samples. Now the class has the methods `#isSample`, `#hasPlaceholders` to check and `#setSample` and `#setHasPlaceholders` to set the values.
  - [X] Moved static utility methods to `TextUtility` class.
- [X] Simplify boss-bars, instead of having two methods as `Channel#getBossBar` and `Channel#giveBossBar` now there is only one method `Channel#getBossBar` which returns one that can be displayed.
  - [X] Boss bars must be displayed using `AdaptedBossBar#display`
- [X] Add support to `Packet`s which changed in Minecraft 1.17
- [X] Better `Reflect` debugging
- Moved `Versions#getObjectiveAdapter` to `Players#getObjectiveAdapter`