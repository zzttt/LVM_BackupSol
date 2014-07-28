package com.example.timetraveler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.kkangsworld.lvmexec.pipeWithLVM;
import net.kkangsworld.lvmexec.readHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SrvBackupActivity extends Activity {

	
	private pipeWithLVM pl;
	private readHandler rh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_srv_backup);
		
		ListView lv = (ListView) this.findViewById(R.id.lv_sList);
		
		rh = new readHandler(this,lv);
		
		// LVM pipe 에 핸들러와 함께 전송
		pl = new pipeWithLVM(rh);
		
		pl.ActionWritePipe("lvs --separator , ");
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.srv_backup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	// Display Snapshot list in device
	
	public File[] getSnapshotList(){
		
		File sHome = new File(MainActivity.homePath);
		File[] sList = sHome.listFiles();
		
		return sList;		
	}
	
	
	/**
	 * back button click
	 */
	@Override
	 public void onBackPressed(){
		finish();
	 }
	public void finishActivity(){

		// Activity finish
		this.finish();
		
	}
	
	
	
}


