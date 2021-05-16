# Embers: Hardcore Alchemy Edition

This is a version of Embers for 1.10 with the aim of maintenance/cross-mod compatibility. It's a backport of a late 1.11 version of Embers. Upgrading from the official 1.10 version of Embers will cause your world to be irreversibly upgraded. If you were just using the Embers mod standalone or as part of your own homebrew kitchen sink pack, then you'll probably be fine, but you should make a backup just in case.

## Building

Please note that build instructions are subject to change.

- If using Eclipse, best practice is to choose some folder as your workspace folder, and do `git clone [this repository]` from within that folder
- Add the non-dev versions of these mods to the `libs/` folder (create it if it does not exist)
    - CodeChickenCore 
    - CodeChickenLib
    - Thaumcraft
    - Baubles
- Run `./gradlew setupDecompWorkspace eclipse` (or the IntelliJ equivalent)
    - Repeat this step as needed when `libs/` changes, gradle stuff changes, or your dev environment mysteriously crashes due to missing method/class errors
- Make changes
- Run `./gradlew build` to build the mod into the `build/libs/` folder
