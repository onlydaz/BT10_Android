package vn.iotstar.apiviewpager2;

import android.content.Context;
import android.content.Intent;
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

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {
    private Context context;
    private List<VideoModel> videoList;

    public VideosAdapter(Context context, List<VideoModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoModel videoModel = videoList.get(position);

        holder.textVideoTitle.setText(videoModel.getTitle());
        holder.textVideoDescription.setText(videoModel.getDescription());

        String videoUrl = videoModel.getUrl();

        if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be")) {
            // YouTube link
            holder.videoView.setVisibility(View.GONE); // ẩn VideoView
            holder.videoProgressBar.setVisibility(View.GONE);

            // Khởi tạo YouTubePlayerView và phát video YouTube
            holder.youtubePlayerView.setVisibility(View.VISIBLE);
            String videoId = getVideoIdFromUrl(videoUrl); // Phương thức để lấy video ID từ URL YouTube
            if (videoId != null) {
                holder.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0); // Load video từ video ID
                        youTubePlayer.play(); // Bắt đầu phát video
                    }
                });
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                intent.setPackage("com.google.android.youtube"); // nếu có app YouTube
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    // Nếu không có app YouTube, dùng trình duyệt
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)));
                }
            });
        } else {
            // File .mp4: phát bằng VideoView
            holder.videoView.setVisibility(View.VISIBLE);
            Uri videoUri = Uri.parse(videoUrl);
            holder.videoView.setVideoURI(videoUri);
            holder.videoProgressBar.setVisibility(View.VISIBLE);

            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(holder.videoView);
            holder.videoView.setMediaController(mediaController);

            holder.videoView.setOnPreparedListener(mp -> {
                holder.videoProgressBar.setVisibility(View.GONE);
                mp.setLooping(true);
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

            holder.videoView.setOnErrorListener((mp, what, extra) -> {
                holder.videoProgressBar.setVisibility(View.GONE);
                return true;
            });

            holder.itemView.setOnClickListener(null); // bỏ click mở YouTube
        }

        holder.favorites.setOnClickListener(v -> {
            boolean isFav = v.getTag() != null && (boolean) v.getTag();
            if (!isFav) {
                holder.favorites.setImageResource(R.drawable.ic_fill_favorite);
                v.setTag(true);
            } else {
                holder.favorites.setImageResource(R.drawable.ic_favorite);
                v.setTag(false);
            }
        });
    }


    // Phương thức để lấy video ID từ URL YouTube
    private String getVideoIdFromUrl(String url) {
        String videoId = null;
        if (url.contains("youtube.com")) {
            String[] urlParts = url.split("v=");
            if (urlParts.length > 1) {
                videoId = urlParts[1].split("&")[0]; // Lấy phần video ID từ URL
            }
        } else if (url.contains("youtu.be")) {
            videoId = url.substring(url.lastIndexOf("/") + 1); // Lấy video ID từ link rút gọn
        }
        return videoId;
    }

    @Override
    public int getItemCount() {
        return videoList != null ? videoList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView;
        public ProgressBar videoProgressBar;
        public TextView textVideoTitle, textVideoDescription;
        public ImageView imPerson, favorites, imShare, imMore;
        public YouTubePlayerView youtubePlayerView;

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
            youtubePlayerView = itemView.findViewById(R.id.youtubePlayerView);
        }
    }
}
