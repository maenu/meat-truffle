# meat-truffle

The meat language running on Truffle.

## Installation (Eclipse)

Similar to [graalvm/simplelanguage](https://github.com/graalvm/simplelanguage#installation).
Clone this repo and move GraalVM to a directory called `graalvm` alongside the `language` directory.
Setup Eclipse (Oxygen) workspace at the repo root with a new JRE pointing to the GraalVM with default arguments: `-XX:-UseJVMCIClassLoader -Xbootclasspath/a:../graalvm/jre/lib/truffle/truffle-api.jar:../graalvm/jre/lib/truffle/locator.jar:../graalvm/jre/lib/truffle/truffle-nfi.jar:../graalvm/Contents/Home/jre/lib/truffle/truffle-api.jar:../graalvm/Contents/Home/jre/lib/truffle/locator.jar:../graalvm/Contents/Home/jre/lib/truffle/truffle-nfi.jar`.
Set this JRE as default for Java 1.8.
Import existing Maven project in Eclipse.
Run `meat.Main`.

## TODO

- support dictionary
- context is merely a dictionary
- context lookup can be simplified
- message sends should be handled by Truffle, not meat itself
- partial compilation for default implementations
- primitives
