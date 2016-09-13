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

