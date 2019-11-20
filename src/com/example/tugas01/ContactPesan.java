package com.example.tugas01;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

public class ContactPesan extends Activity {
	
	ActionBar.Tab TabInbox, TabOutbox;
	Fragment fragmentInbox = new FragmentInbox();
	Fragment fragmentOutbox = new FragmentOutbox();
	
	private String contact_id, contact_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_pesan);
		
		Intent intent = getIntent();
        contact_id = intent.getStringExtra("id");
        contact_name = intent.getStringExtra("name");
        Bundle bundle = new Bundle();
        bundle.putString("id", contact_id);
        fragmentInbox.setArguments(bundle);
        fragmentOutbox.setArguments(bundle);
        
        setTitle(contact_name);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        TabInbox = actionBar.newTab().setText("Inbox");
        TabOutbox = actionBar.newTab().setText("Outbox");
        
        TabInbox.setTabListener(new TabListener(fragmentInbox));
        TabOutbox.setTabListener(new TabListener(fragmentOutbox));
        
		actionBar.addTab(TabInbox);
		actionBar.addTab(TabOutbox);
	}
}
