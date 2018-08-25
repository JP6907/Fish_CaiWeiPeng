/*
 * ITouchEventNotify.java
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

package p.chart.event;

import p.chart.view.GridChart;

/**
 * 
 * <p>Interface for chart which is support send notify after touch event happened</p>
 * <p>タッチイベントは通知可能のオブジェクトのインタフェー�?</p>
 * <p>touch事件发生后，支持对外发�?�事件消息的此类对象接口</p>
 *
 * @author limc 
 * @version v1.0 2013/05/30 17:57:32 
 * @see ITouchEventResponse
 */
public interface ITouchEventNotify {

	/**
	 * <p>Notify all ITouchEventResponse objects</p>
	 * <p>全部ITouchEventResponseレスポンスオブジェクトを通知</p>
	 * <p>通知全部ITouchEventResponse响应对象</p>
	 * @param chart
	 * <p>source chart</p>
	 * <p>ソースチャー�?</p>
	 * <p>源头对象</p>
	 */
	public void notifyEventAll(GridChart chart);
	
	/**
	 * <p>Add a ITouchEventResponse object by its index</p>
	 * <p>ITouchEventResponseレスポンスオブジェクトを追加</p>
	 * <p>增加ITouchEventResponse响应对象</p>
	 * @param notify
	 * <p>ITouchEventResponse object</p>
	 * <p>ITouchEventResponse オブジェクト</p>
	 * <p>对象</p>
	 */
	public void addNotify(ITouchEventResponse notify);
	
	/**
	 * <p>Remove a ITouchEventResponse object by its index</p>
	 * <p>ITouchEventResponseレスポンスオブジェクトを削除</p>
	 * <p>删除ITouchEventResponse响应对象</p>
	 * @param i
	 * <p>index</p>
	 * <p>インデックス</p>
	 * <p>index</p>
	 */
	public void removeNotify(int i);
	
	/**
	 * <p>Remove all ITouchEventResponse objects</p>
	 * <p>全部ITouchEventResponseレスポンスオブジェクトを削除</p>
	 * <p>删除全部ITouchEventResponse响应对象</p>
	 */
	public void removeAllNotify();
}
