package com.yizhilu.baoli;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason.ErrorType;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

public class PolyvDownloadListAdapter extends BaseAdapter {
	private static final String TAG = "PolyvDownloadListAdapter";

	private static final int REFRESH_PROGRESS = 1;
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;

	private LinkedList<PolyvDownloadInfo> data;
	private ArrayList<MyDownloadListener> listener;
	private Context context;
	private LayoutInflater inflater;
	private PolyvDBservice service;
	private ViewHolder holder;
	private ImageLoader imageLoader;
	private PolyvDownloader downloader;

	private ListView listView;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Button btn = null;
			int position = (int) msg.arg1;
			int offset = position - listView.getFirstVisiblePosition();
			int endset = position - listView.getLastVisiblePosition();
			if (offset < 0 || endset > 0)
				return;
			View view = (View) listView.getChildAt(offset);
			switch (msg.what) {
			case REFRESH_PROGRESS:
				btn = (Button) view.findViewById(R.id.download);
				if (!btn.getText().equals("播放")) {
					long downloaded = msg.getData().getLong("count");
					long total = msg.getData().getLong("total");
					ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
					progressBar.setMax((int) total);
					progressBar.setProgress((int) downloaded);
					TextView tv = (TextView) view.findViewById(R.id.rate);
					tv.setText("" + downloaded * 100 / total);
				}
				break;

			case SUCCESS:
				btn = (Button) view.findViewById(R.id.download);
				if (!btn.getText().equals("播放")) {
					btn.setText("播放");
				}
				break;

			case FAILURE:
				ErrorType errorType = (ErrorType) msg.obj;
				btn = (Button) view.findViewById(R.id.download);
				if (!btn.getText().equals("播放")) {
					btn.setText("开始");

					switch (errorType) {
					case VID_IS_NULL:
						break;
					case NOT_PERMISSION:
						break;
					case RUNTIME_EXCEPTION:
						break;
					case VIDEO_STATUS_ERROR:
						break;
					case M3U8_NOT_DATA:
						break;
					case QUESTION_NOT_DATA:
						break;
					case MULTIMEDIA_LIST_EMPTY:
						break;
					case CAN_NOT_MKDIR:
						break;
					case DOWNLOAD_TS_ERROR:
						break;
					case MULTIMEDIA_EMPTY:
						break;
					case NOT_CREATE_DIR:
						break;
					}

				}
				break;
			}
		};
	};

	public String title;

	public PolyvDownloadListAdapter(Context context, LinkedList<PolyvDownloadInfo> data, ListView listView) {
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		this.service = new PolyvDBservice(context);
		listener = new ArrayList<MyDownloadListener>();
		this.listView = listView;
		imageLoader = ImageLoader.getInstance();
		initDownloaders();
	}

	private class MyDownloadListener implements PolyvDownloadProgressListener {
		private int position;
		private PolyvDownloadInfo info;
		private long total;

		public MyDownloadListener(int position, PolyvDownloadInfo info) {
			this.position = position;
			this.info = info;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		@Override
		public void onDownloadSuccess() {
			Message msg = handler.obtainMessage();
			msg.arg1 = position;
			msg.what = SUCCESS;
			service.updatePercent(info, total, total);
			handler.sendMessage(msg);
		}

		@Override
		public void onDownloadFail(PolyvDownloaderErrorReason errorReason) {
			Message msg = handler.obtainMessage();
			msg.arg1 = position;
			msg.what = FAILURE;
			msg.obj = errorReason.getType();
			handler.sendMessage(msg);
		}

		@Override
		public void onDownload(long count, long total) {
			this.total = total;
			Message msg = handler.obtainMessage();
			msg.arg1 = position;
			Bundle bundle = new Bundle();
			bundle.putLong("count", count);
			bundle.putLong("total", total);
			msg.setData(bundle);
			msg.what = REFRESH_PROGRESS;
			service.updatePercent(info, count, total);
			handler.sendMessage(msg);
		}
	}

	private String formatSecond(Object second) {
		String html = "0秒";
		if (second != null) {
			Double s = (Double) second;
			String format;
			Object[] array;
			Integer hours = (int) (s / (60 * 60));
			Integer minutes = (int) (s / 60 - hours * 60);
			Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
			if (hours > 0) {
				format = "%1$,d时%2$,d分%3$,d秒";
				array = new Object[] { hours, minutes, seconds };
			} else if (minutes > 0) {
				format = "%1$,d分%2$,d秒";
				array = new Object[] { minutes, seconds };
			} else {
				format = "%1$,d秒";
				array = new Object[] { seconds };
			}
			html = String.format(format, array);
		}

		return html;
	}

	private void initDownloaders() {
		for (int i = 0; i < data.size(); i++) {
			final PolyvDownloadInfo info = data.get(i);
			final String _vid = info.getVid();
			final int p = i;
			downloader = PolyvDownloaderManager.getPolyvDownloader(_vid, info.getBitrate());
			MyDownloadListener downloadListener = new MyDownloadListener(p, info);
			listener.add(downloadListener);
			downloader.setPolyvDownloadProressListener(downloadListener);
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		final int i = position;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.view_item, null);
			holder = new ViewHolder();
			holder.tv_vid = (TextView) convertView.findViewById(R.id.tv_vid);
			holder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
			holder.tv_filesize = (TextView) convertView.findViewById(R.id.tv_filesize);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			holder.btn_download = (Button) convertView.findViewById(R.id.download);
			holder.btn_delete = (Button) convertView.findViewById(R.id.delete);
			holder.tv_rate = (TextView) convertView.findViewById(R.id.rate);
			holder.view_icon = (ImageView) convertView.findViewById(R.id.view_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		data = service.getDownloadFiles();
		final PolyvDownloadInfo info = data.get(position);
		String duration = info.getDuration();
		long filesize = info.getFilesize();
		long percent = info.getPercent();
		long total = info.getTotal();
		double time = Double.parseDouble(duration);
		title=info.getTitle();
		holder.tv_vid.setText(info.getTitle());
		imageLoader.displayImage(Address.IMAGE_NET + info.getImage(), holder.view_icon, HttpUtils.getDisPlay());
		// holder.tv_duration.setText(duration);
		holder.tv_duration.setText(formatSecond(time));
		holder.tv_filesize.setText(filesize / 1024 / 1024 + "M");
		holder.progressBar.setTag("" + position);
		holder.progressBar.setMax((int) total);
		holder.progressBar.setProgress((int) percent);
		if (total != 0)
			holder.tv_rate.setText("" + percent * 100 / total);
		else
			holder.tv_rate.setText("" + 0);
		if (total != 0 && total == percent) {
			holder.btn_download.setText("播放");
		} else if (PolyvDownloaderManager.getPolyvDownloader(info.getVid(), info.getBitrate()).isDownloading())
			holder.btn_download.setText("暂停");
		else
			holder.btn_download.setText("开始");
		holder.btn_download.setOnClickListener(new DownloadListener(info.getVid(), info.getBitrate(), convertView));
		holder.btn_delete.setOnClickListener(new DeleteListener(info, position));
		return convertView;
	}

	public void downloadAllFile() {
		PolyvDownloaderManager.startAll();
	}

	public void updateAllButton(boolean isStop) {
		for (int i = 0; i < listView.getChildCount(); i++) {
			Button down = (Button) listView.getChildAt(i).findViewById(R.id.download);
			if (!down.getText().equals("播放")) {
				if (isStop)
					down.setText("暂停");
				else
					down.setText("开始");
			}
		}
	}

	private class ViewHolder {
		TextView tv_vid, tv_duration, tv_filesize, tv_rate;
		ProgressBar progressBar;
		Button btn_download, btn_delete, btn_start, btn_pause;
		ImageView view_icon;
	}

	class DownloadListener implements View.OnClickListener {
		private final String vid;
		private final int bitRate;
		private View view;

		public DownloadListener(String vid, int bitRate, View view) {
			this.vid = vid;
			this.bitRate = bitRate;
			this.view = view;
		}

		@Override
		public void onClick(View v) {
			Button download = (Button) view.findViewById(R.id.download);
			if (download.getText().equals("开始")) {
				((Button) v).setText("暂停");
				PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitRate);
				if (downloader != null) {
					downloader.start();
				}

			} else if (download.getText().equals("暂停")) {
				((Button) v).setText("开始");
				PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitRate);
				if (downloader != null) {
					downloader.stop();
				}
			} else if (download.getText().equals("播放")) {
				IjkVideoActicity.intentTo(context, IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid,
						vid, false,title);
			}
		}

	}

	class DeleteListener implements View.OnClickListener {
		private PolyvDownloadInfo info;
		private int position;

		public DeleteListener(PolyvDownloadInfo info, int position) {
			this.info = info;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(info.getVid(), info.getBitrate());
			PolyvDownloaderManager.clearPolyvDownload(info.getVid(), info.getBitrate());
			if (downloader != null) {
				downloader.deleteVideo(info.getVid(), info.getBitrate());
			}
			service.deleteDownloadFile(info);
			data.remove(position);
			listener.remove(position);
			for (int i = 0; i < listener.size(); i++) {
				listener.get(i).setPosition(i);
			}
			notifyDataSetChanged();
		}
	}

	public void stopAll() {
		PolyvDownloaderManager.stopAll();
	}
}
