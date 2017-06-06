package com.andlib.lp.adapter;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;//hash map
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//支持泛型，重写的时候T替换成list里的对象
public abstract class ListBaseAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mList;
	private LayoutInflater mInflater;

	public ListBaseAdapter(Context context, List<T> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(context);
	}

	public List<T> getList() {
		return mList;
	}
	public LayoutInflater getInflater() {
		return mInflater;
	}
	public Context getContext() {
		return mContext;
	}
	@Override public int getCount() {  return mList.size(); }
	@Override public T getItem(int position) { return mList.get(position); }
	@Override public long getItemId(int position) { return position; }
	@Override public View getView(int position, View convertView, ViewGroup parent) {
		return initView(position, convertView, parent);
	}

	public abstract View initView(int position, View convertView,ViewGroup parent);
	public void startActivity(Intent intent) {
		getContext().startActivity(intent);
	}
	public void addItem(T item) {
		mList.add(item);
		notifyDataSetChanged();
	}             //添加单个item
	public void addItem(T item, int postion) {
		mList.add(postion, item);
		notifyDataSetChanged();
	}//在指定位置添加单个item
	public void removeItem(T item) {
		mList.remove(item);
		notifyDataSetChanged();
	}          //删除单个item
	public void removeItem(int position) {
		if (position > getCount()) {
			throw new IndexOutOfBoundsException("postion大于列表的size 了");
		}
		mList.remove(position);
		notifyDataSetChanged();
	}    //删除指定位置item

	public static class ViewHolder {
		public static <T extends View> T get(View view, int id) {

			SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

			if (viewHolder == null) {
				viewHolder = new SparseArray<View>();
				view.setTag(viewHolder);
			}

			View childView = viewHolder.get(id);

			if (childView == null) {
				childView = view.findViewById(id);
				viewHolder.put(id, childView);
			}

			return (T) childView;
		}
	}
}
