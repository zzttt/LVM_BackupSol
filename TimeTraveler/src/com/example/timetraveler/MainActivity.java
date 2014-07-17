package com.example.timetraveler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.kkangsworld.lvmexec.pipeWithLVM;
import net.kkangsworld.lvmexec.readHandler;

import com.AuthcodeGen.CodeGenerator;
import com.FileManager.SnapshotDiskManager;
import com.FrameWork.ConnServer;
import com.FrameWork.opSwitch;

import android.R.color;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private ViewPager mPager;
	private ArrayList<String> mGroupList = null;
	private ArrayList<ArrayList<String>> mChildList = null;
	private ArrayList<String> mDestList = null;
	private ArrayList<String> mChildDestList = null;
	private ArrayList<ArrayList<String>> mChildListContent = null;
	private ExpandableListView mListView;

	private String userCode;

	private opHandler handler;
	
	public static ProgressDialog pd;

	ConnectivityManager manager;

	NetworkInfo mobile;
	NetworkInfo wifi;
	WifiManager mng;
	WifiInfo info;

	public static String homePath = "/dev/vg/";
	public static PagerAdapterClass pac;

	public static boolean setVal0 = false; // auto snapshot On // Off
	public static int setVal1 = 0; // ��� �뷮 ���� �� 1
	public static int setVal2 = 1; // ��� �뷮 ���� �� 2

	public static File[] snapshotListInSrv;
	public static File[] snapshotListInDev;
	
	readHandler rh;
	pipeWithLVM pl;
	String readResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		manager = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		mng = (WifiManager) getSystemService(WIFI_SERVICE);
		info = mng.getConnectionInfo();

		// Handler ����
		handler = new opHandler();

		// MAC �̿� �����ڵ� ����
		CodeGenerator cg = new CodeGenerator(info.getMacAddress());
		Toast.makeText(getApplication(), cg.genCode(), Toast.LENGTH_SHORT)
				.show();

		userCode = cg.genCode();
		

		pd = new ProgressDialog(this);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("Loading initial data ...");
		pd.show();
		
		
		// ��� Snapshot List �� Load (on Device & on Server)
		// Restore ���� ����� ����Ʈ�� �ε���.

		// 1. Load Snapshot List on Device

		SnapshotDiskManager sdm = new SnapshotDiskManager(homePath);
		File[] sList = sdm.getSnapshotList();
		
		snapshotListInDev = sList; // ��ġ���� ����Ʈ ������
		
		// 2. Load Server List on Server
		ConnServer conn = new ConnServer("211.189.19.45", 12345, 0, userCode,
				handler);
		conn.start();


		// �ϴ� �޴��� ���� Pager
		pac = new PagerAdapterClass(getApplicationContext());
		setLayout();

		mPager = (ViewPager) findViewById(R.id.pager);

		mPager.setAdapter(pac);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_one:
			setCurrentInflateItem(0);

			break;
		case R.id.btn_two: // Restore page button
			setCurrentInflateItem(1);
			snapshotListInDev = null;
			snapshotListInSrv = null;
			
			pd.setMessage("Loading...");
			pd.show();
			
			handler.sendEmptyMessage(101); // View Reset Handler
			
			SnapshotDiskManager sdm = new SnapshotDiskManager(homePath);
			File[] sList = sdm.getSnapshotList();
			
			snapshotListInDev = sList; // ��ġ���� ����Ʈ ������

			// 2. Load Server List on Server
			ConnServer conn = new ConnServer("211.189.19.45", 12345, 0, userCode,
					handler);
			conn.start();
			
			break;
		case R.id.btn_three:
			/*Process p;
			try {
				p = new ProcessBuilder("su").start();

				DataOutputStream os = new DataOutputStream(p.getOutputStream());
				os.writeBytes("echo \"Do I have root?\" >/sdcard/temporary.txt\n");

				// Close the terminal
				os.writeBytes("exit\n");
				os.flush();
				try {
					p.waitFor();
					if (p.exitValue() != 255) {
						// TODO Code to run on success
						Toast.makeText(getApplicationContext(), "root", Toast.LENGTH_SHORT).show();
					} else {
						// TODO Code to run on unsuccessful
						Toast.makeText(getApplicationContext(), "not root", Toast.LENGTH_SHORT).show();
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "not root", Toast.LENGTH_SHORT).show();
				}finally{
					os.close();
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "not root", Toast.LENGTH_SHORT).show();
			}
			
			*/
			setCurrentInflateItem(2);
			break;
		}
	}

	private void setCurrentInflateItem(int type) {
		if (type == 0) {
			mPager.setCurrentItem(0);
		} else if (type == 1) {
			mPager.setCurrentItem(1);
		} else {
			mPager.setCurrentItem(2);
		}
	}

	private Button btn_one;
	private Button btn_two;
	private Button btn_three;

	/**
	 * Layout
	 */

	private void setLayout() {
		btn_one = (Button) findViewById(R.id.btn_one);
		btn_two = (Button) findViewById(R.id.btn_two);
		btn_three = (Button) findViewById(R.id.btn_three);

		btn_one.setOnClickListener(this);
		btn_two.setOnClickListener(this);
		btn_three.setOnClickListener(this);
	}

	private View.OnClickListener mPagerListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = ((Button) v).getText().toString();
			// Toast.makeText(getApplicationContext(), text,
			// Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * PagerAdapter
	 */
	public class PagerAdapterClass extends PagerAdapter { // Page Adapter������ ����

		private ArrayList<View> views = new ArrayList<View>();

		private LayoutInflater mInflater;
		private TextView tmp;

		public PagerAdapterClass(Context c) {
			super();
			mInflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object instantiateItem(ViewGroup pager, int position) {
			View v = null;
			if (position == 0) { // Back up ������
				SimpleCursorAdapter mAdapter;
				v = mInflater.inflate(R.layout.inflate_one, null);

				mGroupList = new ArrayList<String>();
				mChildList = new ArrayList<ArrayList<String>>();
				mChildListContent = new ArrayList<ArrayList<String>>();
				mDestList = new ArrayList<String>();
				mChildDestList = new ArrayList<String>();

				mGroupList.add("��������� ������ ���");
				mGroupList.add("�������� ����");
				mGroupList.add("�ڵ� �������� ����");

				ArrayList<String> child1 = new ArrayList<String>();
				ArrayList<String> child2 = new ArrayList<String>();
				ArrayList<String> child3 = new ArrayList<String>();

				child1.add("���� ���");
				child1.add("���� �޸� ���");

				child2.add("��� ����");

				child3.add("�ڵ� ������ ���");

				mChildListContent.add(child1);
				mChildListContent.add(child2);
				mChildListContent.add(child3);

				mChildList.add(mChildListContent.get(0));
				mChildList.add(mChildListContent.get(1));
				mChildList.add(mChildListContent.get(2));

				mDestList.add("- ��������� ��� �����͸� ���� �Ǵ� ����޸𸮿� �����մϴ�.");
				mDestList.add("- ������¸� ������������ �����մϴ�.");
				mDestList.add("- �ڵ����������� �����մϴ�.");

				mChildDestList.add("������ �̹����� ������ �����մϴ�.");
				mChildDestList.add("����� SDī�忡 ������ �̹����� �����մϴ�.");

				mListView = (ExpandableListView) v.findViewById(R.id.elv_list1);
				mListView.setAdapter(new BaseExpandableAdapter(v.getContext(),
						mGroupList, mChildList, mDestList, mChildDestList, 0));

				// �׷� Ŭ�� ���� ��� �̺�Ʈ
				mListView.setOnGroupClickListener(new OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// Toast.makeText(getApplicationContext(), "g click = "
						// + groupPosition,
						// Toast.LENGTH_SHORT).show();
						switch (groupPosition) {
						case 0:
							break;
						case 1:
							break;
						case 2:
							break;
						}

						return false;
					}
				});

				// Backup �޴����� ���ϵ� Ŭ�� ���� ��� �̺�Ʈ
				mListView.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						/*
						 * Toast.makeText( getApplicationContext(), "c click = "
						 * + childPosition + "(" + groupPosition + ")",
						 * Toast.LENGTH_SHORT).show();
						 */

						switch (groupPosition) {
						case 0: // ���� ������ ������ ���
							if (childPosition == 0) // Server Backup
							{
								Toast.makeText(getApplicationContext(),
										"MAC : " + info.getMacAddress(),
										Toast.LENGTH_SHORT).show();
								// check the wi-fi connection
								if (wifi.isConnected()) {
									// WIFI �� �� ���� �Ǿ�����
									Toast.makeText(getApplicationContext(),
											"Wifi ���� Ȯ��. ������� �õ�.",
											Toast.LENGTH_SHORT).show();

									try {
										// ���� ���� ����
										// opcode == 1 ( Snapshot Server backup
										// )
										ConnServer cs = new ConnServer(
												"211.189.19.45", 12345, 1,
												userCode);

										cs.start();

									} catch (Exception e) {
										Toast.makeText(getApplicationContext(),
												"�������� ������ �����߽��ϴ�.",
												Toast.LENGTH_SHORT).show();
									}

								} else {
									Toast.makeText(
											getApplicationContext(),
											"Server Backup�� Wifi ������¿����� �����մϴ�.",
											Toast.LENGTH_SHORT).show();
								}

							} else { // ���� �޸� Full Backup
								Toast.makeText(getApplicationContext(),
										"����� ���� �޸𸮿� ����� �����մϴ�.",
										Toast.LENGTH_SHORT).show();
							}

							break;
						case 1: // ���� ���� ���� ------------------------------------------ Create Snapshot
							// child menu 1�� �̹Ƿ� �ٷ� ����
							Toast.makeText(getApplicationContext(),
									"����� �����մϴ�.", Toast.LENGTH_SHORT).show();

							String line = "";
							StringBuffer output = new StringBuffer();
							
				
							// pipe �̿��� Snapshot ����
							rh = new readHandler() {
								public void handleMessage(Message msg) {
									Log.i("LVMJava", "ResultReader Handler result get");
									switch(msg.what) {
									case 0: //case 0
										Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_LONG).show();
										readResult = (String)msg.obj;
										Log.d("inMain", readResult);
										break;
									}	
										
								}
							
							};
							Calendar cal = Calendar.getInstance();
							
							String today = (new SimpleDateFormat("yyyyMMddHHmm").format(cal
									.getTime()));
							
							pl = new pipeWithLVM(rh);
							pl.ActionWritePipe("lvcreate -s -L 20M -n "+today+" /dev/vg/userdata");
							

							// ��� ���� process
							/*
							 * try {
							 * 
							 * Process pc = new ProcessBuilder("id")
							 * .directory(new File("/lvm")).start();
							 * 
							 * BufferedReader br = new BufferedReader( new
							 * InputStreamReader(pc .getInputStream()));
							 * 
							 * String s = null; String resCommand = ""; while
							 * ((s = br.readLine()) != null) { if (s != null) {
							 * resCommand += s; resCommand += "\n"; } }
							 * Toast.makeText(getApplicationContext(),
							 * resCommand, Toast.LENGTH_LONG).show();
							 * 
							 * br.close();
							 * 
							 * } catch (IOException e) { // TODO Auto-generated
							 * catch block Log.e("eee",
							 * "EXEPTION!------------- :" + e.toString());
							 * Toast.makeText(getApplicationContext(),
							 * e.getMessage(), Toast.LENGTH_LONG) .show(); }
							 */

							break;
						case 2: // scheduled snapshot
							// Alarm Manager

							setVal0 = true;
							Toast.makeText(getApplicationContext(),
									"�ڵ� �������� �����Ǿ����ϴ�.", Toast.LENGTH_SHORT)
									.show();

							break;
						}
						return false;
					}
				});

				// �׷��� ���� ��� �̺�Ʈ
				mListView
						.setOnGroupCollapseListener(new OnGroupCollapseListener() {
							@Override
							public void onGroupCollapse(int groupPosition) {
								// Toast.makeText(getApplicationContext(),
								// "g Collapse = " + groupPosition,
								// Toast.LENGTH_SHORT).show();
							}
						});

				// �׷��� ���� ��� �̺�Ʈ
				mListView.setOnGroupExpandListener(new OnGroupExpandListener() {
					@Override
					public void onGroupExpand(int groupPosition) {
						// Toast.makeText(getApplicationContext(), "g Expand = "
						// + groupPosition,
						// Toast.LENGTH_SHORT).show();
					}
				});

			} else if (position == 1) { // Restore ������

				SimpleCursorAdapter mAdapter;

				v = mInflater.inflate(R.layout.inflate_two, null);

				views.add(v); // Restore Page �� �÷��������ӿ�ũ�� �־��ش�.

				/*
				 * mGroupList = new ArrayList<String>(); mChildList = new
				 * ArrayList<ArrayList<String>>(); mChildListContent = new
				 * ArrayList<ArrayList<String>>(); mDestList = new
				 * ArrayList<String>(); mChildDestList = new
				 * ArrayList<String>();
				 * 
				 * // Group List �� �Էµȴ� ( Snapshot list�� �׷������ ������ )
				 * mGroupList.add("2014-07-01 21:38 ���� ���");
				 * mGroupList.add("2014-07-02 23:38 ���� ���");
				 * mGroupList.add("2014-07-03 04:38 �ڵ� ���");
				 * 
				 * for (int i = 0; i < mGroupList.size(); i++) {
				 * ArrayList<String> child1 = new ArrayList<String>();
				 * 
				 * child1.add("[�ֱ� ���� ���� ]");
				 * 
				 * mChildListContent.add(child1); }
				 * 
				 * mChildList.add(mChildListContent.get(0));
				 * mChildList.add(mChildListContent.get(1));
				 * mChildList.add(mChildListContent.get(2));
				 * 
				 * mChildDestList.clear(); mChildDestList.add("����");
				 * mChildDestList.add("����"); mChildDestList.add("����");
				 * 
				 * mListView = (ExpandableListView)
				 * v.findViewById(R.id.elv_list2); mListView.setAdapter(new
				 * BaseExpandableAdapter(v.getContext(), mGroupList, mChildList,
				 * mDestList, mChildDestList, 1));
				 * 
				 * // �׷� Ŭ�� ���� ��� �̺�Ʈ mListView.setOnGroupClickListener(new
				 * OnGroupClickListener() {
				 * 
				 * @Override public boolean onGroupClick(ExpandableListView
				 * parent, View v, int groupPosition, long id) {
				 * 
				 * Toast.makeText(getApplicationContext(), "g click = " +
				 * groupPosition, Toast.LENGTH_SHORT).show();
				 * 
				 * 
				 * return false; } });
				 * 
				 * // ���ϵ� Ŭ�� ���� ��� �̺�Ʈ mListView.setOnChildClickListener(new
				 * OnChildClickListener() {
				 * 
				 * @Override public boolean onChildClick(ExpandableListView
				 * parent, View v, int groupPosition, int childPosition, long
				 * id) {
				 * 
				 * return false; } });
				 * 
				 * // �׷��� ���� ��� �̺�Ʈ mListView .setOnGroupCollapseListener(new
				 * OnGroupCollapseListener() {
				 * 
				 * @Override public void onGroupCollapse(int groupPosition) {
				 * 
				 * Toast.makeText(getApplicationContext(), "g Collapse = " +
				 * groupPosition, Toast.LENGTH_SHORT).show();
				 * 
				 * } });
				 * 
				 * // �׷��� ���� ��� �̺�Ʈ mListView.setOnGroupExpandListener(new
				 * OnGroupExpandListener() {
				 * 
				 * @Override public void onGroupExpand(int groupPosition) {
				 * 
				 * Toast.makeText(getApplicationContext(), "g Expand = " +
				 * groupPosition, Toast.LENGTH_SHORT).show();
				 * 
				 * } });
				 */
			} else { // // Setting View ( ���� ������ )

				v = mInflater.inflate(R.layout.inflate_three, null);

				final SharedPreferences pref = getBaseContext().getSharedPreferences("SaveState", getBaseContext().MODE_PRIVATE);
				final SharedPreferences.Editor edit = pref.edit();
				
				setVal1 = pref.getInt("setVal1", 0);
				
				final Spinner dateSpinner = (Spinner) v
						.findViewById(R.id.dateSpinner);

				ArrayList<String> date = new ArrayList<String>();

				for (int i = 1; i <= 15; i++) {
					date.add(Integer.toString(i));
				}

				Log.i("rrr", Integer.toString(date.size()));

				ArrayAdapter<String> aa = new ArrayAdapter<String>(
						v.getContext(), android.R.layout.simple_spinner_item,
						date);

				// ��Ӵٿ� ȭ�鿡 ǥ��
				aa.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
				dateSpinner.setAdapter(aa);

				final CheckBox chkBox1 = (CheckBox) v
						.findViewById(R.id.checkbox_upToSrv);
				final CheckBox chkBox2 = (CheckBox) v
						.findViewById(R.id.checkbox_delSnapshot);
				final CheckBox chkBox3 = (CheckBox) v
						.findViewById(R.id.checkbox_delBackup);

				// ����� ������ �ҷ��ɴϴ�.
				Boolean chk1 = pref.getBoolean("check1", false);
				Boolean chk2 = pref.getBoolean("check2", false);
				Boolean chk3 = pref.getBoolean("check3", false);

				chkBox1.setChecked(chk1);
				chkBox2.setChecked(chk2);
				chkBox3.setChecked(chk3);
				
				dateSpinner.setSelection(pref.getInt("spinnerSelection",0));

				
				// ���� ������ üũ�ڽ� ��Ŭ�� ������
				chkBox1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						chkBox2.setChecked(false);
						chkBox3.setChecked(false);

						Log.i("1_setVal1", Integer.toString(setVal1));
						setVal1 = 1;
						edit.putBoolean("check1", chkBox1.isChecked());
						edit.putBoolean("check2", chkBox2.isChecked());
						edit.putBoolean("check3", chkBox3.isChecked());
						edit.commit();
					}

				});

				chkBox2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						chkBox1.setChecked(false);
						chkBox3.setChecked(false);
						Log.i("2_setVal1", Integer.toString(setVal1));
						setVal1 = 2;
						edit.putBoolean("check2", chkBox2.isChecked());
						edit.putBoolean("check1", chkBox1.isChecked());
						edit.putBoolean("check3", chkBox3.isChecked());
						edit.commit();
					}

				});

				chkBox3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						chkBox1.setChecked(false);
						chkBox2.setChecked(false);
						Log.i("3_setVal1", Integer.toString(setVal1));
						setVal1 = 3;
						edit.putBoolean("check3", chkBox3.isChecked());
						edit.putBoolean("check1", chkBox1.isChecked());
						edit.putBoolean("check2", chkBox2.isChecked());
						edit.commit();
					}

				});

				// Spinner ���� ��
				dateSpinner
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								
								setVal2 = Integer.parseInt(arg0
										.getItemAtPosition(arg2).toString());
								Log.i("eee", Integer.toString(setVal2));

								int selectedPosition = dateSpinner.getSelectedItemPosition();
								Log.i("position", "position : " + (Integer.toString(selectedPosition+1)));
								edit.putInt("spinnerSelection",selectedPosition);
								edit.commit();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub
							}

						});

			}
			((ViewPager) pager).addView(v, 0);

			return v;
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View pager, Object obj) {
			return pager == obj;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		public View getView(int position) {
			return views.get(position);
		}

		public void removeView(int postion) {
			views.remove(postion);
		}

		public int getViewCount() {
			return views.size();
		}

	}

}

class opHandler extends Handler {
	private ArrayList<String> mGroupList = null;
	private ArrayList<ArrayList<String>> mChildList = null;
	private ArrayList<String> mDestList = null;
	private ArrayList<String> mChildDestList = null;
	private ArrayList<ArrayList<String>> mChildListContent = null;
	private ExpandableListView mListView;
	private ArrayList<String> childList;
	private BaseExpandableAdapter bea = null;

	public opHandler() {
		mGroupList = new ArrayList<String>();
		mChildList = new ArrayList<ArrayList<String>>();
		mChildListContent = new ArrayList<ArrayList<String>>();
		mDestList = new ArrayList<String>();
		mChildDestList = new ArrayList<String>();
		childList = new ArrayList<String>();
	}

	@Override
	public void handleMessage(Message msg) {

		super.handleMessage(msg);

		View vv = MainActivity.pac.getView(0);

		switch (msg.what) {
		case 0:

			break;
		case 100: // Snapshot List Handling
			
			// pac ���� View �� �о��
			
			// mChildDestList , mChildList �� group ������ŭ ����ؾ� ��
			// mChildList �� childList�� �׷�. ( ��������� ���������� ���� )
			for (int i = 0; i < MainActivity.snapshotListInSrv.length; i++) {
				mGroupList.add(MainActivity.snapshotListInSrv[i].getName()+" [Server]");
				
				if(childList.size() == 0){
					childList.add("[ ���� ��� ]");
				}
				
				mChildDestList.add("����� �׸��� �����ϴ�.");
				mChildList.add(childList);

			}
			
			
			for (int i = 0; i < MainActivity.snapshotListInDev.length; i++) {
				mGroupList.add(MainActivity.snapshotListInDev[i].getName()+" [Device]");
				
				if(childList.size() == 0){
					childList.add("[ ���� ��� ]");
				}
				
				// mChildDestList �� ���ϸ���Ʈ �Է�
				mChildDestList.add("����� �׸��� �����ϴ�.");
				mChildList.add(childList);
			}

			
			// ����Ʈ View �� ����
			mListView = (ExpandableListView) vv.findViewById(R.id.elv_list2);
			mListView.setAdapter(new BaseExpandableAdapter(vv.getContext(),
					mGroupList, mChildList, mDestList, mChildDestList, 1));

			
			mListView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView arg0, View vv,
						int gPosition, long arg3) {
					// TODO Auto-generated method stub
					

					String gName = null;
					
					
					if(gPosition > MainActivity.snapshotListInSrv.length ){ // gPosition��  snapshotListInSrv �̻��̸� Device Snapshot
						gName = MainActivity.snapshotListInDev[gPosition].getName(); // Click �� ����Ʈ�� ����.
					}else{
						gName = MainActivity.snapshotListInSrv[gPosition].getName(); // Click �� ����Ʈ�� ����.
					}
					
					Toast.makeText(vv.getContext(), gName,
							Toast.LENGTH_SHORT).show();
					
					// snapshot File �� lvm ���͸��� mount
					
					/*File f = new File("/sdcard/ssDir/"+gName);
					
					if(f.mkdirs())
					{
						Log.i("lvm","created");
					}else{
						Log.i("lvm","error!");
					}
					*/
					
					try {
						Process p =  Runtime.getRuntime().exec("su"); //  root ��
						p.getOutputStream().write("mount -t ext4 /dev/vg/userdata /sdcard/ssDir\n".getBytes());
						//p.getOutputStream().write(<my next command>);
						
						
						// root �������¿��� ls 
						p.getOutputStream().write("ls -l /sdcard/ssDir> /sdcard/tmp.txt\n".getBytes());
						
						
						// console ����
						p.getOutputStream().write("exit\n".getBytes());			
						p.getOutputStream().flush();
						try {
							p.waitFor();
							if (p.exitValue() != 255) {
								// TODO Code to run on success
								Toast.makeText(vv.getContext(), "root",
										Toast.LENGTH_SHORT).show();
							} else {
								// TODO Code to run on unsuccessful
								Toast.makeText(vv.getContext(),
										"not root", Toast.LENGTH_SHORT).show();
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Toast.makeText(vv.getContext(), "not root",
									Toast.LENGTH_SHORT).show();
						} 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e("lvm", "error : "+e.toString());
						e.printStackTrace();
					}
					
					return false;
				}
				
			});
			
			mListView.setOnChildClickListener(new OnChildClickListener(){

				@Override
				public boolean onChildClick(ExpandableListView arg0, View arg1,
						int arg2, int arg3, long arg4) {
					// TODO Auto-generated method stub
					return false;
				}
				
			});
			
			
			Toast.makeText(vv.getContext(), "Reading complete...",
					Toast.LENGTH_SHORT).show();
			dismissDialog(MainActivity.pd);
			
			break;
		case 101: // Clearing Snapshot List 
			mGroupList.clear();
			mChildList.clear();
			mDestList.clear();
			mChildDestList.clear();
			
			mListView = (ExpandableListView) vv.findViewById(R.id.elv_list2);
			mListView.setAdapter(new BaseExpandableAdapter(vv.getContext(),
					mGroupList, mChildList, mDestList, mChildDestList, 1));

			
			break;
		default:
			break;
		}
		
	}

	private void dismissDialog(ProgressDialog pd) {
		// TODO Auto-generated method stub
		pd.cancel();
	}
	
	
};
