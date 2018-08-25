package p.mbt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import p.mbt.R;


public class BlueSearchActivity extends Activity {
	private int DSRESULT_CODE=1;
	
	private Handler _handler = new Handler();

	private ListView mylistview;
	
	Map<String, String> map = new HashMap<String, String>();
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	
	/* 取得默认的蓝牙适配器 */
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	/* 用来存储搜索到的蓝牙设备 */
	private List<BluetoothDevice> _devices = new ArrayList<BluetoothDevice>();
	/* 是否完成搜索 */
	private volatile boolean _discoveryFinished;
	private Runnable _discoveryWorkder = new Runnable() {
		public void run() 
		{
			/* 开始搜索 */
			_bluetooth.startDiscovery();
			for (;;) 
			{
				if (_discoveryFinished) 
				{
					break;
				}
				try 
				{
					Thread.sleep(100);
				} 
				catch (InterruptedException e){}
			}
		}
	};
	/**
	 * 接收器
	 */
	private BroadcastReceiver _foundReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			/* 从intent中取得搜索结果数据 */
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			
			if (_devices.indexOf(device) == -1){
				_devices.add(device); 
				} 
			
			showDevices();
		}
	};
	private BroadcastReceiver _discoveryReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) 
		{
			/* 卸载注册的接收器 */
			unregisterReceiver(_foundReceiver);
			unregisterReceiver(this);
			_discoveryFinished = true;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Transparent);    
		setContentView(R.layout.listview);
		
		
		/* 如果蓝牙适配器没有打开，则结果 */
		if (!_bluetooth.isEnabled())
		{

			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(intent);
		}
		/* 注册接收器 */
		IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(_discoveryReceiver, discoveryFilter);
		IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(_foundReceiver, foundFilter);
		
		SamplesUtils.indeterminate(BlueSearchActivity.this, _handler, "Scanning...", _discoveryWorkder, new OnDismissListener() {
			public void onDismiss(DialogInterface dialog)
			{

				for (; _bluetooth.isDiscovering();)
				{

					_bluetooth.cancelDiscovery();
				}

				_discoveryFinished = true;
			}
		}, true);
		
		
		
	}
	/* 显示列表 */
	protected void showDevices()
	{
		getData();

		mylistview = (ListView)findViewById(R.id.listview);	
		final SimpleAdapter myArrayAdapter = new SimpleAdapter(this,getData(),R.layout.items,
                new String[]{"name","mac"},
                new int[]{R.id.item1,R.id.item2});		

		_handler.post(new Runnable() {
			public void run()
			{

				mylistview.setAdapter(myArrayAdapter);
				mylistview.setOnItemClickListener(new OnItemClickListener(){
		            @Override
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                    long arg3) {
		                // TODO Auto-generated method stub
		                    Intent result = new Intent();
		                    final BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(list.get(arg2).get("mac"));
		                    result.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
		                    setResult(DSRESULT_CODE, result);
		                    finish();
		            }    
		        });
			}
		});
	}
	private List<Map<String, String>> getData() {
        
		for (int i = 0, size = _devices.size(); i < size; ++i)
		{
			Map<String, String> map1 = new HashMap<String, String>();		
			BluetoothDevice d = _devices.get(i);
			map1.put("name", d.getName());
            map1.put("mac", d.getAddress());
            if (list.indexOf(map1) == -1){
				list.add(map1); 
				} 
		}

        return list;

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blueactivity, menu);
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
