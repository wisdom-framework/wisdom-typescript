/*
 * #%L
 * Wisdom-Framework
 * %%
 * Copyright (C) 2013 - 2014 Wisdom Framework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wisdom.typescript;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.Test;
import org.wisdom.maven.WatchingException;
import org.wisdom.maven.node.NodeManager;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks the TypeScript Mojo.
 */
public class TypeScriptMojoTest {

    public static final String FAKE_PROJECT = "target/test-classes/fake-project";
    public static final String FAKE_PROJECT_TARGET = "target/test-classes/fake-project/target";
    File nodeDirectory;
    private TypeScriptMojo mojo;


    @Before
    public void setUp() throws IOException {
        nodeDirectory = new File("target/test/node");
        nodeDirectory.mkdirs();
        Log log = new SystemStreamLog();
        NodeManager manager = new NodeManager(log, nodeDirectory, mojo);
        manager.installIfNotInstalled();
        mojo = new TypeScriptMojo();
        mojo.basedir = new File(FAKE_PROJECT);
        mojo.buildDirectory = new File(FAKE_PROJECT_TARGET);
        mojo.buildDirectory.mkdirs();
        mojo.version = "1.3";

        mojo.removeComments = false;
        mojo.declaration = true;
        mojo.module = "commonjs";
        mojo.sourcemap = true;
        mojo.noImplicitAny = false;
    }

    @Test
    public void testProcessingOfTypeScriptFiles() throws MojoFailureException, MojoExecutionException, IOException {
        cleanup();
        mojo.execute();

        File js = new File(FAKE_PROJECT_TARGET, "classes/Animal.js");
        File decl = new File(FAKE_PROJECT_TARGET, "classes/Animal.d.ts");
        File map = new File(FAKE_PROJECT_TARGET, "classes/Animal.js.map");
        assertThat(js).isFile();
        assertThat(decl).isFile();
        assertThat(map).isFile();


        js = new File(FAKE_PROJECT_TARGET, "wisdom/assets/ts/raytracer.js");
        decl = new File(FAKE_PROJECT_TARGET, "wisdom/assets/ts/raytracer.d.ts");
        map = new File(FAKE_PROJECT_TARGET, "wisdom/assets/ts/raytracer.js.map");
        assertThat(js).isFile();
        assertThat(decl).isFile();
        assertThat(map).isFile();
    }

    @Test
    public void testProcessingOfTypeScriptFilesWithDifferentConfiguration() throws MojoFailureException,
            MojoExecutionException, IOException {
        mojo.removeComments = false;
        mojo.declaration = false;
        mojo.module = "amd";
        mojo.sourcemap = false;
        mojo.noImplicitAny = false;
        cleanup();
        mojo.execute();

        File js = new File(FAKE_PROJECT_TARGET, "classes/Animal.js");
        File decl = new File(FAKE_PROJECT_TARGET, "classes/Animal.d.ts");
        File map = new File(FAKE_PROJECT_TARGET, "classes/Animal.js.map");
        assertThat(js).isFile();
        assertThat(decl).doesNotExist();
        assertThat(map).doesNotExist();


        js = new File(FAKE_PROJECT_TARGET, "wisdom/assets/ts/raytracer.js");
        decl = new File(FAKE_PROJECT_TARGET, "wisdom/assets/ts/raytracer.d.ts");
        map = new File(FAKE_PROJECT_TARGET, "wisdom/assets/ts/raytracer.js.map");
        assertThat(js).isFile();
        assertThat(decl).doesNotExist();
        assertThat(map).doesNotExist();
    }

    @Test
    public void testWatching() throws MojoFailureException, MojoExecutionException, IOException, WatchingException, InterruptedException {
        cleanup();

        // Copy animal to animal (do not modify var as it is used by other tests).
        final File originalAnimal = new File(FAKE_PROJECT, "src/main/resources/Animal.ts");
        final File newAnimal = new File(FAKE_PROJECT, "src/main/resources/Animal2.ts");
        final File originalRT = new File(FAKE_PROJECT, "src/main/assets/ts/raytracer.ts");
        String originalAnimalContent = FileUtils.readFileToString(originalAnimal);
        FileUtils.copyFile(originalAnimal, newAnimal);

        mojo.execute();

        final File anim = new File(FAKE_PROJECT_TARGET, "classes/Animal2.js");
        assertThat(anim).isFile();
        String content = FileUtils.readFileToString(anim);
        assertThat(content).contains("var sam = new Snake(\"Sammy the Python\");");

        final File rt = new File(FAKE_PROJECT_TARGET, "wisdom/assets/ts/raytracer.js");
        assertThat(rt).isFile();
        content = FileUtils.readFileToString(rt);
        assertThat(content).contains("new Plane(new Vector(0.0, 1.0, 0.0), 0.0, Surfaces.checkerboard),");

        // Delete new animal
        newAnimal.delete();
        mojo.fileDeleted(newAnimal);

        assertThat(anim.isFile()).isFalse();
        // Check that the .d.ts and the .js.map are deleted too
        assertThat(new File(FAKE_PROJECT_TARGET, "classes/Animal2.d.ts")).doesNotExist();
        assertThat(new File(FAKE_PROJECT_TARGET, "classes/Animal2.js.map")).doesNotExist();

        // Recreate the file with another name (same content)
        File newFile = new File(FAKE_PROJECT, "src/main/resources/Animal3.ts");
        FileUtils.write(newFile, originalAnimalContent);
        mojo.fileCreated(newFile);
        File var3 = new File(FAKE_PROJECT_TARGET, "classes/Animal3.js");
        assertThat(var3).isFile();
        content = FileUtils.readFileToString(var3);
        assertThat(content).contains("var sam = new Snake(\"Sammy the Python\");");

        // Update link
        long originalLastModified = rt.lastModified();
        FileUtils.touch(originalRT);
        mojo.fileUpdated(originalRT);
        // The file should have been updated
        assertThat(rt.lastModified()).isGreaterThanOrEqualTo(originalLastModified);
    }

    private void cleanup() {
        FileUtils.deleteQuietly(mojo.buildDirectory);
    }
}
