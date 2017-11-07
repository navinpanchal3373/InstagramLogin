package com.example.dell.instagramlogin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.instagramlogin.R;
import com.example.dell.instagramlogin.model.instamedia.ModelInstaMedia;
import com.example.dell.instagramlogin.myinterface.RecyclerViewClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by navinpanchal3373@gmail.com on 11/4/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<ModelInstaMedia> modelInstaMedias;
    private Context context;
    private DisplayImageOptions options;
    private RecyclerViewClickListener mListener;

    public DataAdapter(Context context, List<ModelInstaMedia> modelInstaMedias, RecyclerViewClickListener mListener) {
        this.modelInstaMedias = modelInstaMedias;
        this.context = context;
        this.mListener = mListener;

//        this.options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.ic_launcher)
//                .showImageForEmptyUri(R.mipmap.ic_launcher)
//                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory(true)
//                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        if (modelInstaMedias.get(i).getCaption() != null && modelInstaMedias.get(i).getCaption().getText() != null)
            viewHolder.tv_android.setText(modelInstaMedias.get(i).getCaption().getText());

        if (modelInstaMedias.get(i).getImages() != null && modelInstaMedias.get(i).getImages().getStandardResolution() != null)
            ImageLoader.getInstance().displayImage(modelInstaMedias.get(i).getImages().getStandardResolution().getUrl(),
                    viewHolder.img_android, options);

        if (modelInstaMedias.get(i).getLocation() != null && modelInstaMedias.get(i).getLocation().toString().length() > 0)
            viewHolder.img_mapion.setVisibility(View.VISIBLE);
        else
            viewHolder.img_mapion.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return modelInstaMedias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private ImageView img_android, img_mapion;

        public ViewHolder(View view) {
            super(view);
            tv_android = (TextView) view.findViewById(R.id.tv_android);
            img_android = (ImageView) view.findViewById(R.id.img_android);
            img_mapion = (ImageView) view.findViewById(R.id.img_mapion);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v, getAdapterPosition());
                }
            });
        }
    }

}