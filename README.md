# Wisdom-Myth

[Myth](http://www.myth.io/) is a CSS preprocessor that lets you write pure CSS without having to worry about slow
browser support, or even slow spec approval. It's like a CSS polyfill.

Wisdom-Myth integrate Myth pre-processing in Wisdom. So, stylesheets (CSS files) containing Myth directives are
automatically processed at build time.

It supports the Wisdom Watch Mode, so any change to a CSS file immediately triggers the processing and updates the
output file.

## Usage

Add the following plugin to you `pom.xml` file:

````
<plugin>
    <groupId>org.wisdom-framework</groupId>
    <artifactId>wisdom-myth-maven-plugin</artifactId>
    <version>${project.version}</version>
    <executions>
        <execution>
            <id>compile-myth-files</id>
            <phase>compile</phase>
            <goals>
                <goal>compile-myth</goal>
            </goals>
        </execution>
    </executions>
</plugin>
````

By default, Myth 0.3.4 is used, but you can set the version to use as follows:

````
<plugin>
    <groupId>org.wisdom-framework</groupId>
    <artifactId>wisdom-myth-maven-plugin</artifactId>
    <version>${project.version}</version>
    <executions>
            <execution>
                <id>compile-myth-files</id>
                <phase>compile</phase>
                <goals>
                    <goal>compile-myth</goal>
                </goals>
            </execution>
        </executions>
    <configuration>
        <version>0.3.4</version>
    </configuration>
</plugin>
````

## Examples

The process CSS files can be either in `src/main/resources/assets` (internal resources,
packaged within the application), or in `src/main/assets` (external resources, only packaged within the distribution).

For example, the file `src/main/resources/assets/style.css` with the following content:

````
:root {
    var-purple: #847AD1;
    var-large: 10px;
}

a {
    color: var(purple);
}

pre {
    padding: var(large);
}
````

generates the `target/classes/assets/style.css` with the following content:

````
a {
  color: #847AD1;
}

pre {
  padding: 10px;
}
````

While, the file `src/main/assets/style2.css` with the following content:

````
:root {
    var-purple: #847AD1;
}

a {
    color: var(purple);
}

a:hover {
    color: color(var(purple) tint(20%));
}

a {
    transition: color .2s;
}
````

generates the `target/wisdom/assets/style2.css` with the following content:

````
a {
  color: #847AD1;
}

a:hover {
  color: rgb(157, 149, 218);
}

a {
  -webkit-transition: color .2s;
  transition: color .2s;
}
````
