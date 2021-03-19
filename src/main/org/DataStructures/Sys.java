package org.DataStructures;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;


/**
 * This class contains settings (in the type of compile-time settings, rather
 * than run-time.  As such, they are mostly final, and are generally flags.
 * <p>
 * Otherwise there are some helper classes and some other types of object.
 */
/*abstract*/  /*public final */public class Sys{
  public static final boolean SHOW_VALUE = true;
  public static       boolean debugView  =true ;
  //determines the format of
  // the TokenString - H( Len=6 )>-D~F~B~C~A~N~-<T
  final static        String  LINE_SEP   = System.lineSeparator();
  final static         String  FILE_SEP             =
        System.getProperty( "file.separator" );
  final static         String  CLASSPATH            =
        System.getProperty( "java.class.path" );
  final static         String  JAVA_HOME            =
        System.getProperty( "java.home" );
  //  final static String USER_DIR = "O:\\Logs";
  final static         String  USER_DIR             =
        System.getProperty( "user.dir" );
  final static         String  USER_HOME            =
        System.getProperty( "user.home" );
  final static String OS_NAME = System.getProperty( "os.name" );
  final static boolean IS_WIN =
        System.getProperty( "os.name" ).toLowerCase().startsWith( "win" );
  final static         String  resourceRootPath     = "res\\";
  final static         int     STACK_TRACE_LEN      = 3;
  final static         boolean useSystemLookAndFeel = false;
  final static         boolean LOG_TO_FILE          = true;
  private static final Logger  LOG                  = Logger.getLogger( "Sys" );
  public static        boolean verbose;
  static               boolean LOG_EN    = true;
  public static        boolean DEBUG_EN  = true;
  private static final String  VALUE_SEP = " ";
  private static final String NULL_REPRESENTATION = "<null>";
  
  public static void propertyHandler() throws IOException{
    Properties defaultProperties = getProperties();
    // create application properties with default
    Properties applicationProperties =
          applicationProperties( defaultProperties );
    
    Map<String, String> env = getEnvVars();
  }
  
  
  static Map<String, String> getEnvVars(){
    Map<String, String> env = System.getenv();
    return env;
  }
  
  static Properties getProperties() throws IOException{
    // create and load default properties
    Properties      defaultProperties = new Properties();
    FileInputStream in                =
          new FileInputStream( "defaultProperties" );
    defaultProperties.load( in );
    in.close();
    return defaultProperties;
  }
  
  static Properties applicationProperties( Properties defaultProps )
        throws IOException
  {
    FileInputStream in;
    Properties      applicationProps = new Properties( defaultProps );
    
    // now load properties
    // from last invocation
    in = new FileInputStream( "appProperties" );
    applicationProps.load( in );
    in.close();
    return applicationProps;
  }
  
  /**
   * A handler for printing stack traces in the Log, which doesn't like pure
   * stack traces, just string elements.  I am sure there is an easier way.
   *
   * @param ste
   *       - the stack trace element.
   *
   * @return A formatted string of the last [STACK_TRACE_LEN] stack trace
   * elements.
   */
  public static String stackTraceConv( StackTraceElement[] ste ){
    StringBuilder sb = new StringBuilder();
    for( int i = 0 ; i < STACK_TRACE_LEN ; i++ ){
      if( ste[ i ] != null ) sb
            .append( i )
            .append( ".) " )
            .append( ste[ i ] )
            .append( "\n" );
    }
    return sb.toString();
  }
  
  /**
   * A handler for printing stack traces in the Log, which doesn't like pure
   * stack traces, just string elements.  I am sure there is an easier way.
   *
   * @param e
   *       - the Exception which occurs.
   *
   * @return A formatted string of the last [STACK_TRACE_LEN] stack trace
   * elements.
   */
  public static String exceptionConv( Exception e ){
    Sys.LOG.fine( "exceptionConv Method called." );
    StringBuilder sb = new StringBuilder( "EXCEPTION!: " );
    sb.append( e ).append( "\n" );
    StackTraceElement[] ste = e.getStackTrace();
    for( int i = 0 ; i < STACK_TRACE_LEN ; i++ ){
      if( ste[ i ] != null ) sb
            .append( i )
            .append( ".) " )
            .append( ste[ i ] )
            .append( "\n" );
    }
    return sb.toString();
  }
  
  /**
   * A handler for printing stack traces in the Log, which doesn't like pure
   * stack traces, just string elements.  I am sure there is an easier way.
   *
   * @param t
   *       - the Exception which occurs.
   *
   * @return A formatted string of the last [STACK_TRACE_LEN] stack trace
   * elements.
   */
  public static String throwableCon( Throwable t ){
    StringBuilder sb = new StringBuilder( "EXCEPTION!: " );
    sb.append( t ).append( "\n" );
    StackTraceElement[] ste = t.getStackTrace();
    for( int i = 0 ; i < STACK_TRACE_LEN ; i++ ){
      if( ste[ i ] != null ) sb
            .append( i )
            .append( ".) " )
            .append( ste[ i ] )
            .append( "\n" );
    }
    return sb.toString();
  }
  
  /**
   * just a mneumonic for me mostly.  Generic method example and a scale
   * example.
   * @param n the value
   * @param <N> a class which is in the number hierarchy. A number which will
   *          return the log10(n) basically.
   * @return the count, basically the log10(value) but whole.
   */
  public static <N extends Number> int scaleNumber( N n ){
    int i = (int)n,  ct= 0;
    while((i /= 10) > 0)ct++;
    return ct;
  }
  

  
  /**
   * Converts stack traces to a usable string.  Inspiration found here:
   * <p>
   * link: https://howtodoinjava.com/java/string/convert-stacktrace-to-string/
   */
  public static String convertThrowableStackTraceToString( Throwable throwable ){
    try( StringWriter stringWriter = new StringWriter() ;
         PrintWriter printWriter = new PrintWriter( stringWriter ) ){
      throwable.printStackTrace( printWriter );
      return stringWriter.toString();
      
    } catch( IOException iOE ){
      if( Sys.DEBUG_EN ){
        System.err.println( "IO Exception in " + "convertStackTraceTo String" );
        iOE.printStackTrace();
      }
      if( Sys.LOG_EN ){
        Sys.LOG.warning( "IO Exception error" );
        //	      throw new Exception("IO Exception");
        throw new IllegalStateException( iOE );
      }
    }
    return null;
  }
  
  /**
   * Method for formatting a Log Record.
   * <p>
   * Source : https://github.com/jenkinsci/jenkins/blob/master/core/src/main
   * /java/hudson/Functions.java#L576
   */
  public static String[] logPreformatError( LogRecord record ){
    String source;
    if( record.getSourceClassName() == null ){
      //source missing, replace with logger name...
      source = record.getLoggerName();
    } else{
      if( record.getSourceMethodName() == null ){
        source = record.getSourceClassName();
      } else{
        source = String.format( "%s %s" ,
              record.getSourceClassName() ,
              record.getSourceMethodName() );
      }
    }
    String message =
          new SimpleFormatter().formatMessage( record ) + Sys.LINE_SEP;
    Throwable throwable = record.getThrown();
    
    return new String[]{ String.format(
          "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp" ,
          new Date( record.getMillis() ) ) ,
          source ,
          record.getLevel().getLocalizedName() ,
          throwable == null ?
          message :
          message + Sys.convertStackTraceToString( throwable ) + "\n" };
  }
  
  
  /**
   * Rather unneccessary, but delightfully interesting (and efficient) way to
   * iterate the number at the end of a file name.  At least I expect it to be
   * efficient since it utilizes a single character buffer all the way through.
   *
   * @param fileName
   *
   * @return
   */
  static String fileNameIterator( String fileName ){
    Pattern  COMPILE = Pattern.compile( "(\\.)" );
    String[] fn      = COMPILE.split( fileName );
    //48-57
    //fn[0].charAt( (fn[0].length()-1) )
    //		System.out.println( fn.length );
    String        temp          = fn[ 0 ];
    StringBuilder stringBuilder = new StringBuilder( temp );
    temp = stringBuilder.reverse().toString();
    stringBuilder.setLength( 0 );
    while( Character.isDigit( temp.charAt( 0 ) ) ){
      stringBuilder.append( temp.charAt( 0 ) );
      temp = temp.substring( 1 ); //cut off one digit from the front
    }
    if( stringBuilder.length() >= 1 ){
      stringBuilder
            .reverse()
            .replace( 0 ,
                  stringBuilder.length() ,
                  String.valueOf( Integer.parseInt( stringBuilder.toString() ) + 1 ) )
            .reverse() //reverse it backwards again since filename is still
            // rev.
            .append( temp )  //append the nonnumerial part
            .reverse()
            .append( "." ) //add the extension
            .append( fn[ 1 ] ); //reappend the filename
    } else{
      stringBuilder
            .append( fn[ 0 ] )
            .append( 001 )
            .append( "." )
            .append( fn[ 1 ] );
    }
    return stringBuilder.toString();
  }
  
  /**
   * Generates a string the length of the argument of Random Characters from a
   * set of given characters.  Returns the String.
   * <p>
   * The string in the method from which characters are chosen can be edited to
   * add characters and other special symbols.
   *
   * @param length
   *       -  the number of Random characters to return.
   *
   * @return
   */
  static String randChars( int length ){
    int c = length;
    if( c < 0 ) return Sys.randChars( Math.abs( c ) );
      //err handle or pass
      //      return null;
      //      throw new IllegalArgumentException();
    else if( length == 0 ) return "";
    else{
      //Add additional chars into the string below if needed.
      final String allChars = "0123456789" // numerals
                              + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"  //uppercase
                              + "abcdefghijklmnopqrstuvwxyz";  //lowercase
      //              + "!@#$%^&*()"          //Regular Symbols
      //               + ",.\\/<>[]{};:'\"`~=-_+ ";  //all other symbols
      
      char[]        chArr = allChars.toCharArray();
      double        x1    = 0.0, x2 = allChars.length();
      StringBuilder sb    = new StringBuilder();
      while( c > 0 ){
        double f = Math.random() / Math.nextDown( 1.0 );
        double x = x1 * ( 1.0 - f ) + x2 * f;
        int    i = (int)Math.floor( x );
        sb.append( chArr[ i ] );
        //or in a single harder to read line:
        //sb.append( allChars.toCharArray()[(int)Math.floor(x1 * ( 1.0 - f )
        // + x2 * f)]);
        c--;
      }
      return sb.toString();
    }
  }
  
  
  /**
   * Converts stack traces to a usable string.  Inspiration found here:
   * <p>
   * link: https://howtodoinjava.com/java/string/convert-stacktrace-to-string/
   */
  public static String convertStackTraceToString( Throwable throwable ){
    try( StringWriter stringWriter = new StringWriter() ;
         PrintWriter printWriter = new PrintWriter( stringWriter ) ){
      throwable.printStackTrace( printWriter );
      return stringWriter.toString();
      
    } catch( IOException iOE ){
      if( DEBUG_EN ){
        System.err.println( "IO Exception in " + "convertStackTraceTo String" );
        iOE.printStackTrace();
      }
      if( LOG_EN ){
        Sys.LOG.warning( "IO Exception error" );
        //	      throw new Exception("IO Exception");
        throw new IllegalStateException( iOE );
      }
    }
    return null;
  }
  
  /**
   * Unwrap an array into a single String
   */
  static String unwrapArray( String[] stringArray , String separator ){
    separator = ( separator == null ) ? "" : separator;
    StringBuilder outString = new StringBuilder();
    if( stringArray != null && stringArray.length > 0 ){
      for( String s : stringArray ){
        //add the separator, but only after the first one and if theres another.
        if( outString.length() > 0 ) outString.append( separator );
        
        outString.append( s );
      }
    }
    return outString.toString();
  }
  
  /**
   * Overloaded single param method with a new line as the default
   */
  static String unwrapArray( String[] stringArray ){
    return unwrapArray( stringArray , LINE_SEP );
  }
  
  
  /**
   * Convienence method to join paths.
   */
  public static String joinPath( String prefixPathString ,
                                 String suffixPathString ,
                                 String... additionalPaths )
  {
    //set these up does our path start with a fileSeparator.
    boolean startsWithSep =
          suffixPathString.substring( 0 , 1 ).equals( File.separator );
    boolean startsWithSepAlt =
          ( suffixPathString.charAt( 0 ) == File.separatorChar );
    
    
    //check to see if a file object given this path is resolving to something
    // from the root file tree or relative file tree.  if from the relative
    // file tree, it should contain the User.Dir.  Unless of course you are
    // like me and make liberal usage of the parent file link `\..\` like this
    // POM statement I used on this project '..\..\..\..\ASUSsync\VS
    // Code\md\dsRes'
    
    String outstring;
    //set up, does UserDirectory end in '\'
    boolean endsWithSep = prefixPathString
          .substring( prefixPathString.length() - 1 )
          .equals( File.separator );
    
    //      Processor.LOG.info( "Path stub found., trying to append User.Dir" );
    
    if( endsWithSep && startsWithSep ){
      //both
      outstring = prefixPathString + suffixPathString.substring( 1 );
    } else if( endsWithSep || startsWithSep ){
      //one of them.
      outstring = prefixPathString + suffixPathString;
      
      //inspection says "ends with Sep" is always true, and the whole
      // statement below is always true but on testing its not.  Might be
      // missing something.  Check here with unexpected behavior.
    } else if( ( ! endsWithSep ) && ( ! startsWithSep ) ){
      //neither
      outstring = prefixPathString + File.separator + suffixPathString;
    } else{
      outstring = suffixPathString;
    }
    int addlPathsLen = additionalPaths.length;
    
    if( addlPathsLen > 0 ){
      addlPathsLen--;
      if( addlPathsLen > 0 ){
        String[] remainingPaths = new String[ addlPathsLen ];
        System.arraycopy( additionalPaths ,
              1 ,
              remainingPaths ,
              0 ,
              addlPathsLen
                        );
        return joinPath( outstring , additionalPaths[ 0 ] , remainingPaths );
      } else{ //length now equals zero (did equal 1)
        return joinPath( outstring , additionalPaths[ 0 ] );
      }
    } else return outstring;
  }
  
  
  /**
   * I was experimenting here with making my error logging simpler, as its
   * basically the same thing every time.  Generally my exceptions are errors in
   * my coding, not runtime errors. This was the result..
   */
  public static void throwableLogger( Throwable t , Class<?> thisClass ){
    
    try( PrintWriter printWriter = new PrintWriter( new StringWriter() ) ){
      t.printStackTrace( printWriter );
      
      if( LOG_EN ) LOG.warning( String.format(
            "%s was thrown in %s::%s:%s.\n The " + "given cause was: %s \n\t "
            + "Stack " + "Trace \n%s" ,
            t.getClass().getCanonicalName() ,
            thisClass.getEnclosingClass().getSimpleName() ,
            thisClass.getEnclosingClass().getEnclosingClass().getSimpleName() ,
            thisClass.getEnclosingMethod().getName() ,
            t.getMessage() ,
            printWriter
                                             ) );
    }
    
  }
  
  
  /**
   * Cast int array integer [ ].
   * cant just cast Integer[] to int[].  This is mostly a reminder more than
   * a useful method.
   * @param intArray
   *       the int array
   *
   * @return the integer [ ]
   */
  public Integer[] castIntArray(int[] intArray){
    return Arrays.stream( intArray ).boxed().toArray( Integer[] :: new );
    
  }
  
  
  /**
   * Get the relative component of a  path, assuming it is a child of the
   * running program.
   *
   * Takes the User.Dir path,and the given path and iterates over the common
   * path elements, when they start to differ adds each of the remaining paths.
   *
   * todo- in the future, by comparing lengths, you could determine if a was
   * a child of b, if the common length and user.dir length were the same.
   *
   *
   * @param path a given path.
   * @return = the path segments by which theses differ,
   *
   * Example
   * User.dir:
   *      O:\OneDrive\Code\IntelliJ\Timmons
   * Given Path:
   *      O:\OneDrive\Code\IntelliJ\Timmons\res\input\asc10K.dat
   * Common Path Returned :: \OneDrive\Code\IntelliJ\Timmons
   * Relative Path Returned::  \res\input\asc10K.dat
   */
  public static String getRelPath( Path path ){
    Iterator<Path> i, j;
    Path  a,
          b = null,
          q = path.toAbsolutePath(),
          p=Path.of( System.getProperty( "user.dir" ) ).toAbsolutePath();;
    String relative = "",
          common = q.toString().substring( 0 , 4 );
          //see if drive letters match
    if(common.equals( p.toString().substring( 0,4 ) )){
//      System.out.println(p.toString());
//      System.out.println(q.toString());
      for(i=q.iterator(), j=p.iterator(); i.hasNext();){
        a = i.next();
        if(j.hasNext()) b = j.next();
        if( b != null &&  a.equals( b )){
          common += File.separator;
          common += a.toString();
        } else{
          relative += File.separator + a.toString() ;
          while (i.hasNext()){
            a = i.next();
            relative += File.separator + a.toString() ;
          }
        }
      }
      return relative;
    }else return q.toString();
    //    System.err.println(common);
    //    System.err.println(relative);
  }
  
  
  /**
   * Returns the relative path of the programs running directory to a given
   * path.
   * @param path The path, likely absolute, though possibly already relative
   *             (though whats the point?)
   * @return the relative path string, from the executing path.
   *
   * This is the appropriate implementation of the methods I wrote above an
   * below.
   */
  public static String getRelativePath(Path path){
    Path relativeTo = Path.of( Sys.USER_DIR );
    if( !path.isAbsolute()) path = path.toAbsolutePath();
    return  relativeTo.relativize( path ).toString();
  }
  
  /**
   * Get the relative component of a  path, assuming it is a child of the
   * running program.
   *
   * Takes the User.Dir path,and the given path and iterates over the common
   * path elements, when they start to differ adds each of the remaining paths.
   *
   * todo- in the future, by comparing lengths, you could determine if a was
   * a child of b, if the common length and user.dir length were the same.
   *
   *
   * @param path a given path.
   * @return = the path segments by which theses differ,
   *
   * Example
   * User.dir:
   *      O:\OneDrive\Code\IntelliJ\Timmons
   * Given Path:
   *      O:\OneDrive\Code\IntelliJ\Timmons\res\input\asc10K.dat
   * Common Path Returned :: \OneDrive\Code\IntelliJ\Timmons
   * Relative Path Returned::  \res\input\asc10K.dat
   */
  public static String getCommonPathString( Path path ){
    Iterator<Path> i, j;
      Path  a,
            b = null,
            q = path.toAbsolutePath(),
            p=Path.of( System.getProperty( "user.dir" ) ).toAbsolutePath();;
      String relative = "",
            common = q.toString().substring( 0 , 4 );
      //see if drive letters match
      if(common.equals( p.toString().substring( 0,4 ) )){
    System.out.println(p.toString());
    System.out.println(q.toString());
    for(i=q.iterator(), j=p.iterator(); i.hasNext();){
      a = i.next();
      if(j.hasNext()) b = j.next();
      if( b != null &&  a.equals( b )){
        common += File.separator;
      
        common += a.toString();
      } else{
        relative += File.separator + a.toString() ;
        while (i.hasNext()){
          a = i.next();
          relative += File.separator + a.toString() ;
        }
      }
    }
    return common;
}else return "";
      //    System.err.println(common);
//    System.err.println(relative);
}
  
  /**
   * Array formatter => string.
   *
   * Returns a string representation of an array.  This can be customized here.
   * While this is trivial, this is explicitly for the IntelliJ Debugger -
   * custom variable watch formatter.  I could extend the class and override
   * the format() method to gain access to the {@link Formatter} interface
   * and acheive it that way, but it would require a tangible change in my
   * code, that may not always run in this program.
   *
   * So it returns a string.
   *
   *  Just calls the toString method, and adds them serially. With some
   *  Separators.
   *
   * To change the appearance, just modify the final's and the rest should be
   * left alone.
   * @param o
   *       the object, presumed to be an array.
   *
   *
   * @return the string
   */
  public  String arrayFormatter( Object ... o ){
    final String INTER = "]-[";
    final String END = "]";
    final String BEFORE = "";
    if(Objects.nonNull( o )){
      String s = BEFORE;
      for(Object oo : o){
        //change this if needed as well.
        if(Objects.isNull( s )) s = String.format( "{%d}-[" , o.length );
        s +=  INTER + oo.toString();
      }
      return ( s + END );
    }else{
      return "Empty Object";
    }
  
}


  /**
   * Arraytype formatter => string.
   *
   * This is the Static version of above.
   *
   * Returns a string representation of an array.  This can be customized here.
   * While this is trivial, this is explicitly for the IntelliJ Debugger -
   * custom variable watch formatter.  I could extend the class and override
   * the format() method to gain access to the {@link Formatter} interface
   * and acheive it that way, but it would require a tangible change in my
   * code, that may not always run in this program.
   *
   * So it returns a string.
   *
   *  Just calls the toString method, and adds them serially. With some
   *  Separators.
   *
   * To change the appearance, just modify the final's and the rest should be
   * left alone.
   * @param o
   *       the object, presumed to be an array.
   *
   *
   * @return the string
   */
public static String arrayTypeFormatter( Object ... o ){
    final String INTER = "]-[";
    final String END = "]";
    final String BEFORE = "";
    if(Objects.nonNull( o )){
      String s = BEFORE;
      for(Object oo : o){
        //change this if needed as well.
        if(Objects.isNull( s )) s = String.format( "{%d}-[" , o.length );
        s +=  INTER + oo.toString();
      }
      return ( s + END );
    }else{
      return "Empty Object";
    }
  
}
  
  
  /**
   * Convienence method to get the formatting string of this object, without
   * having to create an object just to get it.  Just pass an Integer array
   * @param ints - an Integer Array.
   *             prints the first `MAX` values.  Which is set to 100 by default.
   *
   * @return a formatted string of the array with guideposts.
   */
  public static String formatSet(int length, Integer ... ints){
    int maxLength = length;
    int n = ints.length;
    StringBuilder sb = new StringBuilder();
    for( int i = 0 ; i <n  ; i++ ){
      sb.append( VALUE_SEP );
      if(n>5 && i%5== 0) sb.append( String.format( "(%d)" , i ) );
      sb.append(
            ( ints[ i ] != null )
            ? ints[ i ]
            : NULL_REPRESENTATION
               );
      if(i>= maxLength) break;
    }
    return sb.toString();
  }
  
  
  /**
   * Convienence method to get the formatting string of this object, without
   * having to create an object just to get it.  Just pass an Integer array
   * @param ints - an Integer Array.
   *             prints the first `MAX` values.  Which is set to 100 by default.
   *
   * @return a formatted string of the array with guideposts.
   */
  public static String formatSet(int length, int ... ints){
    int maxLength = length;
    int n = ints.length;
    StringBuilder sb = new StringBuilder();
    for( int i = 0 ; i <n  ; i++ ){
      sb.append( VALUE_SEP );
      //print the mile post.
      if(n>5 && i%5== 0) sb.append( String.format( "(%d)" , i ) );
      //add the value.
      sb.append( ints[ i ] );

      if(i>= maxLength) break;
    }
    return sb.toString();
  }
  
  /**
   * Overloaded method of Format Set, not requiring specifying a length.  Run
   * with care, as with very large sets, it can go a bit overboard.
   *
   * @param ints the series or array of ints to print
   * @return A String with the representation, of the array.
   */
  public static String  formatSet(int ... ints){
    return formatSet( ints.length , ints );
  }
  
  
  /**
   * Overloaded method of Format Set, not requiring specifying a length.  Run
   * with care, as with very large sets, it can go a bit overboard.
   *
   * @param ints the series or array of ints to print
   * @return A String with the representation, of the array.
   */
  public static String  formatSet(Integer ... ints){
    return formatSet( ints.length , ints );
  }
  
  
  /**
   * Convienence method to get the formatting string of this object, without
   * having to create an object just to get it.  Just pass an Integer array
   * @param ints - an Integer Array.
   *             prints the first `MAX` values.  Which is set to 100 by default.
   *
   * @return a formatted string of the array with guideposts.
   */
  public static String formatIntArray(Integer ... ints){
    final int MAXLEN = 100;
    int n = ints.length;
    StringBuilder sb = new StringBuilder();
    for( int i = 0 ; i <n  ; i++ ){
      sb.append( VALUE_SEP );
      if(n>5 && i%5== 0) sb.append( String.format( "(%d)" , i ) );
      sb.append(
            ( ints[ i ] != null )
            ? ints[ i ]
            : NULL_REPRESENTATION
               );
      if(i>= MAXLEN) break;
    }
    return sb.toString();
  }
}
