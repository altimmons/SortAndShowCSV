package org.DataStructures;


import org.DataStructures.GArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGArrayList{
  
  
  @Test
  void testList(){
    GArrayList<Integer> integerGArrayList = new GArrayList<>( Integer.class );
    assertTrue( true );
    //no errors on instantiation.
  
    final int SEED = 999, MIN = 0, MAX = Integer.MAX_VALUE, COUNT = 10000;
    //    IntStream intStream = new Random( SEED ).ints( MIN , MAX );
    Random rng = new Random( SEED );
    for( int i = 0 ; i < COUNT ; i++ ){
      integerGArrayList.add( rng.nextInt() );
    }
    assertEquals( COUNT , integerGArrayList.getSize() );
  }
  
  
  @Test
  void testFiles(  TestReporter testReporter ){
    Path dir = App.getDataPath();
    GArrayList<Path> pathGArrayList = new GArrayList<>( Path.class );
    testReporter.publishEntry( String.format( "Path: %s, Abs: %s",
          dir.toString(), dir.toAbsolutePath() ) );
      try( DirectoryStream<Path> dirStream = Files.newDirectoryStream( dir )){
//        Iterator<Path> fileIterator = dirStream.iterator();
        
        for(Path path: dirStream ){
          pathGArrayList.add( path );
        }
        
        testReporter.publishEntry( pathGArrayList.toString() );
        StringBuilder sb = new StringBuilder();
        for(Path p : pathGArrayList){
          sb.append( p.toString() ).append( "\n" );
        }
        testReporter.publishEntry( sb.toString() );
    //    fileIterator.
      }catch(Throwable t ){
        //catch block
      }
  }
  
}
