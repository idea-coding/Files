package in.type.io.adapter;

import android.widget.*;
import java.io.*;
import android.app.Activity;
import java.util.*;
import in.type.io.*;
import android.view.*;
import in.type.app.*;

public class FileAdapter extends ArrayAdapter<File>
{ 
	public Activity act;
	public FileAdapter(Activity act,List<File> lst){
		super(act,R.layout.adapter_filelist,lst);
		this.act = act;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = act.getLayoutInflater().inflate(R.layout.adapter_filelist,parent,false);
		onCreateView(position,v);
		return v;
	}

	private void onCreateView(int position,View view)
	{
		try{
			File f = getItem(position);
			TextView tv = view.findViewById(R.id.adapter_file_name);
			ImageView iv = view.findViewById(R.id.adapter_file_icon);
			tv.setText(f.getName());
			handleIcon(f,iv);
		}catch(Throwable e){
			EHandler.show(act,e);
		}
	}

	private void handleIcon(File f, ImageView iv)
	{
		if(f.isDirectory()){
			iv.setImageResource(R.drawable.ic_folder);
		}else{
			iv.setImageResource(R.drawable.ic_file);
		}
	}
}
