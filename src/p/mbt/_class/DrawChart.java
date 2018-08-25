package p.mbt._class;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

public class DrawChart extends View {

	private Activity activity;

	private int chartH;
	private int chartW;
	private int layoutTop;
	private int layoutHeigth;
	private int layoutWidth;
	private int offsetLeft = 70;
	private int offestTop;
	private int textOffestLeft = 30;
	private int textOffestTop;
	private int xIntetval = 20; // 横坐标差
	private int screenWidth;
	private int screenHeigh;
	public int numX;
	
	private boolean isDrawLine1 = true;
	private boolean isDrawLine2 = true;

	private List<MyPoint> plist1;
	private MyPoint newPoint1 = new MyPoint(offsetLeft + chartW - 1, 0); // 赋初值！！！！！
	private float newDataConverted1; // 按照比例折算后
	
	private List<MyPoint> plist2;
	private MyPoint newPoint2 = new MyPoint(offsetLeft + chartW - 1, 0); // 赋初值！！！！！
	private float newDataConverted2; // 按照比例折算后
	
	public DrawChart(Context context) {
		super(context);
		activity = (Activity) context;
		plist1 = new ArrayList<MyPoint>();
		plist2 = new ArrayList<MyPoint>();
		getScreenData();
		// newPoint.x = 0; ////////////在这里赋值会闪退
		// newPoint.y = 0;
		// initPlist();

	}

	public void setDraw(boolean isDrawLine1, boolean isDrawLine2){
		this.isDrawLine1 = isDrawLine1;
		this.isDrawLine2 = isDrawLine2;
	}
	@Override
	protected void onDraw(Canvas canvas) { // invalidate()刷新后自动调用
		super.onDraw(canvas);
		drawTable(canvas);
		prepareLine1(); // 得到数据
		prepareLine2(); // 得到数据
		if(isDrawLine1)
			drawCurve1(canvas);
		if(isDrawLine2)
			drawCurve2(canvas);
	}

	public void getLayoutData(int top,int width,int heigth) {
		layoutTop = top;
		layoutWidth = width;
		layoutHeigth = heigth;
		offestTop = layoutTop + 80;
	//	chartW = layoutWidth;
	}
	
	private void getScreenData() {
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeigh = dm.heightPixels;

		chartH = 7 * screenHeigh / 10;
		chartW = 3 * screenWidth / 5 - 2 * offsetLeft - 10;

		textOffestTop = offestTop - 5;

		numX = chartW / xIntetval;
		/*
		 * activity.getWindowManager().getDefaultDisplay().getWidth(); height =
		 * activity.getWindowManager().getDefaultDisplay().getHeight();
		 */
	}

	private void drawTable(Canvas canvas) {

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		Paint paintRed = new Paint();
		paintRed.setColor(Color.RED);

		// 画外框
		paint.setStyle(Paint.Style.STROKE);
		// paint.setStrokeWidth(Pain);
		Rect chartRec = new Rect(offsetLeft, offestTop, chartW + offsetLeft, chartH + offestTop);
		canvas.drawRect(chartRec, paint);

		// 画左边的文字
		Path textPath = new Path();
		paint.setStyle(Paint.Style.FILL);
		textPath.moveTo(textOffestLeft, textOffestTop + 120);
		textPath.lineTo(textOffestLeft, textOffestTop);
		paint.setTextSize(20);
		paint.setAntiAlias(true);
		canvas.drawTextOnPath("Hello", textPath, 0, 0, paint); // 沿着Path路径写字

		// 画左侧数字
		canvas.drawText("160", offsetLeft - textOffestLeft - 2, offestTop + 5, paint);
		for (int i = 1; i < 10; i++) {
			canvas.drawText("" + 16 * (10 - i), offsetLeft - textOffestLeft - 2, offestTop + chartH / 10 * i,
					paint);
		}
		canvas.drawText("0", offsetLeft - textOffestLeft + 10, offestTop + chartH, paint);

		// 画表格中的虚线
		Path path = new Path();
		PathEffect effects = new DashPathEffect(new float[] { 4, 4, 4, 4 }, 1); // 虚线的轮廓
		// float数组中，2实线2虚线2实线2虚线 ，1为偏移量
		paintRed.setStyle(Paint.Style.STROKE);
		paintRed.setAntiAlias(false);
		paintRed.setPathEffect(effects);
		for (int i = 1; i < 10; i++) {
			path.moveTo(offsetLeft, offestTop + chartH / 10 * i);
			path.lineTo(offsetLeft + chartW, offestTop + chartH / 10 * i);
			canvas.drawPath(path, paintRed);
			// canvas.drawLine(OFFSET_LEFT, OFFSET_TOP + CHARTH/10*i,
			// OFFSET_LEFT+CHARTW, OFFSET_TOP + CHARTH/10*i, paint);

		}
	}

	private void drawCurve1(Canvas canvas) {
		Paint paintBlue = new Paint();
		paintBlue.setColor(Color.BLUE);
		paintBlue.setStrokeWidth(2);
		paintBlue.setAntiAlias(true);
		// canvas.drawLines(line, paint);

		if (plist1.size() >= 2) {
			for (int i = 0; i < plist1.size() - 1; i++) {
				canvas.drawLine(plist1.get(i).x, plist1.get(i).y, plist1.get(i + 1).x, plist1.get(i + 1).y, paintBlue);
			}
		}
	}
	private void drawCurve2(Canvas canvas) {
		Paint paintBlue = new Paint();
		paintBlue.setColor(Color.GREEN);
		paintBlue.setStrokeWidth(2);
		paintBlue.setAntiAlias(true);
		// canvas.drawLines(line, paint);

		if (plist2.size() >= 2) {
			for (int i = 0; i < plist2.size() - 1; i++) {
				canvas.drawLine(plist2.get(i).x, plist2.get(i).y, plist2.get(i + 1).x, plist2.get(i + 1).y, paintBlue);
			}
		}
	}

	private void prepareLine2() {
		if (plist2.size() > chartW / xIntetval + 1) {
			plist2.remove(0);
			for (int i = 0; i < chartW / xIntetval; i++) {
				if (i == 0)
					plist2.get(i).x -= (xIntetval - 2);
				else
					plist2.get(i).x -= xIntetval;
			}
			plist2.add(newPoint2);
		} else {
			for (int i = 0; i < plist2.size() - 1; i++) {
				plist2.get(i).x -= xIntetval;
			}
			plist2.add(newPoint2);
		}

	}
	private void convertData2(float d) { // 将数据按比例折算
		float maxData = 160;
		float maxPointY = chartH;
		newDataConverted2 = d * maxPointY / maxData;
	}
	public void getData2(float d) {
		convertData2(d);
		getPoint2();
	}
	private void getPoint2() {
		MyPoint p = new MyPoint(offsetLeft + chartW - 2, offestTop + chartH - newDataConverted2);
		newPoint2 = p;
	}
	
	private void prepareLine1() {
		if (plist1.size() > chartW / xIntetval + 1) {
			plist1.remove(0);
			for (int i = 0; i < chartW / xIntetval; i++) {
				if (i == 0)
					plist1.get(i).x -= (xIntetval - 2);
				else
					plist1.get(i).x -= xIntetval;
			}
			plist1.add(newPoint1);
		} else {
			for (int i = 0; i < plist1.size() - 1; i++) {
				plist1.get(i).x -= xIntetval;
			}
			plist1.add(newPoint1);
		}

	}
	private void convertData1(float d) { // 将数据按比例折算
		float maxData = 160;
		float maxPointY = chartH;
		newDataConverted1 = d * maxPointY / maxData;
	}
	public void getData1(float d) {
		convertData1(d);
		getPoint1();
	}
	private void getPoint1() {
		MyPoint p = new MyPoint(offsetLeft + chartW - 2, offestTop + chartH - newDataConverted1);
		newPoint1 = p;
	}

}
