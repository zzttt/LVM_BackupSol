package net.kkangsworld.lvmexec;

import java.io.BufferedReader;
import java.io.FileReader;

import net.kkangsworld.lvmexec.pipeWithLVM;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ResultReader extends Thread {
	
	Handler rHandler;
	public ResultReader() {
		
	}
	
	public ResultReader(Handler rHandler) {
		this.rHandler = rHandler;
		Log.i("LVMJava", "ResultReader thread init");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.i("LVMJava", "ResultReader thread start");
		
		/* �ٸ� App���� ������ �ʿ��� ��� ���� path�� �����ؼ� �׽�Ʈ �Ѵ�. */
		//String path = "/data/data/net.kkangsworld.lvmexec/result_pipe";
		String path = pipeWithLVM.RESULTFIFO;
		
			try
			{ 
				/* ���� package ����� result_pipe */
				//String path = getApplicationInfo().dataDir+"/result_pipe";
				
				/* return �� txt */
				String txt = "";
				//Log.i("LVMJava", "openning input stream");
				Log.i("LVMJava", "PATH : "+path);
				
				//����ؼ� BufferedReade�� �Էµ� result pipe data�� gettering
				
				while(true) {
					Log.i("LVMJava", "run whiling..");
					BufferedReader reader = 
							 new BufferedReader(new FileReader(path));
				    while((txt = reader.readLine()) != null) 
				    {
				    	Log.d("LVMJava", txt);
				    	
				    	if(txt.contains("[in run_command]"))
				    			Log.d("LVMJava", "in run command get return");
				    	else {
					    	Message msg = Message.obtain();
							msg.what = 0; msg.arg1 = 0; //���⿡ �ش� COMMAND���п� �����ɵ�
							msg.obj = txt;
							rHandler.sendMessage(msg);
							//�� ������ use Handler
							Log.d("LVMJava", "To handler success");	
				    	}
						
				    } 
				    reader.close();
			    }
				
			 
			 }catch(Exception e) {
				 e.printStackTrace();
				 }
		}
}