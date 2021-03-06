package com.app.wptools.StoryDownloader.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.app.wptools.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class RecyclerViewMediaAdapter extends RecyclerView.Adapter<RecyclerViewMediaAdapter.FileHolder> implements EasyVideoCallback {

    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/storage/emulated/legacy/WSDownloader/";
    private ArrayList<File> filesList;
    private Activity activity;

    public RecyclerViewMediaAdapter(ArrayList<File> filesList, Activity activity) {
        this.filesList = filesList;
        this.activity = activity;
    }

    @Override
    public RecyclerViewMediaAdapter.FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_media_row_item, parent, false);
        return new FileHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewMediaAdapter.FileHolder holder, int position) {
        File currentFile = filesList.get(position);

        holder.buttonImageDownload.setOnClickListener(this.downloadMediaItem(currentFile));
        holder.buttonVideoDownload.setOnClickListener(this.downloadMediaItem(currentFile));

        holder.player.setCallback(this);

        if (currentFile.getAbsolutePath().endsWith(".mp4")) {
            holder.cardViewImageMedia.setVisibility(View.GONE);
            holder.cardViewVideoMedia.setVisibility(View.VISIBLE);
            Uri video = Uri.parse(currentFile.getAbsolutePath());
            holder.player.setSource(video);
//            holder.videoViewVideoMedia.setVideoURI(video);
//            holder.videoViewVideoMedia.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setLooping(true);
//                    holder.videoViewVideoMedia.start();
//                }
//            });
        } else {
            Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            holder.imageViewImageMedia.setImageBitmap(myBitmap);
        }

    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public View.OnClickListener downloadMediaItem(final File sourceFile) {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Runnable(){

                    @Override
                    public void run() {
                        try {
                            //String DIRECTORY_TO_SAVE_MEDIA_NOW = "/storage/emulated/legacy/"+ Resources.getSystem().getString(R.string.app_name)+"/";

                            Log.d("Hello", "onClick:  "+DIRECTORY_TO_SAVE_MEDIA_NOW);

                            copyFile(sourceFile, new File(DIRECTORY_TO_SAVE_MEDIA_NOW +sourceFile.getName()));
//                            Snacky.builder().
//                                    setActivty(activity).
//                                    setText(R.string.save_successful_message).
//                                    success().
//                                    show();//
                            Toast.makeText(activity, R.string.save_successful_message, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("RecyclerV", "onClick: Error:"+e.getMessage() );

//                            Snacky.builder().
//                                    setActivty(activity).
//                                    setText(R.string.save_error_message).
//                                    error().
//                                    show();
                            Toast.makeText(activity, R.string.save_error_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }.run();
            }
        };
    }

    /**
     * copy file to destination.
     *
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }


    public static class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewImageMedia;
        VideoView videoViewVideoMedia;
        CardView cardViewVideoMedia;
        CardView cardViewImageMedia;
        Button buttonVideoDownload;
        Button buttonImageDownload;
        EasyVideoPlayer player;

        public FileHolder(View itemView) {
            super(itemView);
            imageViewImageMedia = (ImageView) itemView.findViewById(R.id.imageViewImageMedia);
//            videoViewVideoMedia = (VideoView) itemView.findViewById(R.id.videoViewVideoMedia);
            cardViewVideoMedia = (CardView) itemView.findViewById(R.id.cardViewVideoMedia);
            cardViewImageMedia = (CardView) itemView.findViewById(R.id.cardViewImageMedia);
            buttonImageDownload = (Button) itemView.findViewById(R.id.buttonImageDownload);
            buttonVideoDownload = (Button) itemView.findViewById(R.id.buttonVideoDownload);
            player = (EasyVideoPlayer) itemView.findViewById(R.id.player);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

    @Override
    public void onViewDetachedFromWindow(FileHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.player.pause();
    }
}
