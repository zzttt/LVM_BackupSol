package net.kkangsworld.lvmexec;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class readHandler extends Handler {
	
	String readResult;
	
	public readHandler() {
	
	}
	
	public void handleMessage(Message msg) {
		Log.i("LVMJava", "ResultReader Handler result get");
		switch(msg.what) {
		case 0: //case 0
			//Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_LONG).show();
			readResult = (String)msg.obj;
			Log.d("inAction", "["+getClass()+"]"+readResult);
			break;
		}	
			
	}
	
	public String readResult() {
		return readResult;
	}
}
