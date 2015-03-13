package com.example.research;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

import java.util.Vector;

public class MyResultReceiver extends ResultReceiver {

	private Vector<String> last11;
	private Vector<Classifier> SVMs;
	private SVMMethods methodObject;
    private setsMeanStdDev holdInfo;

	public interface Receiver {
		public void onReceiveResult(int resultCode, Bundle resultData);
	}

	public MyResultReceiver(Handler handler) {
		super(handler);
		last11 = new Vector<String>();
		SVMs = new Vector<Classifier>();
		methodObject = new SVMMethods();
		// TODO Auto-generated constructor stub
	}

	private MyResultReceiver mReceiver;

	public void setReceiver(MyResultReceiver receiver) {
		mReceiver = receiver;
	}

	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (mReceiver != null) {
			
			Vector<String> temp = new Vector<String>(resultData.getStringArrayList("results"));  
			//CLASSIFY IF 1, no training here, the vector here should contain 1 element
            //at index 0, the most recent reading
			if (resultCode == 1) {
				//
				//POTENTIAL ERROOR HERE!!!!!!!!!!!!!
			    Instance toClassify = methodObject.makeInstance(last11, temp.get(0), holdInfo);
                //I'm not sure what you want to do with these values, 
                //they are either true or false, an alert would happen if one returned true
                Object classificationHigh = methodObject.classify(SVMs.get(0), toClassify);
                Object classificationLow = methodObject.classify(SVMs.get(1), toClassify);
                //increment the last11
                for(int i=10; i>0; --i) 
                {
                	last11.set(i, last11.get(i-1));
                }
                last11.set(0, (String) temp.get(0));
			}
			//TRAIN / RETRAIN if 2
			
			else if (resultCode == 2) {
				for(int i=11; i>=0 ; --i){
					last11.add(temp.get(i));	
				}
				
		        holdInfo = methodObject.produceDataSets(temp, 80, 160);
			    SVMs = methodObject.trainSVM(holdInfo.sets.get(0), holdInfo.sets.get(1));
			    
			    Instance toTest = methodObject.makeInstance(last11, temp.get(0), holdInfo);
			    Log.d("data", methodObject.classify(SVMs.get(0), toTest).toString());
			    Log.d("data", methodObject.classify(SVMs.get(1), toTest).toString());
			    //0 -safe
                //1 - not so safe
                System.out.println(methodObject.classify(SVMs.get(0), toTest));
			    System.out.println(methodObject.classify(SVMs.get(1), toTest));
				
			}

		}
	}
}