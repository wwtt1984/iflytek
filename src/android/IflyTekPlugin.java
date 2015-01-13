package com.iflytek.iflytek;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;
import android.widget.Toast;

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
import com.iflytek.speech.setting.IatSettings;
import com.iflytek.speech.util.ApkInstaller;
import com.iflytek.speech.util.FucUtil;
import com.iflytek.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;



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
		    mIat = SpeechRecognizer.createRecognizer(this, null);
            ret = mIat.startListening(recognizerListener);
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
		Intent intents = new Intent(IatDemo.this, IatSettings.class);
	    startActivity(intents);
	}
	
	this.cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                
				private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener(){
					public void onResult(RecognizerResult results, boolean isLast) {
						String text = JsonParser.parseIatResult(results.getResultString());
						Toast.makeText(this.cordova.getActivity(), text,3000).show();
						//mResultText.append(text);
						//mResultText.setSelection(mResultText.length());
					}
					/**
					 * 识别回调错误.
					 */
					public void onError(SpeechError error) {
						showTip(error.getPlainDescription(true));
					}
				};
				
            }
    });
	
	
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
		
		String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT,lag);
		}
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		// 设置音频保存路径
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/iflytek/wavaudio.pcm");
	}

/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败,错误码："+code);
        	}
		}
	};
	
	
	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener=new RecognizerListener(){

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
			Log.d(TAG, results.getResultString());
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
}
