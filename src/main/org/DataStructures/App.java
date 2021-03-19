package org.DataStructures;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;


/**
 * Hello world!
 *
 */
public class App 
{
  static int fileCount;
  private static SimpleTimer appTimer;
  static final boolean do20K = true;
  static final String[] specificFiles = {  };
  static int[] values = { 25, 50 , 500 , 1000 , 2000 , 5000, 10000};
  static int   MIN    = 0, MAX = 1000000;
  static DataType[] dataTypesToCreate = { DataType.DUPLICATE ,
        DataType.ASCENDING ,
        DataType.RANDOM ,
        DataType.REVERSE };
  
  public static void main( String[] args )
    {
      Sys.debugView = false;
      try{
        generateTestData();
        appTimer = SimpleTimer.simpleTimerFactory().start();
        GArrayList<DataSet> dataList = parse( getDataPath( "input" ) );
        GArrayList<DataSet> dataListCustom = parse( getDataPath( "custom" ) );
  
        dataList.add( dataListCustom.extract() );
        if( do20K ){
          GArrayList<DataSet> twentyK = parse( getDataPath( "20K" ) );
          dataList.add( twentyK.extract() );
        }
        //        GArrayList<DataSet> dataList =  parse(
        //              //              getDataPath("input") );
        //              //        GArrayList<DataSet> dataListCustom =  parse(
        //              getDataPath("Short") );
        appTimer.end();
  
        System.out.printf( "Loaded %d files in %d ns\n\n" ,
              dataList.getSize() ,
              appTimer.getResult() );
        System.err.println( "\tSort Start\n" );
        for( DataSet dataSet : dataList ){
          dataSet.test( QSParams.values() );
        }
        Reporter.closeAndStop();
  
        getViewerNoBlock();
      }catch( FileNotFoundException fnfE ){
        System.err.println( fnfE.getMessage() );
        fnfE.printStackTrace( );
      }catch(IOException e){
        e.printStackTrace();

        //two possible sources on IOException
        // - 1) reading the file in  dataset class (Constructor)
        // - 2) Writing CSV in the Reporter class (Reporter.report() which is
        // a constructor.
      } catch( Exception x ){
        System.err.println("Exception caught in App.main()");
        x.printStackTrace();
      } catch( Throwable t ){
        t.printStackTrace();
        //unknown cause here
      }finally{
        Reporter.closeAndStop();

        //irritating my catch block requires
        // a catch
      }
    }
  
  
  /**
   * Generate the test data live.  To effect new files to create, edit the
   * variables at the top of the app.  I did not elect to put in parameters
   * and such (on the CLI) as the reliability of someone who isnt me putting
   * values in the correct format is reletively low and of generally low yeild.
   *
   * Outputs the results to the file './res/custom/*'
   *
   *
   */
  private static void generateTestData() throws FileNotFoundException{
    System.out.println( "Generating files" );
    SimpleTimer generationTimer = SimpleTimer.simpleTimerFactory();
    generationTimer.start();
    int n = assignmentGenerator();
    generationTimer.end();
    System.out.printf( "Created %d files in %.2f ms (Milliseconds). \n    "
                       + "Files created at (%s)" ,
          n ,
          generationTimer.getResultMilliS(), FileGenerator.customPath
                     );
  }
  
  
  /**
   * From the homework:
   *
   * Create input files of <s>four</s><b>five</b> sizes: 50, 500, 1000, 2000 and
   * 5000  integers. For each size file make 3 versions. On the first use a randomly ordered
   * data set. On the second use the integers in reverse order. On the third
   * use the
   * integers in normal ascending order. (You may use a random number
   * generator to create the randomly ordered file, but it is important to
   * limit the duplicates to <1%. Alternatively, you may write a shuffle
   * function to randomize one of your ordered files.) This means you have an
   * input set of 15 files plus whatever you deem necessary and reasonable.
   * The size 50 files are to be turned in. The other sizes are only for
   * timing purposes. Your code needs to print out the sorted values. Files
   * are available at on the course web page if you want to copy them. Your
   * data should be formatted so that each number is on a separate line with
   * no leading blanks. There should be no blank lines in the file. Each sort
   * must be run against all the input files. With five sorts and 15 input
   * sets, you will have 75 required runs.
   * <br>
   * <br>
   * four sizes: 50, 500, 1000, 2000 and 5000
   * <br>
   * <br>
   * Looks at the boolean result of attempted file creation, and retunrs the
   * number of new files.  Makes no claim on the outcome of file creation,
   * either they may have failed, which would return false, or they already
   * exist.  FileDriver, and gaussFileDriver return true if and only if the
   * file is created and the file can be read.
   * <br>
   * <br>
   * There are two methods here, one using the Gaussian distribuition
   * sequential number generator, and the other using streams.,
   * <br>
   * <br>
   * The Gaussian file generator does not generate random values right now.
   * It could have been adapted but theres little point
   */

  public static int assignmentGenerator() throws FileNotFoundException{
    int newlyCreatedFiles = 0;

  
    for(int n: values){
      for(DataType dt : dataTypesToCreate ){
        boolean rslt = FileGenerator.fileDriver( n, MIN, MAX,
              ( dt == DataType.DUPLICATE ),
              dt );
        if(rslt) newlyCreatedFiles += 1;
        if(dt != DataType.RANDOM){
          boolean rslt2 = FileGenerator.gaussFileDriver( n, MIN, MAX,dt );
          if(rslt2) newlyCreatedFiles += 1;
        }
      }
    }
    return newlyCreatedFiles;
  }

  
  public static void test(){
    
    }
  
  /**
   * Superficially its pointless to pull this into a separate method, except
   * that several of the test methods require this path, and its essentially
   * final anyway.
   * @return
   */
  public static Path getDataPath(String subdir){
//      final String SUBDIR = "input";
      
      //format and build path.
      String outPath = Sys.joinPath(
            Sys.USER_DIR ,
            Sys.resourceRootPath ,
            subdir );

      //marked as soon to be deprecated in Java 12 or earlier.
      //      return Paths.get( outPath )
      return Path.of( outPath );
    }
  
  /**
   * Overloaded method
   * @return the Path
   */
  public static Path getDataPath(){
      return getDataPath( "input" );
    }
  
  
  /**
   * Load all the files in a directory, read each one and add the data to
   * an array of Integers.  Returns a wrapper for the array,
   * <p></p>
   * GArrayList really should have been the super for RecordSet -> DataSet,
   * but I wrote them separately and they sort of converged.  I wrote
   * RecordSet first to store the array of data and handle adding, removing,
   * resizing, subsetting, formatting, etc. an array of Integers.
   * <p></p>
   * DataSet extends RecordSet to add 2 important functionalities, the first
   * the File Reading and nameing methods.  RecordSet only handles the actual
   * data, DataSet Loads and applies that data given a fileName or path.
   * </p>
   * In addition, DataSet implements the{@link Iterable}interface and
   * the contains two sub-classes that provide the <Code>
   * {@link java.util.ListIterator} and the {@link Iterator<Integer>}
   * interfaces.
   *
   *
   * GArray List was written after, and is essentially the same thing, many
   * of the ideas in one were implemented in the other.  And in truth, by the
   * end, GArrayList could have completely replaced {@link RecordSet}.
   * RecordSet is an <b>Integer</b> implementation of {@link GArrayList}
   *
   * @param dir
   *       the dir
   *
   * @return the GArrayList - and arrayList wrapper of DataSet classes.
   *
   * @throws IOException
   *
   * @see public Interface Iterator<E>
   * @see java.util.Iterator
   * @see java.util.ListIterator
   *  @see java.util.ArrayList
   * @see RecordSet
   * @see GArrayList
   *
   */
  public static GArrayList<DataSet> parse( Path dir ){
      GArrayList<DataSet> dataArrayList =new GArrayList<>( DataSet.class);
      try( DirectoryStream<Path> dirStream = Files.newDirectoryStream( dir )){
        //          Iterator<Path> fileIterator = dirStream.iterator();
        for( Path path : dirStream ){
          
          //print file name.
          if(Sys.DEBUG_EN) System.out.println(  "Reading the file at :" + path.toString());
          
          
          File file = new File( path.toUri() );
          if( ! file.canRead() ){
            throw new IOException( String.format( "Unable to read file: %s" ,
                  file.getName().toString() ) );
          } else{
            //          RecordSet fileRecord = new RecordSet( file.getName() );
            DataSet dataSetRecord = new DataSet( path );
            dataArrayList.add( dataSetRecord );
            fileCount += 1;
            //        LineNumberReader lineNumberReader = new LineNumberReader(  Files.newBufferedReader( path ); )
            
            
          }
        }
      }catch( IOException e ){
        e.printStackTrace();
      }
      return dataArrayList;
    }
  
  /**
   * {@inheritDoc parse}
   * A quiet and minimal method to load the datasets for use in the testing
   * methods.  Trimmed version of parse
   *
   * You can use .extract on this to get the array of sets.
   *
   *
   *
   * @see public static GArrayList<DataSet> parse(Path dir)
   * @return GArrayList<DataSet>
   */
  public static GArrayList<DataSet> getDataSets_silent( ){
    Path dir = getDataPath();
    GArrayList<DataSet> dataArrayList =new GArrayList<>( DataSet.class);
    try( DirectoryStream<Path> dirStream = Files.newDirectoryStream( dir )){
      //          Iterator<Path> fileIterator = dirStream.iterator();
      for( Path path : dirStream ){
        
        File file = new File( path.toUri() );
        if( ! file.canRead() ){
          return null;
        } else{
          DataSet dataSetRecord = new DataSet( path );
          dataArrayList.add( dataSetRecord );
          fileCount += 1;
        }
      }
    }catch( IOException e ){
      e.printStackTrace();
    }
    return dataArrayList;
  }
  
  
  /**
   * Runs the 3rd party data viewer.  I found this helpful in exploring my data.
   *
   * Program blocks while the window is open!
   */
  static void getViewer(){
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
        System.err.println( "WARNING: Program blocks while the viewer is open"
                            + "." );
        String cmd = "res/out/csvfileview_64.exe \"res/out/results.csv\"";
        String absCmd = String.format( "%s  \"%s\"" , absPath , fileAbs );
        
        Process process = Runtime.getRuntime().exec( absCmd );
                process.waitFor();
      }catch( IOException ioE ){
        System.err.println(
              "IOException when trying to access 3rd party Viewer..." );
        ioE.printStackTrace();
      }catch( InterruptedException irE ){
        System.err.println( "External Process Terminated Prematurely" );
        irE.printStackTrace(  );
      }
    }
  }
  
  
  /**
   * Runs the 3rd party data viewer.  I found this helpful in exploring my data.
   *
   */
  static void getViewerNoBlock(){
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
      }catch( IOException ioE ){
        System.err.println(
              "IOException when trying to access 3rd party Viewer..." );
        ioE.printStackTrace();
      }
    }
  }
}
