# DavidToTopListView
【Android】Listview返回顶部，快速返回顶部的功能实现，详解代码。

作者：程序员小冰，GitHub主页：https://github.com/QQ986945193 

新浪微博：http://weibo.com/mcxiaobing 

首先给大家看一下我们今天这个最终实现的效果图： 

![这里写图片描述](http://img.blog.csdn.net/20160913110545220)

我这里只是单纯的实现了ListView返回顶部的功能。具体效果大家可以适当地美化 

在实际项目中可以换图标，去掉右侧滚动条等。具体ui美化不做解释。 

好了，首先我们是当不在顶部的时候，返回顶部按钮就会出现，而到顶部之后就会隐藏此按钮，

所以我们这里就要算Listview的滑动偏移量，当然，有这个返回顶部按钮，而且一直显示在底部，

所以当然用相对布局了。先给大家看一下xml布局源码比较简单：

```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white" >

    <ListView
        android:id="@+id/my_listView"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginRight="10dp" />

    <Button
        android:id="@+id/top_btn"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:visibility="gone"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_marginBottom="6dp"
        android:layout_marginRight="6dp"
        android:background="@mipmap/top_btn_bg"
        android:gravity="center"
        android:text="顶" />

</RelativeLayout>
```
然后我们需要一个获取屏幕的一个工具类，这里我封装好了，：
```
package davidtotoplistview.qq986945193.davidtotoplistview.davidtotoplistview;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * @author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 * @CSDN博客: http://blog.csdn.net/qq_21376985
 * @码云OsChina ：http://git.oschina.net/MCXIAOBING
 */
public class ScreenUtil {

	/**
	 * 获取屏幕的宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		
		return display.getWidth();
	}

	/**
	 * 获取屏幕的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 获取屏幕中控件顶部位置的高度--即控件顶部的Y点
	 * 
	 * @return
	 */
	public static int getScreenViewTopHeight(View view) {
		return view.getTop();
	}

	/**
	 * 获取屏幕中控件底部位置的高度--即控件底部的Y点
	 * 
	 * @return
	 */
	public static int getScreenViewBottomHeight(View view) {
		return view.getBottom();
	}

	/**
	 * 获取屏幕中控件左侧的位置--即控件左侧的X点
	 * 
	 * @return
	 */
	public static int getScreenViewLeftHeight(View view) {
		return view.getLeft();
	}

	/**
	 * 获取屏幕中控件右侧的位置--即控件右侧的X点
	 * 
	 * @return
	 */
	public static int getScreenViewRightHeight(View view) {
		return view.getRight();
	}

}
```
好了，然后我们需要一个adapter，就是现实列表的一个简单的textview
```
package davidtotoplistview.qq986945193.davidtotoplistview.davidtotoplistview;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 * @CSDN博客: http://blog.csdn.net/qq_21376985
 * @码云OsChina ：http://git.oschina.net/MCXIAOBING
 *
 * listview所需要的adapter
 */
public class MyAdapter extends BaseAdapter {

    private List<String> mTitleArray;// 标题数组
    private LayoutInflater inflater;

    /**
     * 构造方法
     *
     * @param context    // 上下文对象
     * @param titleArray // 标题数组
     */
    public MyAdapter(Context context, List<String> titleArray) {
        this.mTitleArray = titleArray;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 获取Item总数
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mTitleArray != null) {
            return mTitleArray.size();
        } else {
            return 0;
        }
    }

    /**
     * 获取一个Item对象
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (mTitleArray != null) {
            return mTitleArray.get(position);
        } else {
            return null;
        }
    }

    /**
     * 获取指定item的ID
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_layout, null);
            holder.mTitleTv = (TextView) convertView
                    .findViewById(R.id.title_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitleTv.setText(mTitleArray.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView mTitleTv;
    }

}
```

ok，现在基本上工具都做好了，开始实现我们的重要的部分，实现返回顶部：

```
package davidtotoplistview.qq986945193.davidtotoplistview.davidtotoplistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 * @CSDN博客: http://blog.csdn.net/qq_21376985
 * @码云OsChina ：http://git.oschina.net/MCXIAOBING
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView myListView;
    private Button topBtn;

    private MyAdapter adapter;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myListView = (ListView) findViewById(R.id.my_listView);
        topBtn = (Button) findViewById(R.id.top_btn);

        adapter = new MyAdapter(this, getTitleDatas());
        myListView.setAdapter(adapter);

        topBtn.setOnClickListener(this);
        myListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (myListView.getLastVisiblePosition() == (myListView
                                .getCount() - 1)) {
                            topBtn.setVisibility(View.VISIBLE);
                        }
                        // 判断滚动到顶部
                        if (myListView.getFirstVisiblePosition() == 0) {
                            topBtn.setVisibility(View.GONE);
                        }

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = false;
                        break;
                }
            }

            /**
             * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
             * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
             * CSDN博客: http://blog.csdn.net/qq_21376985
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag
                        && ScreenUtil.getScreenViewBottomHeight(myListView) >= ScreenUtil
                        .getScreenHeight(MainActivity.this)) {
                    if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                        topBtn.setVisibility(View.VISIBLE);
                    } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                        topBtn.setVisibility(View.GONE);
                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }
        });
    }

    /**
     * 获取标题数据列表
     *
     * @return
     */
    private List<String> getTitleDatas() {
        List<String> titleArray = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            titleArray.add("程序员小冰" + i + "个item");
        }
        return titleArray;
    }

    /**
     * 滚动ListView到指定位置
     *
     * @param pos
     */
    private void setListViewPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            myListView.smoothScrollToPosition(pos);
        } else {
            myListView.setSelection(pos);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.top_btn:// 点击按钮返回到ListView的第一项
                setListViewPos(0);
                break;
        }
    }

}


```
最后直接运行即可看到上面的效果。如果对您有帮助，欢迎star，fork。。。

姊妹篇：Scrollview返回顶部，快速返回顶部的功能实现详解链接：

http://blog.csdn.net/qq_21376985/article/details/52511303
