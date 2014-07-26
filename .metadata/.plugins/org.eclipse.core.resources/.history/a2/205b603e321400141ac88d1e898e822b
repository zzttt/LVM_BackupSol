package net.kkangsworld.lvmexec;

import java.io.BufferedReader;
import java.io.FileReader;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class pipeWithLVM {
	
	/* Use for timetraveler package */
	public static final String RESULTFIFO = "/data/data/com.example.timetraveler/result_pipe";
	public static final String CMDFIFO = "/data/data/com.example.timetraveler/cmd_pipe";
	
	private NativePipe nativepipe;
	private ResultReader resultReader;
	private readHandler rHandler;
	
	public pipeWithLVM() {
		//constructor
		nativepipe = new NativePipe(); //Native comm init
		nativepipe.createPipe();
		resultReader = new ResultReader(); //ResultReader thread init;
		resultReader.start();
		//readFromPipe(); //read도 동시에 실행
	}
	
	public pipeWithLVM(readHandler rh) {
		nativepipe = new NativePipe();
		nativepipe.createPipe();
		//this.rHandler = mHandler;
		resultReader = new ResultReader(rh); //ResultReader thread init;
		resultReader.start();
		//readFromPipe(); //read도 동시에 실행
	}
	
	public void ActionWritePipe(String command) {
		
		nativepipe.writePipe(command);
		
	}
	
	public void ActionGetPipe() {
		//readFromPipe();
		//String temp = nativepipe.getPipe();
		//Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
	}

}




