package org.DataStructures;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStack{
  
  @Test
  void stackTest(){
    Stack<String> testStack =new Stack<>();
    testStack.push( "3" );
    testStack.push( "Test String" );
    testStack.push( "" );
    testStack.pop();
    String s = "Hello";
    testStack.push( s );
    assertEquals(3, testStack.getLength());
    assertEquals( s, testStack.peek() );
    assertEquals( s, testStack.pop() );
    
    
  }
  
  
}
