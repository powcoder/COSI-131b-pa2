https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package cs131.pa1.filter.concurrent;
import java.util.LinkedList;
import java.util.Queue;

import cs131.pa1.filter.Filter;

/**
 * An abstract class that extends the Filter and implements the basic functionality of all filters. Each filter should
 * extend this class and implement functionality that is specific for that filter.
 * @author cs131a
 *
 */
public abstract class ConcurrentFilter extends Filter {
	/**
	 * The input queue for this filter
	 */
	protected Queue<String> input;
	/**
	 * The output queue for this filter
	 */
	protected Queue<String> output;
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedList<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	/**
	 * Gets the next filter
	 * @return the next filter
	 */
	public Filter getNext() {
		return next;
	}
	/**
	 * processes the input queue and writes the result to the output queue
	 */
	public void process(){
		while (!input.isEmpty()){
			String line = input.poll();
			String processedLine = processLine(line);
			if (processedLine != null){
				output.add(processedLine);
			}
		}	
	}
	
	@Override
	public boolean isDone() {
		return input.size() == 0;
	}
	
	protected abstract String processLine(String line);
	
}
