package org.DataStructures;
//


import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.TransferQueue;

import static org.DataStructures.App.getDataPath;
import static org.DataStructures.App.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//
///**
// * Unit test for simple App.
// */
public class AppTest{
  
  
  @Test
  @Disabled
  public void testSort( TestReporter testReporter ){
//    String filename = "res/short/ran20.dat"; //fixed.. error in length max
//    String filename = "res/short/ran20.dat"; //fixed.. error in length max
    String filename = "res\\custom\\asc50-u.dat"; //fixed.. error in length max
    
    Path f = Path.of( filename );
    DataSet data = null;
    try{
      data = new DataSet( f );
    } catch( FileNotFoundException e ){
      e.printStackTrace();
    }
    //    QSParams[] paramToUse = QSParams.values();
    QSParams[] paramToUse = {
          QSParams.QS_MID_K_1_NO_IS
    };
    
    for(QSParams p : paramToUse){
  
      QSort qSort = new QSort( data, p );
      Integer[] result = qSort.sort();
      testReporter.publishEntry( Sys.formatSet( result ) );
      if(p.doInsertSort()){
        Integer[] result2 = InsertionSort.sort( result );
        testReporter.publishEntry( Sys.formatSet( result2 ) );
      }
    }
    
  }
  
  
  @Test
  String propLoad() throws IOException{
  String result = "";
    Properties properties = new Properties();
    String propFileName = "config.properties";
    InputStream inputStream =
          getClass().getClassLoader().getResourceAsStream( propFileName );
    if( inputStream != null ){
      properties.load(inputStream);
    }else {
      throw new FileNotFoundException( String.format( "Property File '%s' not"
                                                      + " " + "found in the " + "classpath", propFileName ) );
    }
    return "";
  }
  
  
  @Test
  void writeProp(TestReporter testReporter){
    File propertiesFile = new File("./res/resources/system.properties");
    try{
    
      FileOutputStream fileOutputStream =
            new FileOutputStream( propertiesFile );
    
      Properties properties = new Properties( System.getProperties() );
    
      Properties prop = new Properties();
      properties.store( fileOutputStream , "System Properties" );
      
          } catch( IOException ioE ){
      
    }
  }
  
  
  @Test
  /**
   * The <b>Path</b>.equals(<b>Path</b>) - returns false, though on visual
   * inspection, they are actually equal.  Perhaps because theses are two
   * instances, that point to the same place, but are regardless different
   * objects.
   */
  public void testPath( TestReporter testReporter ){
    String parameter = "res/input";
  Path p = Path.of( parameter );
  testReporter.publishEntry(
String.format(
        "\n[%b]-Path passed parameter <%s>.  The returned path is <%s>.  The "
        + "absolute path is <%s>.\n The [App::getPathData()] method returned "
        + "the path value <%s>.  The \n absolute path is <%s>.\n ",
          p.equals( getDataPath()),
          parameter,
          p,
          p.toAbsolutePath(),
          getDataPath(),
          getDataPath().toAbsolutePath()
                )
             );
  assertTrue( getDataPath()
                 .toAbsolutePath()
                 .toString()
                 .equals(
                       p.toAbsolutePath()
                       .toString()
                        )
            );
    //    assertTrue( p.equals( App.getDataPath() ) );
//    testReporter.publishEntry( p.toAbsolutePath().toString() );

}


@Test
@DisplayName( "Testing External Script Execution" )
@Timeout( 5 )
void testCmd(){
  if(Sys.IS_WIN){
    //          "Powershell './res/view.ps1'"
    try{
  
      String cmd = "./res/out/csvfileview_64.exe './res/out/results.csv'";
      Process process = Runtime.getRuntime().exec( cmd );
//      process.waitFor();
    }catch( IOException ioE ){
      System.err.println(
            "IOException when trying to access 3rd party Viewer..." );
      ioE.printStackTrace();
//    }catch( InterruptedException irE ){
//      System.err.println( "External Process Terminated Prematurely" );
//      irE.printStackTrace(  );
    }
  }
}

@Disabled
@Test
@DisplayName( "Testing External Script Execution" )
@Timeout( 5 )
//does not work...
void testPSHCmd(){
  if(Sys.IS_WIN){
    //          "Powershell './res/view.ps1'"
    try{
  
      String cmd = "powershell '.\\res\\view.ps1'";
      Process process = Runtime.getRuntime().exec( cmd );
//      process.waitFor();
    }catch( IOException ioE ){
      System.err.println(
            "IOException when trying to access 3rd party Viewer..." );
      ioE.printStackTrace();
//    }catch( InterruptedException irE ){
//      System.err.println( "External Process Terminated Prematurely" );
//      irE.printStackTrace(  );
    }
  }
}

@Disabled
//doesnt work
  @Test
  @DisplayName( "Testing External Batch Script Execution" )
@Timeout( 5 )
  void testBatchCmd(){
    if(Sys.IS_WIN){
      //          "Powershell './res/view.ps1'"
      try{
      
//        String cmd = "cmd '.\\res\\viewer.cmd'";
        String cmd = "cmd ./res/viewer.cmd";
        Process process = Runtime.getRuntime().exec( cmd );
//        process.waitFor();
      }catch( IOException ioE ){
        System.err.println(
              "IOException when trying to access 3rd party Viewer..." );
        ioE.printStackTrace();
//      }catch( InterruptedException irE ){
//        System.err.println( "External Process Terminated Prematurely" );
//        irE.printStackTrace(  );
      }
    }
  }
  
  
  @Test
  @DisplayName( "Testing Direct Call of Program" )
  @Timeout( 5 )
  @Disabled  //not good for automated testing.
  void testDirectCall(){
    if(Sys.IS_WIN){
      try{
        String cmd = "res/out/csvfileview_64.exe \"res/out/results.csv\"";
        
        Process process = Runtime.getRuntime().exec( cmd );
//        process.waitFor();
      }catch( IOException ioE ){
        System.err.println(
              "IOException when trying to access 3rd party Viewer..." );
        ioE.printStackTrace();
//      }catch( InterruptedException irE ){
//        System.err.println( "External Process Terminated Prematurely" );
//        irE.printStackTrace(  );
      }
    }
  }
  
  
    @Test
    @DisplayName( "Testing Direct Call of Program With absolute Paths" )
    @Timeout( 5 )
    @Disabled
    void testDirectCallAbsolute(){
    if(Sys.IS_WIN){
      try{
        File file = new File( "res/out/csvfileview_64.exe" );
        if(!file.exists() || !file.canExecute() ){
          file= new File("out/results.csv");
          if(!file.exists()  || !file.canExecute() ){
            System.err.println( "Unable to fine file" );
            return;
          }
        }
        Path absPath = Path.of(file.getAbsolutePath());
        Path parent = absPath.getParent();
        String fileAbs = Path.of( parent.toAbsolutePath().toString() ,
              "results.csv" ).toAbsolutePath().toString();
        
        String cmd = "res/out/csvfileview_64.exe \"res/out/results.csv\"";
        String absCmd = String.format( "%s  \"%s\"" , absPath , fileAbs );
        
        Process process = Runtime.getRuntime().exec( absCmd );
//        process.waitFor();
      }catch( IOException ioE ){
        System.err.println(
              "IOException when trying to access 3rd party Viewer..." );
        ioE.printStackTrace();
//      }catch( InterruptedException irE ){
//        System.err.println( "External Process Terminated Prematurely" );
//        irE.printStackTrace(  );
      }
    }
  }
  
  
  
  
  
  
  private static void oldApp(){
    SimpleTimer appTimer = SimpleTimer.simpleTimerFactory().start();
    
    GArrayList<DataSet> dataList =  parse( getDataPath("input") );
    
    //store these maybe ill want..
    appTimer.end();
    long readTime = appTimer.getResult();
    String parseResult = appTimer.toString();
    
    //reset timer..
    appTimer = SimpleTimer.simpleTimerFactory();
    
    for( DataSet set : dataList){
      QSort qSort = new QSort( set );
      qSort.sort();
    }
    iteratorTimer( dataList );
  }
  
  
  public static void testSort(int n){
    
    String filename;
    switch(n){
      case 1: filename = "res/custom/ran20.dat"; break;
      default: filename = "res/custom/ran200.dat";
      
    }
    Path    p        = Path.of( filename );
    DataSet data     = null;
    try{
      data = new DataSet( p );
    } catch( FileNotFoundException e ){
      e.printStackTrace();
    }
  
    InsertionSort.sort( data.extract() );
    //    QSort   qSort    = new QSort( data );
    //    qSort.sort();
  }
  
  private static void iteratorTimer( GArrayList<DataSet> dataList ){
    ArrayList<Object> arrayList ;
    //      QuickSort.it_partition( dataList.get( 1 ) );
    SimpleTimer t1 = SimpleTimer.simpleTimerFactory();
    SimpleTimer t2 = SimpleTimer.simpleTimerFactory();
    
    for( DataSet d : dataList){
      t1.start();
      for (int i=0, n=d.getSize(); i < n; i++  ){
        d.getValue( i );
      }
      t1.end();
      
      t2.start();
      for( Iterator i = d.iterator() ; i.hasNext();){
        i.next();
      }
      t2.end();
      
      long tmr1 = t1.getResult();
      long tmr2 = t2.getResult();
      boolean blnTimer = (tmr2<tmr1);
      
      float mult =(blnTimer) ?  (tmr1 / tmr2) : (tmr2/tmr1);
      String s1 = ( blnTimer ) ?
                  "iterator  method is faster than the fori - counter "
                  + "method" :
                  "fori- counter method  method is faster than the " +
                  "iterator ";
      System.out.println( String.format(
            "(%B)- the %s - the iterator took %d, while the fori method "
            + "took "
            + "%d,"
            + " a difference of %.2f x"
            + ".", blnTimer, s1,  tmr2, tmr1, mult) );
      
      System.out.println( "complete" );
    }
  }
  
}