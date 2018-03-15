package com.yizhilu.exam;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.utils.StaticUtils;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;

/**
 * @author bin
 *         时间: 2016/6/25 14:47
 *         类说明:记笔记的类
 */
public class RecordNoteActivity extends BaseActivity {

    ImageView sideMenu;

    LinearLayout leftLayout;

    TextView title;

    TextView rightLayoutTv;

    LinearLayout rightLayout;

    EditText nodeContent;
    private ProgressDialog progressDialog;  //加载数据显示
    private int userId, subId, qstId,zongPosition; //用户,专业,试题的Id,当前试题下标
    private Intent intent;  //意图对象
    private String noteContent;  //笔记内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_record_note);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        rightLayoutTv = (TextView) findViewById(R.id.right_layout_tv);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        nodeContent = (EditText) findViewById(R.id.node_content);

        //获取传过来的信息
        getIntentMessage();
        super.onCreate(savedInstanceState);
    }

    /**
     * @author bin
     * 时间: 2016/6/27 11:56
     * 方法说明:获取传过来的信息
     */
    public void getIntentMessage() {
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户Id
        subId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);  //专业Id
        Intent intent = getIntent();
        qstId = intent.getIntExtra("qstId", 0);  //获取试题的id
        zongPosition = intent.getIntExtra("zongPosition",-1);  //试题的下标
        noteContent = intent.getStringExtra("noteContent");  //笔记内容

        nodeContent.setText(noteContent);  //设置笔记内容
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        progressDialog = new ProgressDialog(RecordNoteActivity.this);  //加载数据显示
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.add_note);  //设置标题
        rightLayout.setVisibility(View.VISIBLE);  //提交布局
        rightLayoutTv.setVisibility(View.VISIBLE);
        rightLayoutTv.setText(R.string.submit);  //提交

    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);  //返回
        rightLayout.setOnClickListener(this);  //提交
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout:  //返回
                intent.putExtra("note",noteContent);
                if(zongPosition<0){   //在错题记录或习题收藏过来的
                    setResult(0,intent);
                }else{
                    StaticUtils.getNoteList().set(zongPosition-1,noteContent);
                    setResult(1,intent);
                }
                RecordNoteActivity.this.finish();
                break;
            case R.id.right_layout:  //提交
                String correct = nodeContent.getText().toString();
                if (TextUtils.isEmpty(correct)) {
                    ConstantUtils.showMsg(RecordNoteActivity.this, "请输入笔记内容");
                } else {
                    //添加笔记的方法
                    addNote(userId, subId, qstId, correct);
                }
                break;
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/7 14:18
     * 方法说明:添加笔记的方法
     */
    private void addNote(int cusId, int subId, int qstId, final String noteContent) {
        RequestParams params = new RequestParams();
        params.put("cusId", cusId);
        params.put("subId", subId);
        params.put("qstId", qstId);
        params.put("noteContent", noteContent);
        Log.i("lala", ExamAddress.ADDNOTE_URL+"?"+params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.ADDNOTE_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ConstantUtils.showProgressDialog(progressDialog);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String data) {
                ConstantUtils.exitProgressDialog(progressDialog);
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject object = new JSONObject(data);
                        String message = object.getString("message");
                        if (object.getBoolean("success")) {
                            nodeContent.setText("");
                            ConstantUtils.showMsg(RecordNoteActivity.this, "笔记添加成功");
                            intent.putExtra("note",noteContent);
                            if(zongPosition<0){   //在错题记录过来的
                                setResult(0,intent);
                            }else{
                                StaticUtils.getNoteList().set(zongPosition-1,noteContent);
                                setResult(1,intent);
                            }
                            RecordNoteActivity.this.finish();
                        } else {
                            ConstantUtils.showMsg(RecordNoteActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                ConstantUtils.exitProgressDialog(progressDialog);
            }
        });
    }
}
