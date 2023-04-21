Babel [![](https://jitpack.io/v/me.googas/reflect.svg)](https://jitpack.io/#me.googas/chat)
===

User Interface utility for Bukkit

## Documentation & Installation

* JitPack [Repository](https://jitpack.io/#me.googas/reflect)
* JitPack [JavaDoc](https://jitpack.io/com/github/Chevyself/reflect/master-SNAPSHOT/javadoc/)
* [Wiki](https://github.com/Chevyself/chat/wiki)

## Structure

* **adapter** Allows the utility to work in different Minecraft versions.
* **adapter-{version number}** Contains the adapters for the specific Minecraft version.
* **api** Main module of the utility.
* **sample** Deprecated. This contains sample code for a plugin, will be replaced with an actual plugin.

> Please note that either `reflect`, `adapter` or any `adapter-{verssion}` should be accessed as if those were an API. These modules access NMS using reflection and are in constant change. The `api` module is the only one that is stable.

