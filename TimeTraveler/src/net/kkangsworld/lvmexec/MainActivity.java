package net.kkangsworld.lvmexec;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	Button btn_readpipe;
	Button btn_writepipe;
	pipeWithLVM m_pipeWithLVM;
	readHandler rHandler = new readHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		m_pipeWithLVM = new pipeWithLVM(rHandler);
		
		btn_readpipe = (Button)findViewById(R.id.btn_readPipe);
		btn_writepipe = (Button)findViewById(R.id.btn_writePipe);
		//exec write pipe
		//ActGetPipe();
		
		/* button Listener ���� */
    	OnClickListener btnListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()) {
				case R.id.btn_readPipe:
					Log.d("LVMExec", "in Java, onclicked get result");
					m_pipeWithLVM.ActionGetPipe();
					break;
				case R.id.btn_writePipe:
					Log.d("LVMExec", "in Java, onclicked write command input");
					m_pipeWithLVM.ActionWritePipe("lvs");
					
					Toast.makeText(getApplicationContext(), rHandler.readResult(), Toast.LENGTH_SHORT).show();

				}
				
			}
    	};
    	btn_readpipe.setOnClickListener(btnListener);
    	btn_writepipe.setOnClickListener(btnListener);
    	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
	
