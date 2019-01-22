package detailBase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import bean.ResponsePhotoData.DataBean.NewsBean;
import com.bumptech.glide.Glide;
import com.scy.android.smartcity.R;

import net.ApiUtils;

import java.util.List;

import bean.ResponsePhotoData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 沈程阳
 * created by scy on 2019/1/16 22:25
 * 邮箱：1797484636@qq.com
 */
public class PhotosMenuDetailpager extends BaseMenuDetailPager implements View.OnClickListener {
    private ResponsePhotoData mResponsePhotoData;
    private ListView mListView;
    private RecyclerView mRecyclerView;
    private List<ResponsePhotoData.DataBean.NewsBean> photos;
    private ImageButton picc;

    public PhotosMenuDetailpager(Activity activity, ImageButton picc) {
        super(activity);
        this.picc = picc;
        picc.setOnClickListener(this);
    }

    private boolean isListView = true;
    @Override
    public void onClick(View v) {
        if (isListView) {
            mListView.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            picc.setImageResource(R.drawable.ic_dns_black_24dp);
            isListView = false;
        } else {
            mListView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            picc.setImageResource(R.drawable.ic_dashboard_black_24dp);


            isListView = true;
        }
    }

    @Override
    public View initViews() {
        /*TextView textView = new TextView(mActivity);
        textView.setText("组图");
        textView.setGravity(Gravity.CENTER);
        return textView;*/

        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
        mRecyclerView = view.findViewById(R.id.photo_rv);
        mListView = view.findViewById(R.id.photo_lv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,2));
        return view;
    }

    @Override
    public void initData() {
        getDataFromerver();
    }

    private void getDataFromerver() {
        ApiUtils.getPhotoService().getPhotoAnswers().enqueue(new Callback<ResponsePhotoData>() {
            @Override
            public void onResponse(Call<ResponsePhotoData> call, Response<ResponsePhotoData> response) {
                mResponsePhotoData = response.body();
                setData();
            }

            @Override
            public void onFailure(Call<ResponsePhotoData> call, Throwable t) {

            }
        });
    }

    private void setData() {
         photos = mResponsePhotoData.getData().getNews();
         mListView.setAdapter(new PhotoAdapter());
         mRecyclerView.setAdapter(new ReAdapter());
    }

    class PhotoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public NewsBean getItem(int position) {
            return (NewsBean)photos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_photo_list, null);
                holder = new ViewHolder();
                holder.mTextView = convertView.findViewById(R.id.cardview_title);
                holder.mImageView = convertView.findViewById(R.id.cardview_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.mTextView.setText(getItem(position).getTitle());
            Glide.with(mActivity).load(getItem(position).getListimage()).into(holder.mImageView);

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView mImageView;
        TextView mTextView;
    }

    class ReAdapter extends RecyclerView.Adapter<Vh> {
        @NonNull
        @Override
        public Vh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(mActivity).inflate(R.layout.item_photo_list, viewGroup,false);
            return new Vh(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Vh vh, int i) {
            vh.bind(photos.get(i));
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        public Vh(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.cardview_title);
            mImageView = itemView.findViewById(R.id.cardview_image);
        }

        public void bind(NewsBean bean) {
            mTextView.setText(bean.getTitle());
            Glide.with(mActivity).load(bean.getListimage()).into(mImageView);
        }
    }


}
