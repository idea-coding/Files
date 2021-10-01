package in.type.io.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import in.type.app.*;
import in.type.io.*;
import in.type.io.adapter.*;
import java.io.*;
import java.util.*;
import android.graphics.drawable.*;
import android.widget.AdapterView.*;
import android.content.*;
import android.net.*;
import android.webkit.*;
import android.support.design.widget.*;
import in.type.io.dialog.*;
import android.widget.Toolbar.*;
import android.graphics.*;

public class FileListFragment extends Fragment
{
	private ListView lv;
	private LinearLayout ll;
	private View v;
	private File current;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.frame_filelist,container,false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		try{
			this.v = view;
			
			init();
			setup();
			start();
		}catch(Throwable e){
			EHandler.show(getActivity(),e);
		}
	}

	private void start()
	{
		updateList(current);
	}

	public void updateList(File f)
	{
		this.current = f;
		((TextView) v.findViewById(R.id.text_file_path)).setText(current.getPath());
		if(f.canRead()){
			List<File> lst = getSortedList(f);
			if(lst.size()>0){
				showList();
				FileAdapter adapter = new FileAdapter(getActivity(),lst);
				lv.setAdapter(adapter);
			}else{
				showEmpty();
			}
		}else{
			showEmpty();
		}
	}

	private void showList()
	{
		ll.setVisibility(View.GONE);
		lv.setVisibility(View.VISIBLE);
	}

	private void showEmpty()
	{
		lv.setVisibility(View.GONE);
		ll.setVisibility(View.VISIBLE);
	}

	private List<File> getSortedList(File f)
	{
		List<File> lst = new ArrayList<File>();
		File[] ar = f.listFiles();
		Arrays.sort(ar);
		for(File fa:ar){
			if(fa.isDirectory()){
				lst.add(fa);
			}
		}
		for(File fa:ar){
			if(fa.isFile()){
				lst.add(fa);
			}
		}
		return lst;
	}

	private void setup()
	{
		lv.setDividerHeight(0);
		lv.setDivider(new BitmapDrawable());
		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					try{
						File f = (File) p1.getItemAtPosition(p3);
						if(f.isDirectory()){
							updateList(f);
						}else{
							openFile(f);
						}
					}catch(Throwable e){
						EHandler.show(getActivity(),e);
					}
				}
			});
		lv.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					try{
						final File f = (File) p1.getItemAtPosition(p3);
						Quick.make(getActivity(), new String[]{"Delete","New Folder","Copy Path"}, new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									if(p2 == 0){
										FileDialog.delete(getActivity(),f);
										new Thread(new Runnable(){

												@Override
												public void run()
												{
													while(f.exists()){
														
													}
													getActivity().runOnUiThread(new Runnable(){

															@Override
															public void run()
															{
																updateList(current);
															}
														});
												}
											}).start();
										
									}else if(p2 == 1){
										FileDialog.newFolder(get(),current);
									}else if(p2 == 2){
										ClipboardManager cb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
										cb.setText(f.getAbsolutePath());
										Snackbar.make(v,"Copied to clipboard",Snackbar.LENGTH_SHORT).show();
									}
								}
							});
					}catch(Throwable e){
						EHandler.show(getActivity(),e);
					}
					return true;
				}
			});
	}

	public FileListFragment get(){
		return this;
	}
	private void init()
	{
		lv = v.findViewById(R.id.list_file_main);
		current = new File("/sdcard");
		ll = v.findViewById(R.id.layout_file_not_found);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		updateList(current);
	}
	
	public void openFile(final File f){
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(f),getMimeType(f));
		OnClickListener openWith = new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				try{
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setDataAndType(Uri.fromFile(f),"application/*");
					startActivity(i);
				}catch(Throwable e){
					EHandler.show(getActivity(),e);
				}
			}
		};
		try{
			startActivity(i);
		}catch(Throwable e){
			Snackbar s = Snackbar.make(v,"No apps installed to open the file!",Snackbar.LENGTH_SHORT);
			s.setAction("Open With",openWith);
			s.setActionTextColor(Color.RED);
			s.show();
		}
		try{
			
		}catch(ActivityNotFoundException e){
			;
		}
	}

	private String getMimeType(File f)
	{
		String rt  = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl("file:///"+f.getAbsolutePath()));
		if(rt != null){
			return rt;
		}else{
			return "application/x-"+org.apache.commons.io.FileUtils.getExtension(f.getAbsolutePath());
		}
	}
	
	public boolean onBackPressed(){
		if(current.getAbsolutePath().equals("/")){
			return true;
		}else{
			try{
				updateList(current.getParentFile());
			}catch(Throwable e){
				
			}
			return false;
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.add("New Folder");
		menu.add("Exit");
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		try{
			if(item.getTitle().equals("New Folder")){
				FileDialog.newFolder(this,current);
			}else if(item.getTitle().equals("Exit")){
				getActivity().finish();
			}
		}catch(Throwable e){
			EHandler.show(getActivity(),e);
		}
		return super.onOptionsItemSelected(item);
	}
	
}
