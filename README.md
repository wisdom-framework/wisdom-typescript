# Wisdom-TypeScript

[TypeScript](http://www.typescriptlang.org/)  lets you write JavaScript the way you really want to. TypeScript is a 
typed superset of JavaScript that compiles to plain JavaScript. Any browser. Any host. Any OS. Open Source.

Wisdom-TypeScript integrates TypeScript compilation into Wisdom. So, TypeScript files (`.ts` files) automatically 
compiled to JavaScript at build time.

It supports the Wisdom Watch Mode, so any change to a `.ts` file immediately triggers the compilation and updates the
output file.

## Usage

Add the following plugin to you `pom.xml` file:

````
<plugin>
    <groupId>org.wisdom-framework</groupId>
    <artifactId>wisdom-typescript-maven-plugin</artifactId>
    <version>${project.version}</version>
    <executions>
        <execution>
            <id>compile-typescript-files</id>
            <phase>compile</phase>
            <goals>
                <goal>compile-typescript</goal>
            </goals>
        </execution>
    </executions>
</plugin>
````

By default, TypeScript 1.0.1 is used, but you can set the version to use as follows:

````
<plugin>
    <groupId>org.wisdom-framework</groupId>
    <artifactId>wisdom-typescript-maven-plugin</artifactId>
    <version>${project.version}</version>
    <executions>
            <execution>
                <id>compile-typescript-files</id>
                <phase>compile</phase>
                <goals>
                    <goal>compile-typescript</goal>
                </goals>
            </execution>
        </executions>
    <configuration>
        <version>1.0.1</version>
    </configuration>
</plugin>
````

## Examples

The  TypeScript files can be either in `src/main/resources/assets` (internal resources,
packaged within the application), or in `src/main/assets` (external resources, only packaged within the distribution).

For example, the file `src/main/resources/assets/Animal.ts` with the following content:

````
class Animal {
    constructor(public name: string) { }
    move(meters: number) {
        alert(this.name + " moved " + meters + "m.");
    }
}

class Snake extends Animal {
    constructor(name: string) { super(name); }
    move() {
        alert("Slithering...");
        super.move(5);
    }
}

class Horse extends Animal {
    constructor(name: string) { super(name); }
    move() {
        alert("Galloping...");
        super.move(45);
    }
}

var sam = new Snake("Sammy the Python");
var tom: Animal = new Horse("Tommy the Palomino");

sam.move();
tom.move(34);
````

generates the `target/classes/assets/Animal.js`, as well as the associated source map and declaration.

## Parameters

In addition to the `version` parameter seen above, the plugin supports:


* `removeComments` : When enabled, removes the comments from the generated JavaScript files. (Default: false)
* `declaration` : When enabled, generates corresponding +.d.ts+ files. (Default: false)
* `module` : Set the type of module generated among "commonjs" and "amd". (Default: commonsjs)
* `noImplicitAny` : When enabled, fail the compilation on expressions and declaration with an implied 'any' type. 
(Default: false)
* `sourcemap` : When enabled, generates the source map files (Default: true)     
    