package com.example.frontend.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend.R;
import com.example.frontend.activities.DetailStoryActivity;
import com.example.frontend.activities.MainActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SeekBarAdapter extends RecyclerView.Adapter<SeekBarAdapter.ViewHolder> {
    private Context mContext;
    private List<Object> seekBarList;
    private static final long TOTAL_TIME = TimeUnit.SECONDS.toMillis(5);
    private int finishedCount = 0;
    private int currentPotion = 0;

    public SeekBarAdapter(Context context, List<Object> seekBarList) {
        this.mContext = context;
        this.seekBarList = seekBarList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_seekbar_bar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int screenWidthDp = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / Resources.getSystem().getDisplayMetrics().density);
        int progressBarWidthDp = screenWidthDp / seekBarList.size();
        float progressBarWidthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, progressBarWidthDp, mContext.getResources().getDisplayMetrics());
        ViewGroup.LayoutParams layoutParams = holder.seek_bar_story.getLayoutParams();
        layoutParams.width = (int) progressBarWidthPx;
        holder.seek_bar_story.setLayoutParams(layoutParams);
        if (position == currentPotion) {
            runSeekBar(holder.seek_bar_story, position);
        } else {
            holder.seek_bar_story.setProgress(0);
        }
    }

    @Override
    public int getItemCount() {
        return seekBarList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SeekBar seek_bar_story;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seek_bar_story = itemView.findViewById(R.id.seek_bar_story);
        }
    }

    private void runSeekBar(final SeekBar seekBar, final int position) {
        seekBar.setMax(100);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                final long startTime = System.currentTimeMillis();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        float progress = (float) elapsedTime / TOTAL_TIME * 100;
                        seekBar.setProgress((int) progress);
                        if (progress < 100) {
                            handler.postDelayed(this, 16); // Cập nhật seekBar mỗi 16ms
                        } else {
                            // Chuyển đến item tiếp theo nếu có
                            int nextPosition = position + 1;
                            finishedCount++;
                            if (nextPosition < seekBarList.size()) {
                                // Nếu còn item trong danh sách, chạy seekBar của item tiếp theo
                                currentPotion = nextPosition;
                                notifyItemChanged(currentPotion);
                            } else {
                                // Nếu đã chạy xong item cuối cùng, gọi phương thức finish()
                                if (finishedCount == seekBarList.size()) {
                                    ((DetailStoryActivity) mContext).finish();
                                }
                                Log.e("sisis",String.valueOf(finishedCount) + " and " + String.valueOf(seekBarList.size()));
                            }
                        }
                    }
                }, 0); // Bắt đầu chạy seekBar từ vị trí 0
            }
        });
    }
}
