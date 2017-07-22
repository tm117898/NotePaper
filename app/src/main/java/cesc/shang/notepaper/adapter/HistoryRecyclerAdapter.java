package cesc.shang.notepaper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cesc.shang.baselib.base.adapter.RecyclerViewBaseAdapter;
import cesc.shang.baselib.support.BaseContextSupport;
import cesc.shang.notepaper.R;
import cesc.shang.notepaper.entity.PictureEntity;
import cesc.shang.notepaper.view.NoteView;

/**
 * Created by shanghaolongteng on 2016/9/7.
 */
public class HistoryRecyclerAdapter extends RecyclerViewBaseAdapter<PictureEntity, HistoryRecyclerAdapter.ViewHolder> {
    private View.OnClickListener mItemClickListener = null;
    private View.OnLongClickListener mOnLongClickListener = null;

    public HistoryRecyclerAdapter(BaseContextSupport support, View.OnClickListener clickListener, View.OnLongClickListener longClickListener) {
        super(support);
        mItemClickListener = clickListener;
        mOnLongClickListener = longClickListener;
    }

    @Override
    public int getViewLayoutId(int i) {
        return R.layout.note_view;
    }

    @Override
    public ViewHolder getViewHolder(View view, int i) {
        ViewHolder vh = new ViewHolder(view);
        vh.mNoteView.setEnableTouch(false);
        vh.mNoteView.setOnClickListener(mItemClickListener);
        vh.mNoteView.setOnLongClickListener(mOnLongClickListener);
        return vh;
    }

    @Override
    public void bindData(PictureEntity pictureEntity, ViewHolder viewHolder, int i) {
        viewHolder.mNoteView.setPicture(pictureEntity);
        viewHolder.mNoteView.setTag(pictureEntity);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public NoteView mNoteView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNoteView = itemView.findViewById(R.id.note_view);
        }
    }
}
