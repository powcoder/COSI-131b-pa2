https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package cs131.pa1.filter.concurrent;
import java.io.File;
/**
 * The filter for ls command
 * @author cs131a
 *
 */
public class LsFilter extends ConcurrentFilter{
	/**
	 * The counter of how many contents are in the directory
	 */
	int counter;
	/**
	 * The folder of the current working directory 
	 */
	File folder;
	/**
	 * The list of files within the current working directory
	 */
	File[] flist;
	/**
	 * The constructor of the ls filter, no parameters.
	 */
	public LsFilter() {
		super();
		counter = 0;
		folder = new File(ConcurrentREPL.currentWorkingDirectory);
		flist = folder.listFiles();
	}
	
	@Override
	public void process() {
		while(counter < flist.length) {
			output.add(processLine(""));
		}
	}
	
	@Override
	public String processLine(String line) {
		return flist[counter++].getName();
	}
}
