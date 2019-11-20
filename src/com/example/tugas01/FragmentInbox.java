package com.example.tugas01;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentInbox extends Fragment {
	
	private String TAG = ContactPesan.class.getSimpleName();
    private ProgressDialog pDialog;
	
	private TextView ttotal;
	private ListView lv;
	
	private static String url = "http://apilearning.totopeto.com/messages/inbox?id=";
	
	private ArrayList<HashMap<String, String>> list_pesan;
	
	private int total;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        
        list_pesan = new ArrayList<HashMap<String, String>>();
        
        lv = (ListView) rootView.findViewById(R.id.listpesan);
        ttotal = (TextView) rootView.findViewById(R.id.tvtotal);
        
        return rootView;
    }
	
	private class GetMessages extends AsyncTask<Void, Void, Void> {
	   	 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
    		String link_id = getArguments().getString("id");
        	
            HttpHandler sh = new HttpHandler();
 
            String jsonStr = sh.makeServiceCall(url + link_id);
 
            Log.e(TAG, "Response from url: " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray data = jsonObj.getJSONArray("data");
                    total = data.length();
                    
                    for (int i = 0; i < total; i++) {
                        JSONObject c = data.getJSONObject(i);
                        
                        String id = c.getString("id");
                        String content = c.getString("content");
                        String created_at = c.getString("created_at");
                        String from = c.getString("from");
                        
                        HashMap<String, String> message = new HashMap<String, String>();
 
                        message.put("id", id);
                        message.put("from", from);
                        message.put("content", content);
                        message.put("created_at", created_at);
                        
                        list_pesan.add(message);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            if (pDialog.isShowing())
                pDialog.dismiss();
            
            ttotal.setText(String.valueOf(total));
            
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), list_pesan,
                    R.layout.list_pesan, new String[]{"from", "content",
                    "created_at"}, new int[]{R.id.pengirim,
                    R.id.content, R.id.created_at});
 
            lv.setAdapter(adapter);
        }
    }
	
	@Override
    public void onResume() {
    	super.onResume();
    	new GetMessages().execute();
    }
}