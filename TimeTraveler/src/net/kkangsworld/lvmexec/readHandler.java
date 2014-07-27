package net.kkangsworld.lvmexec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.FrameWork.SnapshotImageMaker;
import com.example.timetraveler.R;
import com.example.timetraveler.SrvBackupActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Piped LVM 을 이용한 결과를 반영하는 핸들러
 * @author Administrator
 *
 */
public class readHandler extends Handler {

	private String readResult;
	private ArrayList<String> ssStrList ;
	private AlertDialog aDialog;
	private Context context;
	private ListView lv;
	
	public readHandler(){
		
	}
	public readHandler( Context context, ListView lv) {
		this.context = context;
		this.lv = lv;
	}

	@Override
	public void handleMessage(Message msg) {
		Log.i("handler", "ResultReader Handler result get");
		switch (msg.what) {
		case 0: // case 0
			Log.i("handler", "msg 0");
			// Toast.makeText(getApplicationContext(), (String)msg.obj,
			// Toast.LENGTH_LONG).show();
			this.readResult = (String) msg.obj;
			Log.d("inAction", "[" + getClass() + "]" + readResult);

			this.sendEmptyMessage(100); //set ListView as data 
			//( msg 0 으로 들어오면 , readResult 를 저장하고 case 100 수행) 
			
			break;
		case 100: 
			// lvs 에 따른 snapshot list 를 읽어 listView에 보임

			ssStrList = new ArrayList<String>();
			readResult = readResult.replace("Convert", ",");
			
			String[] strArr = readResult.split(",");
			
			Log.i("handler", readResult);
			
			for(int i = 0 ; i < strArr.length ; i++){
				if(strArr[i].equals("vg")){
					ssStrList.add(strArr[i-1]); // 임시등록
					
					/*if(strArr[i+1].startsWith("s")) // 스냅샷만 출력해 주는 부분
						ssStrList.add(strArr[i-1]);*/
				}
			}
			
			//Toast.makeText(context, readResult, Toast.LENGTH_SHORT).show();
			
			
			
			//dd if="filePath" obs="bytes"
			
			// ssList를 이용하여 View Listing
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, ssStrList);
			
			Log.i("lvm", "set adapt");
			lv.setAdapter(adapter); // set Adapter
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View vv,
						int itemId, long id) {
					// TODO Auto-generated method stub

					// itemId ( 0 부터 등록된 순서대로 읽어들임 )

					final String ssName = ssStrList.get(itemId); // ssName read

					final AlertDialog.Builder adb = new AlertDialog.Builder(vv
							.getContext());
					adb.setTitle("Notice");
					adb.setMessage("서버에 스냅샷을 전송합니다.");
					aDialog = adb.create();

					adb.setPositiveButton("전송시작", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							ProgressDialog pd = new ProgressDialog(
									context);
							pd.setTitle("전송중 .. ");
							pd.setMessage("Snapshot 을 서버로 전송 중 입니다..");
							pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
							pd.setCancelable(true);
							pd.show();
							
							
							
							// Snapshot Imaging
							// Snapshot Send to Server
							
							SnapshotImageMaker sim = new SnapshotImageMaker(ssName);
							sim.start();
							

							// confirm

							// activity end

							// finishActivity();
						}
					});
					adb.setNegativeButton("취소", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							setDismiss(aDialog);
						}

					});

					adb.show();

				}

				
				private void setDismiss(Dialog dialog) {
					if (dialog != null && dialog.isShowing())
						dialog.dismiss();
				}

			});
			

			Log.i("lvm", "end adapt");
			
			break;
		}

	}

	public String readResult() {
		return this.readResult;
	}
}
