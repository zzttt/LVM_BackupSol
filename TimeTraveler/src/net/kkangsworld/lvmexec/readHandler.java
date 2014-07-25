package net.kkangsworld.lvmexec;

import java.util.ArrayList;

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
import android.widget.AdapterView.OnItemClickListener;

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
			
			break;
		case 100:

			ssStrList = new ArrayList<String>();
			String[] strArr = readResult.split(",");
			
			Log.i("handler", readResult);
			
			for(int i = 0 ; i < strArr.length ; i++){
				if(strArr[i].equals("vg")){
					if(strArr[i+1].startsWith("s"))
						ssStrList.add(strArr[i-1]+" "+strArr[i+1]);
				}
			}
			

			// ssList를 이용하여 View Listing
			
			ArrayAdapter adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, ssStrList);

			
			lv.setAdapter(adapter); // set Adapter
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View vv,
						int itemId, long id) {
					// TODO Auto-generated method stub

					// itemId ( 0 부터 등록된 순서대로 읽어들임 )

					String ssName = ssStrList.get(itemId); // ssName read

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

			break;
		}

	}

	public String readResult() {
		return this.readResult;
	}
}
