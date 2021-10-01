package in.type.app;
import android.app.*;
import android.os.*;

public abstract class ProgressTask extends AsyncTask<String,Void,Void>
{
	public ProgressDialog pd;
	public boolean killed;
	public Activity act;
	
	private static final int PROGRESS_VISIBILITY = 2549203;
	private static final int PROGRESS = 2549283;
	private static final int MAX = 2549537;
	private static final int MESSAGE = 25495193;
	private static final int TITLE = 2549364;
	private static final int STYLE = 2549100;
	
	public ProgressTask(Activity p1){
		this.act = p1;
		this.pd = new ProgressDialog(p1);
		this.killed = false;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		
		onCreate();
		pd.show();
	}
	
	
	@Override
	protected Void doInBackground(String[] p1)
	{
		while(!killed){
			try{
				onUpdate();
			}catch(Throwable e){
				kill();
			}
		}
		return null;
	}
	
	public void kill(){
		killed = true;
		hide();
		onDestroy();
		if(!isCancelled()){
			cancel(true);
		}
	}

	@Override
	protected void onPostExecute(Void result)
	{
		super.onPostExecute(result);
		if(!killed){
			kill();
		}
	}
	
	
	public void hide(){
		run(PROGRESS_VISIBILITY,new Object[]{false});
	}
	
	public void setProgress(int progress){
		run(PROGRESS,new Object[]{progress});
	}
	
	public void setMax(int max){
		run(MAX,new Object[]{max});
	}
	
	public void setTitle(String title){
		run(TITLE,new Object[]{title});
	}
	
	public void setMessage(String message){
		run(MESSAGE,new Object[]{message});
	}
	
	public void setStyle(int style){
		run(STYLE,new Object[]{style});
	}
	
	public void run(final int id,final Object[] args){
		act.runOnUiThread(new Runnable(){

				@Override
				public void run()
				{
					switch(id){
						case PROGRESS_VISIBILITY:
							boolean b = args[0];
							if(b == false){
								pd.hide();
							}else{
								pd.show();
							}
							break;
						case PROGRESS:
							int prgs = args[0];
							pd.setProgress(prgs);
							break;
						case MAX:
							int max = args[0];
							pd.setMax(max);
							break;
						case MESSAGE:
							String message = (String) args[0];
							pd.setMessage(message);
							break;
						case TITLE:
							String title = (String) args[0];
							pd.setTitle(title);
							break;
						case STYLE:
							int style = args[0];
							pd.setProgressStyle(style);
					}
					onUiThread(id,args);
				}
			});
	}
	
	public abstract void onCreate();
	public abstract void onUpdate();
	public abstract void onUiThread(int id,Object[] args);
	public abstract void onDestroy();
}
