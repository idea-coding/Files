package in.type.app;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.AdapterView.*;

public class Quick
{
	public static void make(Activity act,String title,String message){
		new AlertDialog.Builder(act)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton("Ok",null)
			.create().show();
	}
	
	public static void make(Activity act,String[] items,DialogInterface.OnClickListener l){
		new AlertDialog.Builder(act)
			.setItems(items,l)
			.create().show();
	}
	
	public static void make(Activity act,String title,final View v,final OnClickListener l){
		new AlertDialog.Builder(act)
			.setTitle(title)
			.setView(v)
			.setPositiveButton("ok", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					l.onClick(v);
				}
			}).create().show();
	}
}
