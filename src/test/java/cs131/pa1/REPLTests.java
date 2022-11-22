https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package cs131.pa1;

import cs131.pa1.filter.Message;
import cs131.pa1.filter.concurrent.ConcurrentREPL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class REPLTests {

	@Test
	public void testExit(){
		testInput("exit");
		ConcurrentREPL.main(null);
		assertOutput("");
	}
	
	@Test
	public void testNotACommand1(){
		testInput("not-a-command\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + "The command [not-a-command] was not recognized.\n");
	}
	
	@Test
	public void testNotACommand2(){
		testInput("ls | gripe HELLO\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + "The command [gripe HELLO] was not recognized.\n");
	}
	
	@Test
	public void testNotACommand3(){
		testInput("headhello.txt\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + "The command [headhello.txt] was not recognized.\n");
	}
	
	@Test
	public void testNotACommand4(){
		testInput("cdsrc\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + "The command [cdsrc] was not recognized.\n");
	}
	
	@Test
	public void testNotACommand5(){
		testInput("pwd | grepunixish\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + "The command [grepunixish] was not recognized.\n");
	}
	
	@Test
	public void testCanContinueAfterError1(){
		testInput("cd dir1\n ls | gripe HELLO\nls | grep f1\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND.toString() + Message.NEWCOMMAND + "The command [gripe HELLO] was not recognized.\n> f1.txt\n");
	}
	
	@Test
	public void testCanContinueAfterError2(){
		testInput("head -10005 fizz-buzz-100000.txt | grep 1 | wc\nhead -10005 fizz-buzz-10000.txt | grep 1 | wc\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + "At least one of the files in the command [head -10005 fizz-buzz-100000.txt] was not found.\n> 1931 1931 7555\n");
	}
	
	@Test
	public void testFileNotFound(){
		testInput("head doesnt-exist.txt\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + "At least one of the files in the command [head doesnt-exist.txt] was not found.\n");
	}
	
	@Test
	public void testDirectoryNotFound() {
		testInput("cd mystery-dir\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + Message.DIRECTORY_NOT_FOUND.with_parameter("cd mystery-dir"));
	}
    
    
    // ********** Input/Output Tests **********

	@Test
	public void testPwdCannotHaveInput() {
		testInput("head hello-world.txt | pwd\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + Message.CANNOT_HAVE_INPUT.with_parameter("pwd"));
	}
	
	@Test
	public void testLsCannotHaveInput() {
		testInput("head hello-world.txt | ls\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + Message.CANNOT_HAVE_INPUT.with_parameter("ls"));
	}
	
	@Test
	public void testCdCannotHaveInput() {
		testInput("head hello-world.txt | cd dir1\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + Message.CANNOT_HAVE_INPUT.with_parameter("cd dir1"));
	}
	
	@Test
    public void testCdCannotHaveOutput1() {
    	testInput("cd dir1\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND.toString());
    }
	
	@Test
    public void testCdCannotHaveOutput2() {
    	testInput("cd dir1 | wc\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND + Message.CANNOT_HAVE_OUTPUT.with_parameter("cd dir1"));
    }
	
	@Test
	public void testCdRequiresParameter() {
		testInput("cd\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + Message.REQUIRES_PARAMETER.with_parameter("cd"));
	}

	@Test
    public void testHeadCannotHaveInput() {
    	testInput("pwd | head hello-world.txt\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND + Message.CANNOT_HAVE_INPUT.with_parameter("head hello-world.txt"));
    }
    
    @Test
    public void testHeadRequiresParameter1() {
    	testInput("head\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND + Message.REQUIRES_PARAMETER.with_parameter("head"));
    }

    @Test
    public void testHeadRequiresParameter2() {
    	testInput("head -100\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND + Message.REQUIRES_PARAMETER.with_parameter("head -100"));
    }

    @Test
    public void testHeadInvalidParameter() {
    	testInput("head -iloveos hello-world.txt\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND + Message.INVALID_PARAMETER.with_parameter("head -iloveos hello-world.txt"));
    }
    
    @Test
    public void testHeadFileNotFound() {
    	testInput("head iloveos hello-world.txt\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND + Message.FILE_NOT_FOUND.with_parameter("head iloveos hello-world.txt"));
    }
    

	@Test
	public void testGrepRequiresInput() {
		testInput("grep hahaha\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + Message.REQUIRES_INPUT.with_parameter("grep hahaha"));
	}
	
	@Test
	public void testGrepRequiresParameter() {
		testInput("pwd | grep\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + Message.REQUIRES_PARAMETER.with_parameter("grep"));
	}
	
	@Test
	public void testWcRequiresInput() {
		testInput("wc\nexit");
		ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND + Message.REQUIRES_INPUT.with_parameter("wc"));
	}
        
    @Test
    public void testRedirectionRequiresInput() {
    	testInput("> new-hello-world.txt\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND + Message.REQUIRES_INPUT.with_parameter("> new-hello-world.txt"));
    }
    
    @Test
    public void testRedirectionRequiresParameter() {
    	testInput("ls >\nexit");
    	ConcurrentREPL.main(null);
    	assertOutput(Message.NEWCOMMAND + Message.REQUIRES_PARAMETER.with_parameter(">"));
    }
    
    @Test
    public void testRedirectionNoOutput1() {
    	testInput("head hello-world.txt > new-hello-world.txt\nexit");
    	ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND.toString());
		AllConcurrentTests.destroyFile("new-hello-world.txt");
    }
    
    @Test
    public void testRedirectionNoOutput2() {
    	testInput("head hello-world.txt > new-hello-world.txt|wc\nexit");
    	ConcurrentREPL.main(null);
		assertOutput(Message.NEWCOMMAND.toString() + Message.CANNOT_HAVE_OUTPUT.with_parameter("> new-hello-world.txt"));
		AllConcurrentTests.destroyFile("new-hello-world.txt");
    }
    
 // *** repl_jobs tests ***
   	@Test
   	public void testREPLJobs() {
   		testInput("head -10005 fizz-buzz-10000.txt | grep Fi | wc > replTest1.txt &\nrepl_jobs\n" +
   				"head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > replTest3.txt\nrepl_jobs\nexit");
   		ConcurrentREPL.main(null);
   		String result = outContent.toString().replace("\r", "");
   		assertEquals(Message.WELCOME.toString() + Message.NEWCOMMAND + Message.NEWCOMMAND +
   				"\t1. head -10005 fizz-buzz-10000.txt | grep Fi | wc > replTest1.txt &\n" +
   				Message.NEWCOMMAND + Message.NEWCOMMAND + Message.NEWCOMMAND + Message.GOODBYE , result);
   		assertTrue((new File("replTest1.txt")).exists());
   	}

   	@Test
   	public void testREPLMultipleJobs() {
   		testInput("head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > replTest1.txt &\n" +
   				"head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > replTest2.txt &\n" +
   				"repl_jobs\nexit");
   		ConcurrentREPL.main(null);
   		String result = outContent.toString().replace("\r", "");
   		try {
   			Thread.sleep(1000);
   		} catch (InterruptedException e) {
   			e.printStackTrace();
   		}

   		try {
   			assertEquals(Message.WELCOME.toString() + Message.NEWCOMMAND + Message.NEWCOMMAND + Message.NEWCOMMAND +
   					"\t1. head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > replTest1.txt &\n" +
   					"\t2. head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > replTest2.txt &\n" +
   					Message.NEWCOMMAND + Message.GOODBYE , result);
   		} catch (AssertionError e) {
   			assertEquals(Message.WELCOME.toString() + Message.NEWCOMMAND + Message.NEWCOMMAND + Message.NEWCOMMAND +
   					"\t1. head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > replTest2.txt &\n" +
   					"\t2. head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > replTest1.txt &\n" +
   					Message.NEWCOMMAND + Message.GOODBYE , result);
   		}
   		assertTrue((new File("replTest1.txt")).exists());
   		assertTrue((new File("replTest2.txt")).exists());
   		try {
   			Thread.sleep(5000);
   		}
   		catch(InterruptedException ie){
   			ie.printStackTrace();
   		}
   	}
   	
   	@Test
   	public void testKill() {
   		testInput("head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > killTest1.txt &\n" +
   				"head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > killTest2.txt &\n" +
   				"head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > killTest3.txt &\n" +
   				"kill 2\n"+"head -10005 fizz-buzz-10000.txt | grep waittt\n"+
   				"repl_jobs\n"+ "head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > killTest4.txt\n" + "exit");
   		ConcurrentREPL.main(null);
   		String result = outContent.toString().replace("\r", "");
   		try {
   			Thread.sleep(1000);
   		} catch (InterruptedException e) {
   			e.printStackTrace();
   		}
   		assertEquals(Message.WELCOME.toString() + Message.NEWCOMMAND + Message.NEWCOMMAND + 
   				Message.NEWCOMMAND + Message.NEWCOMMAND + Message.NEWCOMMAND + Message.NEWCOMMAND +
   					"\t1. head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > killTest1.txt &\n" +
   					"\t3. head -1500005 fizz-buzz-1500000.txt | grep Fi | wc > killTest3.txt &\n" +
   					Message.NEWCOMMAND + Message.NEWCOMMAND+ Message.GOODBYE , result);
   		assertTrue((new File("killTest1.txt")).exists());
   		assertTrue((new File("killTest2.txt")).exists());
   		assertTrue((new File("killTest3.txt")).exists());
   		assertTrue((new File("killTest4.txt")).exists());
   		File f = new File("killTest2.txt");
   		if (f.exists()) {
   			try {
  				InputStreamReader isr = new InputStreamReader(new FileInputStream(f));
  				int cnt=0;
  				while(isr.ready()) {
  					isr.read();
  					cnt++;
  				}
  				isr.close();
  				if(cnt>0)
  					assertTrue(false);
  			} catch (FileNotFoundException e) {
  				e.printStackTrace();
  				assertTrue(false);
  			}
   			catch (IOException ioe) {
   				ioe.printStackTrace();
  				assertTrue(false);
   			}
   		}
   		try {
   			Thread.sleep(5000);
   		}
   		catch(InterruptedException ie){
   			ie.printStackTrace();
   		}
   	}
   	
   	@Test
   	public void testKillParams() {
   		testInput("kill\n" + "kill asdf\n" + "exit");
   		ConcurrentREPL.main(null);
   		String result = outContent.toString().replace("\r", "");
   		assertEquals(Message.WELCOME.toString() + Message.NEWCOMMAND + Message.REQUIRES_PARAMETER.with_parameter("kill")+ Message.NEWCOMMAND + Message.INVALID_PARAMETER.with_parameter("kill asdf") +Message.NEWCOMMAND+ Message.GOODBYE, result);
   	}
	
	private ByteArrayInputStream inContent;
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	
	public void testInput(String s){
		inContent = new ByteArrayInputStream(s.getBytes());
		System.setIn(inContent);
	}
	
	public void assertOutput(String expected){
		AllConcurrentTests.assertOutput(expected, outContent);
	}
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setIn(null);
	    System.setOut(null);
	    System.setErr(null);
	}
}
