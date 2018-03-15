package com.yizhilu.community.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.MessageEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.CircleImageView;
import com.yizhilu.view.NoScrollListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//话题评论适配器
public class TopicCommentAdapter extends BaseAdapter {

	private Context context;
	private List<EntityPublic> commentDtoList;
	private HolderView holderView = null;
	private ImageLoader imageLoader;
	private AsyncHttpClient httpClient;// 网络连接
	private boolean isload = true, isSize = true;
	private List<Boolean> booleans;
	private int size;

	public TopicCommentAdapter(Context context, List<EntityPublic> commentDtoList) {
		super();
		this.context = context;
		this.commentDtoList = commentDtoList;
		httpClient = new AsyncHttpClient();
		imageLoader = ImageLoader.getInstance();
		booleans = new ArrayList<Boolean>();
	}

	@Override
	public int getCount() {
		Log.i("xm", "count");
		if (isSize) {
			size = commentDtoList.size();
			isSize = false;
		}
		if (size != commentDtoList.size()) {
			isload = true;
			isSize = true;
		}
		if (isload) {
			for (int i = 0; i < commentDtoList.size(); i++) {
				booleans.add(false);
			}
			isload = false;
		}
		return commentDtoList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentDtoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_comment_list, parent, false);
			holderView.topic_comment_avatar = (CircleImageView) convertView.findViewById(R.id.topic_comment_avatar);
			holderView.topic_comment_nickname = (TextView) convertView.findViewById(R.id.topic_comment_nickname);
			holderView.topic_comment_content = (TextView) convertView.findViewById(R.id.topic_comment_content);
			holderView.topic_comment_addTime = (TextView) convertView.findViewById(R.id.topic_comment_addTime);
			holderView.topic_comment_praise = (TextView) convertView.findViewById(R.id.topic_comment_praise);
			holderView.loft_num = (TextView) convertView.findViewById(R.id.loft_num);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.topic_comment_nickname.setText(commentDtoList.get(position).getNickname());
		// replaceTagHTML(hotTopicList.get(position).getContent()),Html.fromHtml(commentDtoList.get(position).getCommentContent())
		try {
			String code = URLDecoder.decode(commentDtoList.get(position).getCommentContent(), "UTF-8");
			holderView.topic_comment_content.setText(replaceTagHTML(code));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		holderView.topic_comment_addTime.setText(commentDtoList.get(position).getAddTime());
		holderView.topic_comment_praise.setText(String.valueOf(commentDtoList.get(position).getPraiseNumber()));
		holderView.topic_comment_praise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (booleans.get(position)) {
					Toast.makeText(context, "你已经点过赞了", 0).show();
				} else {
					commentPraise(commentDtoList.get(position).getId(), position);
					booleans.set(position, true);
				}
			}
		});
		holderView.loft_num.setText(position + 1 + "楼");
		imageLoader.displayImage(Address.IMAGE + commentDtoList.get(position).getAvatar(),
				holderView.topic_comment_avatar, loadImage());
		return convertView;
	}

	class HolderView {
		private CircleImageView topic_comment_avatar;
		private TextView topic_comment_nickname, topic_comment_content, topic_comment_addTime, topic_comment_praise,
				loft_num;
	}

	public static String replaceTagHTML(String src) {
		String regex = "\\<(.+?)\\>";
		if (!TextUtils.isEmpty(src)) {
			return src.replaceAll(regex, "");
		}
		return "";
	}

	private static DisplayImageOptions options;

	private static DisplayImageOptions loadImage() {

		if (options == null) {
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.weijiazai_header) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.weijiazai_header) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.weijiazai_header) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
					// .displayer(new FadeInBitmapDisplayer(1000))// 设置渐入动画时间
					// .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
					.build(); // 构建完成
		}
		return options;
	}

	// 评论点赞
	private void commentPraise(int commentId, final int position) {
		Log.i("xm", position + "---position");
		RequestParams params = new RequestParams();
		params.put("commentId", commentId);
		httpClient.post(Address.COMMENTPRAISE, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					MessageEntity messageEntity = JSON.parseObject(arg2, MessageEntity.class);
					if (messageEntity.isSuccess()) {
						int counts = commentDtoList.get(position).getPraiseNumber();
						int temp = counts++;
						commentDtoList.get(position).setPraiseNumber((temp + 1));
						notifyDataSetChanged();
						Toast.makeText(context, "点赞成功", 0).show();
					} else {
						Toast.makeText(context, "点赞失败", 0).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

			}
		});
	}

}
