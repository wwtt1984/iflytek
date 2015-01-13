package com.iflytek.iflytek;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
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

import com.iflytek.speech.util.JsonParser;

public class IflyTekPlugin extends CordovaPlugin{

    private static CallbackContext CallbackContext;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 语音听写对象
	private SpeechRecognizer mIat;
	private String appId;
	int ret = 0;// 函数调用返回值

    public boolean execute(String action, JSONArray data,
            CallbackContext callbackContext) throws JSONException {
        if(action.equals("Init"))
        {
		    appId = data.getString(0);
			SpeechUtility.createUtility(this.cordova.getActivity(), "appid="+appId);
            return true;
        }
        else if(action.equals("StartListen"))
        {
		    // 初始化识别对象
		    mIat = SpeechRecognizer.createRecognizer(this.cordova.getActivity(), null);
			startRec(null);
            return true;
        }
		 else if(action.equals("StopListen"))
        {

            return true;
        }
       

        return false;
    }
	
	
	private void startRec(final String str)
	{
 		ret = mIat.startListening(recognizerListener);
		this.cordova.getActivity().runOnUiThread(new Runnable() {
				public void run() {


				}
            });
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
			//Log.d(TAG, results.getResultString());
			String text = JsonParser.parseIatResult(results.getResultString());
			//mResultText.append(text);
			//mResultText.setSelection(mResultText.length());
			if(isLast) {
				//TODO 最后的结果
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("当前正在说话，音量大小：" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	private void showTip(final String str)
	{
		Toast.makeText(this.cordova.getActivity(), str,3000).show();
	}

	/**
	 * 参数设置
	 * @param param
	 * @return 
	 */
	public void setParam(){
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");


	}



}
