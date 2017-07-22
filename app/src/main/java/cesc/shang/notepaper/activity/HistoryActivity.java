package cesc.shang.notepaper.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import cesc.shang.baselib.base.activity.BaseActivity;
import cesc.shang.notepaper.R;
import cesc.shang.notepaper.adapter.HistoryRecyclerAdapter;
import cesc.shang.notepaper.adapter.HistoryRecyclerDecoration;
import cesc.shang.notepaper.entity.PictureEntity;
import cesc.shang.notepaper.manager.DBManager;
import cesc.shang.notepaper.pop.DeletePop;
import cesc.shang.utilslib.utils.util.ThreadUtils;

/**
 * Created by shanghaolongteng on 2016/9/6.
 */
public class HistoryActivity extends NoteBaseActivity {
    public static final String HISTORY_THREAD = "HistoryActivity";
    public static final int REQUEST_CODE = 116;
    public static final String DELETE_IDS_KEY = "DELETE_IDS_KEY";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private HistoryRecyclerAdapter mRecyclerAdapter = null;
    private Handler mWork;
    private DeletePop mDeletePop = null;
    private StringBuilder mDeleteBuilder = null;

    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, HistoryActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public int getContentViewId() {
        return R.layout.history_activity_layout;
    }

    @Override
    public void setupView() {
        super.setupView();

        int spanCount = getResources().getInteger(R.integer.history_recycler_span_count);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        int space = (int) getResources().getDimension(R.dimen.history_recycler_item_space);
        HistoryRecyclerDecoration decoration = new HistoryRecyclerDecoration(space);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    public void setAdapter() {
        mRecyclerAdapter = new HistoryRecyclerAdapter(this, mOnClickListener, mOnLongClickListener);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void initData() {
        mWork = getThreadUtils().getHandlerThread(HISTORY_THREAD);
        mWork.post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });
    }

    private ThreadUtils getThreadUtils() {
        return getUtilsManager().getThreadUtils();
    }

    private void getData() {
        final List<PictureEntity> list = getDBManager().selectAllPicture(HistoryActivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerAdapter.setList(list);
            }
        });
    }

    private DBManager getDBManager() {
        return getAppManager().getDBManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getThreadUtils().quitHandlerThread(mWork);
        if (mDeletePop != null)
            mDeletePop.release();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final PictureEntity pic;
            switch (v.getId()) {
                case R.id.note_view:
                    pic = (PictureEntity) v.getTag();
                    MainActivity.startActivity(HistoryActivity.this, pic);
                    finish();
                    break;
                case R.id.delete_button:
                    pic = (PictureEntity) v.getTag();
                    mWork.post(new Runnable() {
                        @Override
                        public void run() {
                            DBManager db = getDBManager();
                            db.delete(pic);
                            mDeleteBuilder.append(pic.getId());
                            mDeleteBuilder.append(",");
                            getData();
                        }
                    });
                case R.id.cancel_button:
                    mDeletePop.dismiss();
                    break;
            }
        }
    };

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.note_view:
                    if (mDeletePop == null) {
                        mDeletePop = new DeletePop(HistoryActivity.this);
                        mDeleteBuilder = new StringBuilder();
                    }
                    mDeletePop.setParam(mOnClickListener, v.getTag());
                    mDeletePop.show(mRecyclerView);
                    return true;
                default:
                    return false;
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDeletePop != null && mDeletePop.isShowing()) {
                mDeletePop.dismiss();
            } else {
                mWork.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDeleteBuilder != null && mDeleteBuilder.length() > 0) {
                            Intent intent = new Intent();
                            intent.putExtra(DELETE_IDS_KEY, mDeleteBuilder.toString());
                            setResult(RESULT_OK, intent);
                            mDeleteBuilder.setLength(0);
                        }
                        finish();
                    }
                });
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}