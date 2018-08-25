/*
 * LineEntity.java
 * Android-Charts
 *
 * Created by limc on 2011/05/29.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package p.chart.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Entity data which is use for display a single line in LineChart</p>
 * <p>LineChartの線表示用データです。単線です�??</p>
 * <p>保存线图表示用单个线的对象�?�多条线的时候请使用相应的数据结构保存数�?</p>
 * @author limc 
 * @version v1.0 2011/05/29 12:24:49 
 */
public class LineEntity {
	
	/** 
	 * <p>Data for draw this line</p>
	 * <p>ラインを表示用データ</p>
	 * <p>线表示数�?</p>
	 */
	private List<Float> lineData;
	
	/**
	 * <p>Title for this line</p>
	 * <p>ラインの表示タイトル</p>
	 * <p>线的标题，用于标识别这条�?</p>
	 */
	private String title;
	
	/**
	 * <p>Line Color</p>
	 * <p>ラインの�?</p>
	 * <p>线的颜色</p>
	 */
	private int lineColor;
	
	/** 
	 * <p>Should display this line?</p>
	 * <p>ラインをチャードで表面で表示する�??</p>
	 * <p>是否在图表上显示该线</p>
	 */
	private boolean display = true;
	
	/**
	 * <p>Constructor of LineEntity</p>
	 * <p>LineEntity类对象的构�?�函�?</p>
	 * <p>LineEntityのコンストラクタ�?</p>
	 *
	 */
	public LineEntity() {
		super();
	}

	/**
	 * <p>Constructor of LineEntity</p>
	 * <p>LineEntity类对象的构�?�函�?</p>
	 * <p>LineEntityのコンストラクタ�?</p>
	 *
	 * @param lineData
	 * <p>Data for draw this line</p>
	 * <p>ラインを表示用データ</p>
	 * <p>线表示数�?</p>
	 * @param title
	 * <p>Title for this line</p>
	 * <p>ラインの表示タイトル</p>
	 * <p>线的标题，用于标识别这条�?</p>
	 * @param lineColor 
	 * <p>Line Color</p>
	 * <p>ラインの�?</p>
	 * <p>线的颜色</p>
	 */
	public LineEntity(List<Float> lineData, String title, int lineColor) {
		super();
		this.lineData = lineData;
		this.title = title;
		this.lineColor = lineColor;
	}
	
	/**
	 * @param value 
	 */
	public void put(float value){
		if (null == lineData){
			lineData = new ArrayList<Float>();
		}
		lineData.add(value);
	}

	/**
	 * @return the lineData
	 */
	public List<Float> getLineData() {
		return lineData;
	}

	/**
	 */
	public void setLineData(List<Float> lineData) {
		this.lineData = lineData;
	}
	
	public void addLineData(float data){
		this.lineData.add(data);
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the lineColor
	 */
	public int getLineColor() {
		return lineColor;
	}

	/**
	 * @param lineColor the lineColor to set
	 */
	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * @return the display
	 */
	public boolean isDisplay() {
		return display;
	}

	/**
	 * @param display the display to set
	 */
	public void setDisplay(boolean display) {
		this.display = display;
	}	
}
