package p.mbt.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import p.chart.entity.LineEntity;
import p.chart.entity.OHLCEntity;
import p.chart.view.LineChart;
import p.mbt.R;
import p.mbt._class.DataManager;


public class ChartActivity extends Activity {
	
	LineChart machart;
	HorizontalScrollView  scroll;

	List<OHLCEntity> ohlc;
	
	private List<Float> Data1 = new ArrayList<Float>();
	private List<Float> Data2 = new ArrayList<Float>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		
		Intent intent = getIntent();
		String fileDate = intent.getStringExtra("filedate");
		
		//new LoadDataTask(fileDate).execute();
		Data1 = DataManager.loadData(fileDate , "1");
		Data2 = DataManager.loadData(fileDate , "2");
		LineChartInit();
		
		int focus = machart.getPointFocus(0);
		if(focus > 1080){
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(focus, ViewGroup.LayoutParams.MATCH_PARENT);
	        machart.setLayoutParams(params);
		}
		else{
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1080, ViewGroup.LayoutParams.MATCH_PARENT);
	        machart.setLayoutParams(params);
		}
		
		scroll.scrollTo(focus, 0);
		
	}
	
	
	
	private void LineChartInit(){
		
		scroll = (HorizontalScrollView)this.findViewById(R.id.scroll);
		this.machart = (LineChart)findViewById(R.id.machart);
			
			List<LineEntity> lines = new ArrayList<LineEntity>();
			
			//计算5日均线
	        LineEntity lineDeepth = new LineEntity();
	        lineDeepth.setTitle("temperature");
	        lineDeepth.setLineColor(Color.RED);
	        lineDeepth.setLineData(Data1);
	        lines.add(lineDeepth);
	        
	        //计算10日均线
	        LineEntity lineForce = new LineEntity();
	        lineForce.setTitle("humidity");
	        lineForce.setLineColor(Color.WHITE);
	        lineForce.setLineData(Data2);
	        lines.add(lineForce);
			
			List<String> ytitle=new ArrayList<String>();
			ytitle.add("0");
			ytitle.add("40");
			ytitle.add("80");
			ytitle.add("120");
			ytitle.add("160");
			List<String> xtitle=new ArrayList<String>();
			xtitle.add("0%");
			xtitle.add("10%");
			xtitle.add("20%");
			xtitle.add("30%");
			xtitle.add("40%");
			xtitle.add("50%");
			xtitle.add("60%");
			xtitle.add("70%");
			xtitle.add("80%");
			xtitle.add("90%");
			xtitle.add("100%");
	        
	        machart.setAxisXColor(Color.LTGRAY);
			machart.setAxisYColor(Color.LTGRAY);
			machart.setBorderColor(Color.LTGRAY);
			machart.setAxisMarginTop(10);
			machart.setAxisMarginLeft(20);
			machart.setAxisYTitles(ytitle);
			machart.setAxisXTitles(xtitle);
			machart.setLongitudeFontSize(10);
			machart.setLongitudeFontColor(Color.WHITE);
			machart.setLatitudeColor(Color.GRAY);
			machart.setLatitudeFontColor(Color.WHITE);
			machart.setLongitudeColor(Color.GRAY);
			machart.setMaxValue(160);
			machart.setMinValue(0);
			machart.setMaxPointNum(36);
			machart.setDisplayAxisXTitle(true);
			machart.setDisplayAxisYTitle(true);
			machart.setDisplayLatitude(true);
			machart.setDisplayLongitude(true);
		//	machart.getBackground().setAlpha(100);
	        
	        //为chart1增加均线
	        machart.setLineData(lines);
	        
	        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
	        machart.setLayoutParams(params);
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
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
