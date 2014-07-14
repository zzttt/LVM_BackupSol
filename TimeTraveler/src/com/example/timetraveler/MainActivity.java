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
import java.util.ArrayList;

import net.kkangsworld.lvmexec.ResultReader;
import net.kkangsworld.lvmexec.pipeWithLVM;
import net.kkangsworld.lvmexec.readHandler;

import com.AuthcodeGen.CodeGenerator;
import com.FileManager.SnapshotDiskManager;
import com.Functions.ConnServer;
import com.Functions.opSwitch;

import android.R.color;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

	ConnectivityManager manager;

	NetworkInfo mobile;
	NetworkInfo wifi;
	WifiManager mng;
	WifiInfo info;

	public static String homePath = "/sdcard/DCIM/Camera/";
	public static PagerAdapterClass pac;

	public static boolean setVal0 = false; // auto snapshot On // Off
	public static int setVal1 = 0; // 백업 용량 세팅 값 1
	public static int setVal2 = 1; // 백업 용량 세팅 값 2

	public static File[] snapshotListInSrv;
	public static String[] snapshotInfoListInSrv;
	public static File[] snapshotListInDev;
	public static String[] snapshotInfoListInDev;
	//readHandler rh;
	//pipeWithLVM pl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		manager = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		mng = (WifiManager) getSystemService(WIFI_SERVICE);
		info = mng.getConnectionInfo();

		// Handler 세팅
		handler = new opHandler();

		// MAC 이용 인증코드 생성
		CodeGenerator cg = new CodeGenerator(info.getMacAddress());
		Toast.makeText(getApplication(), cg.genCode(), Toast.LENGTH_SHORT)
				.show();

		userCode = cg.genCode();

		
		// 모든 Snapshot List 를 Load (on Device & on Server)
		// Restore 에서 사용할 리스트를 로드함.

		// 1. Load Snapshot List on Device

		SnapshotDiskManager sdm = new SnapshotDiskManager(homePath);
		File[] sList = sdm.getSnapshotList();
		snapshotListInDev = sList;

		// 2. Load Server List on Server

		ConnServer conn = new ConnServer("211.189.19.45", 12345, 0, userCode,
				handler);
		conn.start();

		//

		// 하단 메뉴를 위한 Pager
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
			break;
		case R.id.btn_three:
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
		//btn_three.setOnClickListener(this);

		btn_three.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				readHandler rh = new readHandler();
				pipeWithLVM pl = new pipeWithLVM(rh);
				pl.ActionWritePipe("lvs");
				Toast.makeText(getApplicationContext(), rh.readResult(), Toast.LENGTH_SHORT).show();

			}
		});
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
	public class PagerAdapterClass extends PagerAdapter { // Page Adapter에서의 동작

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
			if (position == 0) { // Back up 페이지
				SimpleCursorAdapter mAdapter;
				v = mInflater.inflate(R.layout.inflate_one, null);

				mGroupList = new ArrayList<String>();
				mChildList = new ArrayList<ArrayList<String>>();
				mChildListContent = new ArrayList<ArrayList<String>>();
				mDestList = new ArrayList<String>();
				mChildDestList = new ArrayList<String>();

				mGroupList.add("현재시점을 서버에 백업");
				mGroupList.add("복원시점 생성");
				mGroupList.add("자동 복원시점 생성");

				ArrayList<String> child1 = new ArrayList<String>();
				ArrayList<String> child2 = new ArrayList<String>();
				ArrayList<String> child3 = new ArrayList<String>();

				child1.add("서버 백업");
				child1.add("외장 메모리 백업");

				child2.add("백업 시작");

				child3.add("자동 스냅샷 사용");

				mChildListContent.add(child1);
				mChildListContent.add(child2);
				mChildListContent.add(child3);

				mChildList.add(mChildListContent.get(0));
				mChildList.add(mChildListContent.get(1));
				mChildList.add(mChildListContent.get(2));

				mDestList.add("- 현재시점의 백업 데이터를 서버 또는 외장메모리에 저장합니다.");
				mDestList.add("- 현재상태를 복원시점으로 생성합니다.");
				mDestList.add("- 자동복원시점을 생성합니다.");

				mChildDestList.add("스냅샷 이미지를 서버에 전송합니다.");
				mChildDestList.add("연결된 SD카드에 스냅샷 이미지를 전송합니다.");

				mListView = (ExpandableListView) v.findViewById(R.id.elv_list1);
				mListView.setAdapter(new BaseExpandableAdapter(v.getContext(),
						mGroupList, mChildList, mDestList, mChildDestList, 0));

				// 그룹 클릭 했을 경우 이벤트
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

				// Backup 메뉴에서 차일드 클릭 했을 경우 이벤트
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
						case 0: // 현재 시점을 서버에 백업
							if (childPosition == 0) // Server Backup
							{
								Toast.makeText(getApplicationContext(),
										"MAC : " + info.getMacAddress(),
										Toast.LENGTH_SHORT).show();
								// check the wi-fi connection
								if (wifi.isConnected()) {
									// WIFI 에 만 연결 되었을때
									Toast.makeText(getApplicationContext(),
											"Wifi 연결 확인. 서버통신 시도.",
											Toast.LENGTH_SHORT).show();

									try {
										// 소켓 서버 접속
										// opcode == 1 ( Snapshot Server backup
										// )
										ConnServer cs = new ConnServer(
												"211.189.19.45", 12345, 1,
												userCode);

										cs.start();

									} catch (Exception e) {
										Toast.makeText(getApplicationContext(),
												"서버와의 연결을 실패했습니다.",
												Toast.LENGTH_SHORT).show();
									}

								} else {
									Toast.makeText(
											getApplicationContext(),
											"Server Backup은 Wifi 연결상태에서만 가능합니다.",
											Toast.LENGTH_SHORT).show();
								}

							} else { // 외장 메모리 Full Backup
								Toast.makeText(getApplicationContext(),
										"연결된 외장 메모리에 백업을 수행합니다.",
										Toast.LENGTH_SHORT).show();
							}

							break;
						case 1: // 복원 시점 생성
							// child menu 1개 이므로 바로 진행
							Toast.makeText(getApplicationContext(),
									"백업을 시작합니다.", Toast.LENGTH_SHORT).show();

							String line = "";
							StringBuffer output = new StringBuffer();

							try {
								ArrayList<String> command = new ArrayList<String>();
								command.add("su");
								command.add("|");
								command.add("./lvm");
								command.add("lvs");
								
								ProcessBuilder pb = new ProcessBuilder(command).directory(new File("/lvm"));
								
								pb.redirectErrorStream(true);
								
								Process p = pb.start();
								
								p.waitFor();
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(p
												.getInputStream()));
								output.append(command);
								output.append("\n");
								output.append("d");
								
								while ((line = reader.readLine()) != null) {
									output.append(line + "\n");
								}
								output.append("e");
								reader.close();
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(),  e.getMessage(),
										Toast.LENGTH_SHORT).show();
								Log.e("error", e.getMessage());
							}finally {
								Toast.makeText(getApplicationContext(), output,
										Toast.LENGTH_LONG).show();
							}

							// 백업 시작 process
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
									"자동 스냅샷이 설정되었습니다.", Toast.LENGTH_SHORT)
									.show();

							break;
						}
						return false;
					}
				});

				// 그룹이 닫힐 경우 이벤트
				mListView
						.setOnGroupCollapseListener(new OnGroupCollapseListener() {
							@Override
							public void onGroupCollapse(int groupPosition) {
								// Toast.makeText(getApplicationContext(),
								// "g Collapse = " + groupPosition,
								// Toast.LENGTH_SHORT).show();
							}
						});

				// 그룹이 열릴 경우 이벤트
				mListView.setOnGroupExpandListener(new OnGroupExpandListener() {
					@Override
					public void onGroupExpand(int groupPosition) {
						// Toast.makeText(getApplicationContext(), "g Expand = "
						// + groupPosition,
						// Toast.LENGTH_SHORT).show();
					}
				});

			} else if (position == 1) { // Restore 페이지

				SimpleCursorAdapter mAdapter;

				v = mInflater.inflate(R.layout.inflate_two, null);

				views.add(v); // Restore Page 만 컬렉션프레임워크에 넣어준다.

				/*
				 * mGroupList = new ArrayList<String>(); mChildList = new
				 * ArrayList<ArrayList<String>>(); mChildListContent = new
				 * ArrayList<ArrayList<String>>(); mDestList = new
				 * ArrayList<String>(); mChildDestList = new
				 * ArrayList<String>();
				 * 
				 * // Group List 가 입력된다 ( Snapshot list를 그룹단위로 보여줌 )
				 * mGroupList.add("2014-07-01 21:38 서버 백업");
				 * mGroupList.add("2014-07-02 23:38 서버 백업");
				 * mGroupList.add("2014-07-03 04:38 자동 백업");
				 * 
				 * for (int i = 0; i < mGroupList.size(); i++) {
				 * ArrayList<String> child1 = new ArrayList<String>();
				 * 
				 * child1.add("[최근 변경 사항 ]");
				 * 
				 * mChildListContent.add(child1); }
				 * 
				 * mChildList.add(mChildListContent.get(0));
				 * mChildList.add(mChildListContent.get(1));
				 * mChildList.add(mChildListContent.get(2));
				 * 
				 * mChildDestList.clear(); mChildDestList.add("없음");
				 * mChildDestList.add("없음"); mChildDestList.add("없음");
				 * 
				 * mListView = (ExpandableListView)
				 * v.findViewById(R.id.elv_list2); mListView.setAdapter(new
				 * BaseExpandableAdapter(v.getContext(), mGroupList, mChildList,
				 * mDestList, mChildDestList, 1));
				 * 
				 * // 그룹 클릭 했을 경우 이벤트 mListView.setOnGroupClickListener(new
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
				 * // 차일드 클릭 했을 경우 이벤트 mListView.setOnChildClickListener(new
				 * OnChildClickListener() {
				 * 
				 * @Override public boolean onChildClick(ExpandableListView
				 * parent, View v, int groupPosition, int childPosition, long
				 * id) {
				 * 
				 * return false; } });
				 * 
				 * // 그룹이 닫힐 경우 이벤트 mListView .setOnGroupCollapseListener(new
				 * OnGroupCollapseListener() {
				 * 
				 * @Override public void onGroupCollapse(int groupPosition) {
				 * 
				 * Toast.makeText(getApplicationContext(), "g Collapse = " +
				 * groupPosition, Toast.LENGTH_SHORT).show();
				 * 
				 * } });
				 * 
				 * // 그룹이 열릴 경우 이벤트 mListView.setOnGroupExpandListener(new
				 * OnGroupExpandListener() {
				 * 
				 * @Override public void onGroupExpand(int groupPosition) {
				 * 
				 * Toast.makeText(getApplicationContext(), "g Expand = " +
				 * groupPosition, Toast.LENGTH_SHORT).show();
				 * 
				 * } });
				 */
			} else { // // Setting View ( 세팅 페이지 )

				v = mInflater.inflate(R.layout.inflate_three, null);

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

				// 드롭다운 화면에 표시
				aa.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
				dateSpinner.setAdapter(aa);

				final CheckBox chkBox1 = (CheckBox) v
						.findViewById(R.id.checkbox_upToSrv);
				final CheckBox chkBox2 = (CheckBox) v
						.findViewById(R.id.checkbox_delSnapshot);
				final CheckBox chkBox3 = (CheckBox) v
						.findViewById(R.id.checkbox_delBackup);

				// 설정 페이지 체크박스 온클릭 리스너
				chkBox1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						chkBox2.setChecked(false);
						chkBox3.setChecked(false);
						setVal1 = 1;
					}

				});

				chkBox2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						chkBox1.setChecked(false);
						chkBox3.setChecked(false);
						setVal1 = 2;
					}

				});

				chkBox3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						chkBox1.setChecked(false);
						chkBox2.setChecked(false);
						setVal1 = 3;
					}

				});

				// Spinner 설정 값
				dateSpinner
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								setVal2 = Integer.parseInt(arg0
										.getItemAtPosition(arg2).toString());
								Log.i("eee", Integer.toString(setVal2));

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
		case 100:
			
			// pac 에서 View 를 읽어옴
			childList.add("[ No data ]");
			mChildDestList.add("변경된 항목이 없습니다.");
			
			for (int i = 0; i < MainActivity.snapshotListInSrv.length; i++) {
				mGroupList.add(MainActivity.snapshotListInSrv[i].getName());
				mChildList.add(childList);
			}
			for (int i = 0; i < MainActivity.snapshotListInDev.length; i++) {
				mGroupList.add(MainActivity.snapshotListInDev[i].getName());
				mChildList.add(childList);
			}

			
			// 리스트 View 에 적용
			
			mListView = (ExpandableListView) vv.findViewById(R.id.elv_list2);
			mListView.setAdapter(new BaseExpandableAdapter(vv.getContext(),
					mGroupList, mChildList, mDestList, mChildDestList, 1));

			Toast.makeText(vv.getContext(), "Reading complete...",
					Toast.LENGTH_SHORT).show();
			break;
		case 101:

			break;

		default:
			break;
		}

	}
};
