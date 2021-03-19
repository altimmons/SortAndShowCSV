package org.DataStructures;


import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 * @param <T> The type of record to collect.
 *
 */
public class RecordBag<T> implements Collection<T>{
  private T t;
  Class T
  Logger LOGGER = Logger.getLogger( "RecordBag" );
   private int size = 0;
  private static final int INITIAL_SIZE = 1000;
  T[] records = T[ INITIAL_SIZE ];
  public RecordBag( ){
    this.size = 0;
  }
  
  @Override
  public int size(){
    return this.size;
  }
  
  @Override
  public boolean isEmpty(){
    return (size == 0);
  }
  
  @Override
  public boolean contains( Object o ){
    return false;
  }
  
  @NotNull
  @Override
  public Iterator<T> iterator(){
    return null;
  }
  
  @NotNull
  @Override
  public Object[] toArray(){
    return new Object[ 0 ];
  }
  
  @NotNull
  @Override
  public <T1> T1[] toArray( @NotNull T1[] a ){
    return null;
  }
  
  @Override
  public boolean add( T t ){
    return false;
  }
  
  @Override
  public boolean remove( Object o ){
    return false;
  }
  
  @Override
  public boolean containsAll( @NotNull Collection<?> c ){
    return false;
  }
  
  @Override
  public boolean addAll( @NotNull Collection<? extends T> c ){
    return false;
  }
  
  @Override
  public boolean removeAll( @NotNull Collection<?> c ){
    return false;
  }
  
  @Override
  public boolean retainAll( @NotNull Collection<?> c ){
    return false;
  }
  
  @Override
  public void clear(){
  
  }
}
