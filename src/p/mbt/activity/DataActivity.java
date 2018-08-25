package p.mbt.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import p.mbt.R;
import p.mbt.R.id;
import p.mbt.R.layout;
import p.mbt.R.menu;
import p.mbt._class.DataManager;

public class DataActivity extends Activity {

	private ListView datalist;
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	private boolean isStateOfChoice = false;

	private LinearLayout ly;

	LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

	private Button btnDelete;
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		datalist = (ListView) this.findViewById(R.id.listview);
		ly = (LinearLayout) this.findViewById(R.id.ly);

		btnDelete = new Button(this);
		btnDelete.setText("删除");
		btnDelete.setLayoutParams(p);

		btnBack = new Button(this);
		btnBack.setText("返回");
		btnBack.setLayoutParams(p);

		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isStateOfChoice = false;
				ly.removeAllViews();
				datalist.setAdapter(new SimpleAdapter(DataActivity.this, list, R.layout.items,
						new String[] { "name", "date" }, new int[] { R.id.item1, R.id.item2 }));
			}

		});

		getData();
		showData();
	}

	private void getData() {
		list.clear();
		File _path = new File("/sdcard/" + DataManager.FloderName);
		String[] fileNameArray = null;
		if (_path.exists()) {
			if (_path.isDirectory())
				fileNameArray = _path.list();
		} else {
			_path.mkdirs();// ����һ��Ŀ¼
			fileNameArray = _path.list();
		}
		for (int i = 0; i < fileNameArray.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			String[] subArray = null;
			subArray = fileNameArray[i].split("-|\\.");
			map.put("name", "温湿度数据");
			map.put("date", subArray[1].toString());
			if (!list.contains(map)) {
				list.add(map);
			}
		}

		Log.i("list", "" + list.size());
		for (int j = 0; j < list.size(); j++) {
			Log.i("list", j + ":" + list.get(j).toString());
		}
	}

	private void showData() {
		final SimpleAdapter AdapterChoice = new SimpleAdapter(this, list, R.layout.items_choice,
				new String[] { "name", "date" }, new int[] { R.id.item_choice1, R.id.item_choice2 });

		final SimpleAdapter Adapter = new SimpleAdapter(this, list, R.layout.items, new String[] { "name", "date" },
				new int[] { R.id.item1, R.id.item2 });

		final MyAdapter myAdapter = new MyAdapter(this, list);

		datalist.setAdapter(Adapter);
		datalist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!isStateOfChoice) {
					String name = list.get(position).get("name");
					String date = list.get(position).get("date");
					Log.i("name", "" + name);

					Intent intent = new Intent(DataActivity.this,ChartActivity.class);
					 intent.putExtra("userName", name);
					 intent.putExtra("filedate", date);
					 startActivity(intent);
				}
				if (isStateOfChoice) {
					myAdapter.notifyDataSetChanged();
					boolean selected = myAdapter.getIsSelected().get(position);
					myAdapter.getIsSelected().put(position, !selected);
					// myAdapter.getIsSelected().put(position, true);

				}
			}
		});

		datalist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				myAdapter.initCheckBox();
				myAdapter.getIsSelected().put(position, true);
				if (!isStateOfChoice) {
					isStateOfChoice = true;
					// datalist.setAdapter(AdapterChoice);
					datalist.setAdapter(myAdapter);
					ly.addView(btnBack);
					ly.addView(btnDelete);

					btnDelete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							for (int k = 0; k < list.size(); k++) {
								Log.i("list", "k = " + k);
								if (myAdapter.getIsSelected().get(k)) {
									Log.i("list", k + "is checked");
									// list.remove(k);
									// myAdapter.getIsSelected().remove(k);
									String userName = list.get(k).get("name");
									String fileDate = list.get(k).get("date");
									String fileName = userName + "-" + fileDate;
									File file = new File("/sdcard/himi" + "/" + fileName + ".txt");
									file.delete();

									myAdapter.getIsSelected().remove(k);
								}
							}
							getData();
							Log.i("adapter", "1");
							myAdapter.notifyDataSetChanged();
						}

					});
				}
				return true;
			}

		});

	}

	public class MyAdapter extends BaseAdapter {

		Context context;
		List<Map<String, String>> list;
		private HashMap<Integer, Boolean> isSelected; // ����CheckBoxѡ��״̬
		// private List<Integer> choicedItems; //���汻ѡ�е�Items
		LayoutInflater mInflater; // �õ�һ��LayoutInfalter�����������벼��

		public MyAdapter(Context context, List<Map<String, String>> list) {
			this.context = context;
			this.list = list;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// choicedItems = new ArrayList<Integer>();
			isSelected = new HashMap<Integer, Boolean>();
			initCheckBox();
		}

		public void initCheckBox() {
			for (int i = 0; i < list.size(); i++) {
				isSelected.put(i, false);
			}
		}

		public HashMap<Integer, Boolean> getIsSelected() {
			return isSelected;
		}

		public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
			this.isSelected = isSelected;
		}

		// public List<Integer> getChoicedItems(){
		// return choicedItems;
		// }
		@Override
		public int getCount() { // �ֻ���ĻҪ��ʾ��Item��Ŀ
			return list.size(); // �������鳤��
			// return 5; //���ˣ���������������������������������������
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) { // ����ʾ��item��position
			// ���з����仯��ʱ��getView�����ͻᱻ����
			ViewHolder viewHolder = null; // �ؼ������涨��
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.items_choice, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView.findViewById(R.id.item_choice1);
				viewHolder.date = (TextView) convertView.findViewById(R.id.item_choice2);
				viewHolder.select = (CheckBox) convertView.findViewById(R.id.item_cb);
				convertView.setTag(viewHolder); // ��ViewHolder����
												// ��view����Ӷ�����Ϣ
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.name.setText("" + list.get(position).get("name"));
			viewHolder.date.setText("" + list.get(position).get("date"));
			viewHolder.select.setChecked(isSelected.get(position));

			// if(isSelected.get(position) == true){
			//
			// }

			return convertView;
		}

		private class ViewHolder { /* ��ſؼ� */
			TextView name;
			TextView date;
			CheckBox select;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data, menu);
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

}
