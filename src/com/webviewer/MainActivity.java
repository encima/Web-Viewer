package com.webviewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MainActivity extends Activity
{	
	SharedPreferences sitePrefs;
	ArrayAdapter<Object> adapter;
	Object[] sitePrefsArray;
	Object[] site;
	ListView sites;
	String url;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);     
        
        sitePrefs=PreferenceManager.getDefaultSharedPreferences(this); 
        
        sites=(ListView)findViewById(R.id.sites);
        refresh();
        
        sites.setOnItemClickListener(new OnItemClickListener()
        {
        	public void onItemClick(AdapterView<?> parent, View view, int pos,long id) 
        	{
        		url=(String) ((TextView) view).getText();
        		Intent i=new Intent(MainActivity.this, SiteViewer.class);
        		i.putExtra("site", url);
        		startActivity(i);
        	}	      	
       }); 
               
        sites.setOnItemLongClickListener(new OnItemLongClickListener()
        {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,int arg2, long arg3)
			{
				boolean found=false;
				for(int i=0;i<sitePrefsArray.length;i++)
				{					
					if(sitePrefsArray[i].equals(((TextView) arg1).getText()))
					{
						System.out.println("Selection is:"  +  ((TextView) arg1).getText().toString());
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setMessage("Are you sure you want to Delete this Site?")
						       .setCancelable(false)
						       .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
						       {
						           public void onClick(DialogInterface dialog, int id)
						           {
							       		SharedPreferences.Editor sitePrefsEditor=sitePrefs.edit();
							    		sitePrefsEditor.remove(((TextView) arg1).getText().toString());
					    				sitePrefsEditor.commit();
					    				refresh();				    				
							    		Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_LONG).show();
						           }
						       })
						       .setNegativeButton("No", new DialogInterface.OnClickListener()
						       {
						           public void onClick(DialogInterface dialog, int id) 
						           { 
						                dialog.cancel(); 
						           }
						       });
						builder.show();
						found=true;
					}
				}
				if(!found)
				{
					Toast.makeText(MainActivity.this, "Cant Delete this Site as it is not User Specified", Toast.LENGTH_SHORT).show();
				}
				return false;
			}
        	
        });
    }
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	menu.add("Add Site");
    	menu.add("Exit");
    	return true;
	}

    public boolean onOptionsItemSelected(MenuItem item)
    {

	    	if(item.getTitle().equals("Add Site")) 
	    	{
	    		AddDialog add=new AddDialog(this, sitePrefs);
	    		add.show();		    		
	    	}else if(item.getTitle().equals("Exit"))
	    	{
	    		finish();
	    	}
    	return false;
    } 
    
    public void refresh()
    {       
        Object[] sitesArray=getResources().getStringArray(R.array.url_chooser);
        sitePrefsArray=sitePrefs.getAll().values().toArray();
        	site=new Object[sitesArray.length+sitePrefsArray.length];
        	System.arraycopy(sitesArray, 0, site, 0, sitesArray.length);
        	System.arraycopy(sitePrefsArray, 0, site, sitesArray.length, sitePrefsArray.length);  
        	adapter=new ArrayAdapter<Object>(MainActivity.this, R.layout.list_item, site);
    		adapter.notifyDataSetChanged();
    		sites.setAdapter(adapter);
    }
    
    class AddDialog extends Dialog implements OnClickListener
    {
    	EditText site;
    	Button ok;

    	public AddDialog(Context context, SharedPreferences sitePrefs)
    	{
    		super(context); 
    		setContentView(R.layout.addsitedialog); 		
    		setTitle("Add a Site");   		
    		site=(EditText) findViewById(R.id.Add_Site);
    		site.setHint("Enter Site Here");
    		ok=(Button) findViewById(R.id.OKAdd);
    		ok.setOnClickListener(this);
    	}

    	@Override
    	public void onClick(View v)
    	{     		
    		SharedPreferences.Editor sitePrefsEditor=sitePrefs.edit();
    		sitePrefsEditor.putString(site.getText().toString(), site.getText().toString());
    		sitePrefsEditor.commit();
    		refresh();
    		dismiss();
    	}
    }
}