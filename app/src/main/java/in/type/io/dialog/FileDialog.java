package in.type.io.dialog;
import android.app.Activity;
import java.io.*;
import in.type.app.*;
import android.app.ProgressDialog;
import org.apache.commons.io.*;
import android.media.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import in.type.io.fragments.*;

public class FileDialog
{
	public static void delete(Activity act,File file){
		new DeleteTask(act,file).execute();
	}
	
	public static void copy(Activity act,File in,File outDir){
		
	}
	
	public static void newFolder(final FileListFragment act,final File current){
		EditText et = new EditText(act.getActivity());
		et.setHint("Folder Name");
		Quick.make(act.getActivity(), "New Folder", et, new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					try{
						EditText et = (EditText) p1;
						String name = et.getText()+"";
						if(!name.isEmpty()&&!name.contains("/")&&!name.contains("*")){
							File n = new File(current,name);
							if(!n.exists()&&current.canWrite()){
								n.mkdir();
								act.updateList(current);
							}
						}else{
							Quick.make(act.getActivity(),"Check your input","Name contains symbols like '/','*' etc.");
						}
					}catch(Throwable e){
						EHandler.show(act.getActivity(),e);
					}
				}
			});
	}
	
	
	public static class DeleteTask extends ProgressTask
	{
		public File file;
		
		public DeleteTask(Activity act,File f){
			super(act);
			this.file = f;
		}

		@Override
		public void onCreate()
		{
			if(file.isFile()){
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setMessage("Deleting "+file.getName());
			}else{
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setTitle("Deleting" +file);
				pd.setMax(countFiles(file));
			}
		}

		private int countFiles(File fa)
		{
			int rt = 0;
			for(File f:fa.listFiles()){
				if(f.isFile()){
					rt+=1;
				}else{
					rt+=countFiles(f);
				}
			}
			rt+=1;
			return rt;
		}

		@Override
		public void onUpdate()
		{
			if(file.isFile()){
				file.delete();
				kill();
			}else{
				deleteDir(file);
				kill();
			}
		}

		private void deleteDir(File fa)
		{
			for(File f:fa.listFiles()){
				if(f.isFile()){
					setProgress(pd.getProgress()+1);
					f.delete();
				}else{
					deleteDir(f);
				}
			}
			setProgress(pd.getProgress()+1);
			fa.delete();
		}

		@Override
		public void onUiThread(int id, Object[] args)
		{
			
		}
		
		@Override
		public void onDestroy()
		{
			// TODO: Implement this method
		}
		
		
	}
}
