package cn.edu.bupt.sdmda.main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.edu.bupt.sdmda.ds.linearlist.LinkedQueue;
import cn.edu.bupt.sdmda.ds.linearlist.MyLinkedList;

public class PrinterSimulator {
	// maximum time tick to simulate
	public static int MAXTICK;
	// current tick, added by function tick()
	private static int _currTick;
	// Page Per Minute of Printer
	private static int PPM;
	// the printer
	private static Printer _printer;
	// queue to store print tasks
	// TODO: CHANGE THIS TO YOUR QUEUE 
	private static Queue<Task> _queue;
	// list to store tasks finished print finished
	
	// TODO: CHANGE THIS TO YOUR LIST 
	private static List<Task> _allTasks;

	// main loop of this program
	private static void tick() {
		System.out.println("Tick " + _currTick);
		// try to generate a task according the probability 
		// and add it to the queue if succeeded
		// TODO: YOUR CODE
		
		// printer tick
		_printer.tick(_currTick);
		
		
		_currTick++;
	}

	// default setting if parsing failed
	public static void defaultSetting() {
		Task.TASKGENPROB = 0.01;
		Task.MAXPAGENUM = 20;
		MAXTICK = 100000;
		// MAXTICK = Integer.MAX_VALUE;
		PPM = 30;
	}
	
	// function to print final summary information
	private static void printSummary() {
		int sumTasks, sumWaitingTime = 0;
		sumTasks = _allTasks.size();
		for (Task t : _allTasks) {
			sumWaitingTime += t.getWaitingTime();
		}
		String fmt = "Total %d tasks, wait for %d seconds, average %f seconds";
		System.out.println(
				String.format(fmt, 
						sumTasks, sumWaitingTime, (double) (sumWaitingTime) / sumTasks));
	}

	// entry point of this work
	public static void main(String[] args) {
		// try to parse the value of variable from args
		try {
			Task.TASKGENPROB = Double.parseDouble(args[1]);
			Task.MAXPAGENUM = Integer.parseInt(args[2]);
			MAXTICK = Integer.parseInt(args[3]);
			PPM = Integer.parseInt(args[4]);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Arguments error in main of PrinterSimulator, use Default settings");
			defaultSetting();
		}
		
		_currTick = 0;
		_queue = new LinkedList<Task>();
		_allTasks = new ArrayList<Task>();

		// initialize the printer
		_printer = new Printer(PPM, _queue, _allTasks);

		// main loop
		while ((_currTick++) < MAXTICK) {
			tick();
		}
		
		printSummary();
	}

}

class Printer {
	// ppm of printer
	private int _pagePerMinute;
	// remaining time of current task
	private int _remainingTime;
	// queue and list to store tasks
	private Queue<Task> _queue;
	private List<Task> _allTasks;
	private Task curTask;

	public Printer(int ppm, Queue<Task> q, List<Task> l) {
		_pagePerMinute = ppm;
		_queue = q;
		_allTasks = l;
		curTask = null;
	}

	public void tick(long time) {
		// if printer is not busy and queue is not empty
		// get a new task from queue
		// calculate the remaining time of this task
		if(_remainingTime == 0 && !_queue.isEmpty()) {
			curTask = _queue.poll();
			curTask.start(time);
			_remainingTime = curTask.getPageNum()/_pagePerMinute;
		}
		// time elapsed
		if(_remainingTime > 1) _remainingTime--;
		else if(_remainingTime == 1) {
			_remainingTime--;
			curTask.finish(time);
			curTask = null;
		}
		Task newtask = Task.genTask();
		if(newtask != null) {
			_allTasks.add(newtask);
			_queue.add(newtask);
		}
	}
}

class Task {
	// page of current task
	private int _pageNum;
	// the time when the task is added into the queue
	private long _startTime;
	// the time when the task is got out of the queue
	private long _endTime;
	
	// probability to generate new task per second
	public static double TASKGENPROB = 0.01;
	// maximum number of pages of the task
	public static int MAXPAGENUM = 20;

	// initialize this task
	public Task(int pageNum) {
		_pageNum = pageNum;
		_endTime = -1;
		System.out.println("Task generated, pages: " + pageNum);
	}
	
	// record the start time of this task
	public void start(long time) {
		_startTime = time;
	}

	// record the end time of this task
	public void finish(long time) {
		_endTime = time;
	}

	// get the waiting time in queue of this task
	public int getWaitingTime() {
		if (_endTime == -1)
			return -1;
		return (int) (_endTime - _startTime);
	}

	public int getPageNum() {
		return _pageNum;
	}
	
	// generate a random number
	// and decide to generate a new task or not
	public static Task genTask() {
		double rand = Math.random();
		if(rand < TASKGENPROB) {
			int pagenum = (int) (Math.random()*MAXPAGENUM);
			Task newtask = new Task(pagenum);
			return newtask;
		}
		return null;
	}
}