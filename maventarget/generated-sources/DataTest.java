package org.DataStructures;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataTest{
  
  final String COMMA = ", ";
  
  @Test
  public GArrayList<Path> getTestString( TestReporter testReporter ){
    
    
    StringBuilder stringBuilder = new StringBuilder();
    //dummy var to make this a test, and at least test if we didnt have an
    // error.
    boolean b = true;
    
    Path          dir           = App.getDataPath();
    
//    Store these paths
    GArrayList<Path> pathList = new GArrayList < > ( Path.class );
    
    
    try( DirectoryStream<Path> dirStream = Files.newDirectoryStream( dir ) ){
      //          Iterator<Path> fileIterator = dirStream.iterator();
      for( Path path : dirStream ){
        File tempFile = new File( path.toUri() );
        stringBuilder.append( tempFile.getName() ).append( COMMA );
      }
  
    } catch( IOException ioE ){
      b = false;
    }
    testReporter.publishEntry( stringBuilder.toString() );
    assertTrue(b);
    return pathList;
  }
  
  @Test
  public void countTest(TestReporter testReporter){
    GArrayList<Path> paths = getTestString( testReporter );
    for (Path p : paths){
      testReporter.publishEntry( p.toString() );
  
      DataSet dataSet = null;
      try{
        dataSet = new DataSet( p.toAbsolutePath() );
      } catch( FileNotFoundException e ){
        e.printStackTrace();
      }
      assertEquals(  dataSet.getSize(), dataSet.getCount() );
     testReporter.publishEntry( String.format( "The file %s is (based on file"
                                               + " name) %s and contains %d "
                                               + "recordSet, after reading, %d "
                                               + "values are read.",
           dataSet.getDescriptor(), dataSet.getDataType(), dataSet.getCount(),
           dataSet.getSize() ) );
    }
  }
}
