package org.DataStructures;


import org.DataStructures.DataSet.Results;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The Reporter class is intended to output the results, generally in a method
 * where I can operate on in excel or the like.
 * <p></p>
 * As implemented, this is called from the {@link DataSet} <Code >dataset
 * .test() </Code> method
 * <p>
 *    * The class Hierarchy here is <bu>Reporter</bu>
 * <br>--{@link FileWriter}
 * <br>--{@link OutputStreamWriter}
 * <br>--{@link Writer}
 * <br>implementing:
 * <br> <i></i>  --{@link Closeable}
 * <br>   --{@link Appendable}
 * <br>   --{@link Flushable}
 *  </i>
 * </p>
 * Intended to be a singleton class.
 * <p></p>
 * but since I didnt want to write much, extended FIleWriter, which is an
 * instance method.
 * <p></p>
 * So there will be a static /instance interface like a factory.
 * <p></p>
 * I have added Comma separated files, and Tab separated files.  Later I could
 * implement a BAR separated fike '|'.  I used to like this format the most when
 * I used to work with data commonly.  A lot of datasets will have commas in
 * them already, which is truly a nightmare in a dataFile that is exported
 * without careful cleaning... lots of strings have commas in them, formatted
 * names and dates, some addresses, numbers in far away locales, really any time
 * people are allowed to enter data on their own.
 * <p></p>
 * Tabs can get hidden in whitespace less commonly, but even more less commonly,
 * they rarely use the bar.
 * <p>
 * <p>
 * Currently the following is reported.
 *
 * <p></p>
 * <br>- "Filename", = the file name  from dataset.getDescriptor
 *
 * <br>- "Records", = the size of the sort value from dataset.getSize(),
 *
 * <br>- "DataType", = from dataset.getDataType().name()
 *
 * <br>- "ParameterSet", = param.name() e.g. the name of the Parameter
 *
 * <br>- "Pivot Type", =from param.PivotType (Enum)
 *
 * <br>- "KStop",  =from param.getKStop()
 *
 * <br>- "InsertionSort", =values Yes and No for T/F
 *
 * <br>- "Recorded Time", =from results.time (SimpleTimer.getResults() long
 *
 * <br>- "Final Check Result", =the result of running the test that d[i]<=
 * d[i+]
 *
 * <br>- "Presorted sample [1:30]",  =results of the head() in RecordSet
 *
 * <br>- "Sorted sample [1:30]",  = a similar method, though requires a call to
 * static
 * generator method,
 *
 *
 * @see Closeable
 * @see Appendable
 * @see Flushable
 * @see DataSet
 * @see Results
 * @see FileWriter
 * @see OutputStreamWriter
 * @see Writer
 */
//@SuppressWarnings( "ALL" )
public final class Reporter
      extends FileWriter
      implements Closeable,
                 Appendable,
                 Flushable
{
  
  /**
   * initialize a new object pool to store results in.  Hoping to stream and
   * filter these later.
   */
  private static final GArrayList<Results> resultStore =
        new GArrayList<>( Results.class );
  
  private static final String   outPath;
  private static final String   COMMA     = ", "; //csv
  private static final String   TAB       = "\t ";  //tab separated file.
  private static final String   BAR       = "| ";  //tab separated file.
  private static       File     resultFile;
  private static       boolean  append;
  private static       Reporter instance;
  private static       boolean  NO_ERRORS = true;
  private static boolean written = false;
  
  //create the file (before the constructor runs)\

  /*
   * Generic "Consructor?"
   *
   * Try 3 times to create the file.
   * <br>
   * Display relevant user messages...
   * <br>
   * throws IllegalStateException upon 3rd failure (so that exception should
   * hopefully rise all the way back to App.main() and exit.
   * <br>
   * This has to be done like this because the constructor for this class
   * MUST call super() as the first class.  In addition, the call to super
   * must be one of the Constructors for the FileWriter class which this
   * extends.
   */
  static{
    outPath = Sys.joinPath( Sys.USER_DIR , "res" , "out" );
    File outDir = new File( Reporter.outPath );
    if( ! outDir.exists() ){
      try{
        Files.createDirectory( Path.of( Reporter.outPath ) );
      } catch( IOException e ){
        System.err.println( "Unable to create directory for output.  Fatal " + "error" );
        System.err.println( "If you are running inside an IDE, sometimes it " + "forces security provisions on you.  Create the " + "directory 'out' in the root directory and rerun" );
        Reporter.NO_ERRORS = false;
      }
    }
    Reporter.resultFile =
          new File( Sys.joinPath( Reporter.outPath , "results.csv" ) );
    if( ! Reporter.resultFile.exists() ){
      try{
        
        if( Reporter.resultFile.createNewFile() ) System.out.println(
              "The file did not exist, so it was created" );
        else{
          System.out.print( "The file already existed, " );
          if( Reporter.append ) System.out.println(
                " so it will be appended (NOT RECOMMENDED)" );
          else System.out.println( " it will be over written with the latest "
                                   + "results." );
        }
      } catch( IOException e ){
        System.err.println( "File for output does not exist, and was unable " + "to" + " be created." );
        try{
          Reporter.resultFile = File.createTempFile( "results" ,
                "csv" ,
                new File( Sys.USER_DIR ) );
        } catch( IOException eRetry ){
          System.err.println( "Second attempt failed... Trying somewhere " +
                              "else:" );
          try{
            Reporter.resultFile = File.createTempFile( "results" , "csv" );
          } catch( IOException eRetry2 ){
            System.err.println( "Third attempt failed... Aborting" );
            Reporter.NO_ERRORS = false;
          }
        }
        
      }
    }
    
    System.out.println( "REPORTER STARTED: The results will be saved in " + Reporter.resultFile );
  }
  
  /**
   * The primary COLUMN name storage.  This is not updatable at runtime as it
   * might cause alignment errors with testing, but it can be updated here.
   */
  private final String[] COLUMN_NAMES = { "Filename" ,
        //the file name  from dataset.getDescriptor
        "Records (Actual)" , "Labeled Size",
        //the size of the sort value from dataset.getSize(),
        "DataType" ,
        // from dataset.getDataType().name()
        "ParameterSet" , "Pivot Type" ,
        //from param.PivotType (Enum)
        "KStop" ,
        //from param.getKStop()
        "InsertionSort" ,
        //values Yes and No for T/F
        "Time(nS)" ,
        "Time(uS)",
        "Time(mS)",
        //from results.time (SimpleTimer.getResults() long
        "Final Check Result" ,
        "User Generated Data", //from dataset.usergen bool
        "GaussianSequential",  //from dataset bool var
        //the result of running the test that d[i]<=
        // d[i+]
        "Presorted sample [1:30]" ,
        //results of the head() in RecordSet
        "Sorted sample [1:30]" ,
        "Path", //from the file path.  DataSet Field filepath origin
        "Full Path", //as above from dataset.filePathOrigin.toAbsolutePath
        // a similar method, though requires a call
        // to static generator method, double check no commas are in here.
    
  };
  ;
  
  
  /**
   * Constructor
   * <br>
   * Note this is private.  Just start using the class (Reporter.report(Result))
   * and it will all be handled.
   *
   * @throws IOException
   *       inherited from a nested call.  Extends from the inability to create
   *       the file upon which this class will write out to.
   */
  private Reporter() throws IOException{
    super( Reporter.resultFile , Reporter.append );
    if( Reporter.instance == null ){
      Reporter.instance = this;
    }
    if(!written){ //this is writing first and last.  Cant figure out why?
      writeFirstLine();
      written = true;
    }
    
  }
  
  static void start() throws IOException{
    if( Reporter.instance == null ) new Reporter();
  }
  
  /**
   * Getter for property 'outFile'.
   *
   * @return Value for property 'outFile'.
   */
  public static File getResultFile(){
    return resultFile;
  }
  
  /**
   * Setter for property 'outFile'.
   *
   * @param resultFile
   *       Value to set for property 'outFile'.
   */
  public static void setResultFile( File resultFile ){
    Reporter.resultFile = resultFile;
  }
  
  /**
   * Store a test result in the static collection here for access later.
   * @param result
   */
  public static void store( Results result ){
    resultStore.add( result );
  }
  
  /**
   * Get the result store, no other methods are implemented here.  The rest
   * will have to rely on the GArrayList infrastructure.
   * @return -  the Results in an ArrayList format.  The
   * {@link GArrayList}<{@link Results}> format.
   */
  public static GArrayList<Results> getResultStore(){
    return resultStore;
  }
  
  /**
   * Getter for property 'append'.
   *
   * @return Value for property 'append'.
   */
  public static boolean isAppend(){
    return append;
  }
  
  /**
   * Setter for property 'append'.
   *
   * @param append
   *       Value to set for property 'append'.
   */
  public static void setAppend( boolean append ){
    Reporter.append = append;
  }
  
  /**
   * Main functional method here, Workflow is almost entirely here...
   * Reporter.report() will instantiate and produce the file.
   *
   * @param results
   *       the Results object {@link Results} that contains the copy of all the
   *       objects.
   *
   * @throws IOException
   *       - IOException propagates up the chain of calls if the file cannot be
   *       written to.
   */
  public static void report( Results results ) throws IOException{
    Reporter reporterInstance = getInstance();
    if( Reporter.NO_ERRORS ) reporterInstance.write( results ); //format
    // string and
    // send to super.
  }
  
  /**
   * Singleton Helper method.
   *
   * @return the single <i>instance </i> of the Reporter class.
   *
   * @throws IOException
   *       If Reporter is unable to open a file for writing.
   */
  static Reporter getInstance() throws IOException{
    if( Reporter.instance == null ) new Reporter();
    return instance;
  }
  
  
  /**
   * public static void closeAndStop()
   * <br>
   * The <code>close()</code> method for {@link FileWriter} since it would not
   * let me overload it, I had to change the name slightly.
   */
  public static void closeAndStop(){
    try{
      Reporter.getInstance().close();
      Reporter.setAppend( true ); //in case its reopened.
      Reporter.instance = null; //clear the reference.
    } catch( IOException ignored ){
      System.err.println( "Notify: non-critical error on attempting to " +
                          "close Reporter." );
    }
    
    
  }
  
  /**
   * Write the first line of the file.
   *
   * @throws IOException
   *       inherited from a nested call.  Extends from the inability to create
   *       the file upon which this class will write out to.
   */
  private void writeFirstLine() throws IOException{
    write( String.join( Reporter.COMMA , COLUMN_NAMES ) + "\n" );
  }
  
  /**
   * Wrapper for the particular {@link Results} object.
   * <p>
   * Responsible for taking the values and generating a comma Separated line.
   * <p></p>
   * <br>- "Filename", = the file name  from dataset.getDescriptor
   *
   * <br>- "Records", = the size of the sort value from dataset.getSize(),
   *
   * <br>- "DataType", = from dataset.getDataType().name()
   *
   * <br>- "ParameterSet", = param.name() e.g. the name of the Parameter
   *
   * <br>- "Pivot Type", =from param.PivotType (Enum)
   *
   * <br>- "KStop",  =from param.getKStop()
   *
   * <br>- "InsertionSort", =values Yes and No for T/F
   *
   * <br>- "Recorded Time", =from results.time (SimpleTimer.getResults() long
   *
   * <br>- "Final Check Result", =the result of running the test that d[i]<=
   * d[i+]
   *
   * <br>- "Presorted sample [1:30]",  =results of the head() in RecordSet
   *
   * <br>- "Sorted sample [1:30]",  = a similar method, though requires a call
   * to static
   * generator method,
   * <p></p>
   * todo: double check no (illicit) commas are in here.
   *
   * @param results
   *       The results object wrapper.
   *
   * @see DataSet
   * @see Results
   */
  void write( Results results ) throws IOException{
  
    String[] stringArray = {
          /*Filename*/
          results.file ,
          /*Records*/
          String.valueOf( results.dataSize ) ,
          /*Labeled Size*/
          String.valueOf( results.count ),
          /*DataType*/
          results.dataType.name() ,
          /*ParameterSet*/
          results.params.name() ,
          /*Pivot Type*/
          results.params.getPivotMethod().name() ,
          /*KStop*/
          String.valueOf( results.params.getkStopValue() ) ,
          /*InsertionSort*/
          ( ( results.params.doInsertSort() ) ? "Yes" : "No" ) ,
          /*Time*/
          String.valueOf( results.time ) ,
          /*Check*/
          results.time_micros ,
          results.time_millis ,
          String.valueOf( results.testTestResult ) ,
          /*User Generated Data*/
          String.valueOf( results.userGen ) ,
          String.valueOf( results.gauss ) ,
          /*PreSortView*/
          results.presortedHead ,
          /*SortView*/
          results.sortedHead ,
          /*File Path*/
          results.path ,
          /*Full Path*/
          results.absPath };
    String writable = String.join( Reporter.COMMA , stringArray );
    
    //sanity check, make sure our columns line up.
    //i have never used assert, so to make sure this works as expected, I
    // added the throwable.  In the meantime will check that assert works
    // like I expect- I think it throws an IllegalStateException...
    assert ( stringArray.length == COLUMN_NAMES.length );
    
    if( stringArray.length != COLUMN_NAMES.length )
      throw new IllegalArgumentException(
            "The column counts are not lining up between fields and values," + " " + "check for stray commas and make sure new fields werent" + " added." );
    
    assert ( writable.split( "(,)" ).length == COLUMN_NAMES.length );
    
    if( writable.split( "(,)" ).length != COLUMN_NAMES.length )
      throw new IllegalArgumentException(
            "The column counts are not lining up between fields and values," + " " + "check for stray commas and make sure new fields werent" + " added." );
    if( Reporter.NO_ERRORS ){
      write( writable );
      write( System.lineSeparator() );
    }
  }
  
  
  /**
   * The over-ridden method.  Un-necessary. but for completion sake.
   */
  @Override
  public void write(String str) throws IOException {
    super.write( str );
  }
}
