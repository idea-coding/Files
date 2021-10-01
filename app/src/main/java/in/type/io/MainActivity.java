package in.type.io;

import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import in.type.app.*;
import in.type.io.fragments.*;
import java.io.*;
import java.util.*;

import android.support.v7.app.ActionBarDrawerToggle;
import android.graphics.*;
import android.content.res.*;

public class MainActivity extends AppCompatActivity 
{

	private Fragment currentFrame;
	private DrawerLayout dl;
	private ActionBarDrawerToggle toggle;

	private Toolbar toolbar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		try{
			init();
			setup();
			start();
		}catch(Throwable e){
			EHandler.show(this,e);
		}
    }

	private void start()
	{
		currentFrame = new FileListFragment();
		getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.frame_main,currentFrame)
			.commit();
	}

	private void setup()
	{
		setSupportActionBar(toolbar);
		NavigationView nv = new NavigationView(this);
		final List<MenuItem> items = new ArrayList<MenuItem>();
		items.add(nv.getMenu().add("Home").setCheckable(true).setChecked(true));
		items.add(nv.getMenu().add("Downloads").setCheckable(true).setChecked(false));
		items.add(nv.getMenu().add("Documents").setCheckable(true).setChecked(false));
		items.add(nv.getMenu().add("Photos").setCheckable(true).setChecked(false));
		DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT,DrawerLayout.LayoutParams.MATCH_PARENT,Gravity.START);
		nv.setLayoutParams(params);
		dl.addView(nv);
		toggle = new ActionBarDrawerToggle(this,dl,R.string.nav_open,R.string.nav_close);
		dl.addDrawerListener(toggle);
		toggle.syncState();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

				@Override
				public boolean onNavigationItemSelected(MenuItem p1)
				{
					File f = null;
					if(p1.getTitle().equals("Home")){
						f = Environment.getExternalStorageDirectory();
					}else if(p1.getTitle().equals("Downloads")){
						f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
					}else if(p1.getTitle().equals("Documents")){
						f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
					}else if(p1.getTitle().equals("Photos")){
						f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
					}
					if(f!=null){
						if(currentFrame.getClass().getName().equals(FileListFragment.class.getName())){
							FileListFragment flf = (FileListFragment) currentFrame;
							flf.updateList(f);
						}
					}
					for(MenuItem i:items){
						i.setChecked(false);
					}
					p1.setChecked(true);
					dl.closeDrawer(Gravity.START);
					return true;
				}
			});
	}

	private void init()
	{
		dl = findViewById(R.id.drawer_main);
		toolbar = findViewById(R.id.toolbar);
	}

	@Override
	public void onBackPressed()
	{
		if(currentFrame != null){
			if(currentFrame.getClass().getName().equals(FileListFragment.class.getName())){
				if(((FileListFragment) currentFrame).onBackPressed()){
					super.onBackPressed();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if(currentFrame != null){
			currentFrame.onCreateOptionsMenu(menu,getMenuInflater());
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(toggle.onOptionsItemSelected(item)){
			return toggle.onOptionsItemSelected(item);
		}
		if(currentFrame != null){
			currentFrame.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}
}
