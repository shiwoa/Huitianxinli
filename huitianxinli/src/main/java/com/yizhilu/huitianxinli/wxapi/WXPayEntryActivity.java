package com.yizhilu.huitianxinli.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.wechatpayment.Constants;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			String valueOf = String.valueOf(resp.errCode);
			if("-1".equals(valueOf)){
				ConstantUtils.showMsg(WXPayEntryActivity.this, "支付错误");
				this.finish();
			}else if("0".equals(valueOf)){
//				StaticUtils.setPay(true);
				Intent intent = new Intent("wxSuccess");
				sendBroadcast(intent);
				this.finish();
			}else if("-2".equals(valueOf)){
				ConstantUtils.showMsg(WXPayEntryActivity.this, "取消支付");
				this.finish();
			}
		}
	}
}