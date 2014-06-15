import java.io.*;

// Check the internal assets.
File style1 = new File( basedir, "target/classes/assets/Animal.js" );
File style2 = new File( basedir, "target/classes/assets/sub/Animal.js" );
if ( !style1.isFile() ) {
    throw new FileNotFoundException( "Could not find generated JS: " + style1 );
}
if ( !style2.isFile() ) {
    throw new FileNotFoundException( "Could not find generated JS: " + style2 );
}

// Check the external assets.
style1 = new File( basedir, "target/wisdom/assets/Animal.js" );
style2 = new File( basedir, "target/wisdom/assets/sub/Animal.js" );
if ( !style1.isFile() ) {
    throw new FileNotFoundException( "Could not find generated JS: " + style1 );
}
if ( !style2.isFile() ) {
    throw new FileNotFoundException( "Could not find generated JS: " + style2 );
}