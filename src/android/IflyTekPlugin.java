package com.iflytek.iflytek;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.ContactManager;
import com.iflytek.cloud.util.ContactManager.ContactListener;

import android.content.Context;

import com.iflytek.speech.util.JsonParser;

public class IflyTekPlugin extends CordovaPlugin{

    private CallbackContext callContext = null;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
	private SpeechRecognizer mIat;// 语音听写对象
	private String appId;
	int ret = 0;// 函数调用返回值

	@Override
    public boolean execute(String action, JSONArray args,
            final CallbackContext callbackContext) throws JSONException {
				
        if(action.equals("Init"))
        {
		    appId = args.getString(0);
			SpeechUtility.createUtility(this.cordova.getActivity(), SpeechConstant.APPID +"="+appId);
			Toast.makeText(this.cordova.getActivity(), "初始化成功!",3000).show();
			return true;
        }
        else if(action.equals("StartListen"))
        {
			callContext = callbackContext;
		    final Context cn = this.cordova.getActivity();
			mIat = SpeechRecognizer.createRecognizer(cn, null);
			mIat.setParameter(SpeechConstant.DOMAIN, "iat");
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
			Toast.makeText(cn, "444",3000).show();
			ret = mIat.startListening(recognizerListener);
			
			// Don't return any result now, since status results will be sent when events come in from broadcast receiver
			PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
			pluginResult.setKeepCallback(true);
			callbackContext.sendPluginResult(pluginResult);
			
			return true;
        }
		 else if(action.equals("StopListen"))
        {
            return true;
        }

        return false;
    }
	
	
	private void startRec()
	{
		
		
	}
	
	/**
	 * 听写监听器。
	 */
	RecognizerListener recognizerListener = new RecognizerListener(){

		@Override
		public void onBeginOfSpeech() {
			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			showTip(text);
			PluginResult result = new PluginResult(PluginResult.Status.OK, text);
			result.setKeepCallback(false);
			callContext.sendPluginResult(result);
			
			if(isLast) {
				//TODO 最后的结果
				
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			//showTip("当前正在说话，音量大小：" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	private void callBackValue(final String str)
	{
	    Toast.makeText(this.cordova.getActivity(), str,3000).show();
		callContext.success(str);
	}

	

	private void showTip(final String str)
	{
		Toast.makeText(this.cordova.getActivity(), str,3000).show();
	}


}
