package in.type.app;

import android.app.Activity;
import android.util.*;

public class EHandler
{
	public static void show(Activity act,Throwable e){
		Quick.make(act,e.getClass().getName(),Log.getStackTraceString(e));
	}
}
