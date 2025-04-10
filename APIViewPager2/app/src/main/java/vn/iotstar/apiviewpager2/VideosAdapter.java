package vn.iotstar.apiviewpager2;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {
    private Context context;
    private List<VideoModel> videoList;
    private boolean isFav = false;

    public VideosAdapter(Context context, List<VideoModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video_row, parent, false); // ⚠️ Fix cú pháp attachToRoot
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoModel videoModel = videoList.get(position);

        holder.textVideoTitle.setText(videoModel.getTitle());
        holder.textVideoDescription.setText(videoModel.getDescription());

        holder.videoView.setVideoURI(Uri.parse(videoModel.getUrl()));

        holder.videoView.setOnPreparedListener(mp -> {
            holder.videoProgressBar.setVisibility(View.GONE);
            mp.start();

            float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
            float screenRatio = holder.videoView.getWidth() / (float) holder.videoView.getHeight();
            float scale = videoRatio / screenRatio;

            if (scale >= 1f) {
                holder.videoView.setScaleX(scale);
            } else {
                holder.videoView.setScaleY(1f / scale);
            }
        });

        holder.videoView.setOnCompletionListener(mp -> {
            holder.videoView.setMediaController(new MediaController(context));
            holder.videoView.requestFocus();
            mp.start(); // Auto replay
        });

        holder.favorites.setOnClickListener(v -> {
            if (!isFav) {
                holder.favorites.setImageResource(R.drawable.ic_fill_favorite);
                isFav = true;
            } else {
                holder.favorites.setImageResource(R.drawable.ic_favorite);
                isFav = false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList != null ? videoList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private VideoView videoView;
        private ProgressBar videoProgressBar;
        private TextView textVideoTitle;
        private TextView textVideoDescription;
        private ImageView imPerson, favorites, imShare, imMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            videoProgressBar = itemView.findViewById(R.id.videoProgressBar);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            imPerson = itemView.findViewById(R.id.imPerson);
            favorites = itemView.findViewById(R.id.favorites);
            imShare = itemView.findViewById(R.id.imShare);
            imMore = itemView.findViewById(R.id.imMore);
        }
    }
}
