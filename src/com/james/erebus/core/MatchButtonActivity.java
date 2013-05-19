package com.james.erebus.core;

import java.io.IOException;
import java.util.ArrayList;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.misc.AppConsts;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.MatchSubscriptionManager;
import com.james.erebus.networking.SubscriptionRetriever;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MatchButtonActivity extends Activity {

	Match match;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.james.erebus.R.layout.activity_match_button);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setTitle("");
		//Log.i("intent action values", this.getIntent().getStringExtra("com.james.erebus.MatchButtonActivity.dataValues"));
		Bundle b = this.getIntent().getExtras();
		if(b != null)
		{
			JSONObject o = (JSONObject) b.get("com.james.erebus.MatchButtonActivity.dataValues");
			displayData(o);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setSubButtonText()
	{
		SubscriptionRetriever sr = new SubscriptionRetriever();
		JSONArray ja = sr.retrieve(sr.getURI(), sr.getSubscriptionsFilename());
		Button subButton = (Button) findViewById(com.james.erebus.R.id.matchSubscribeButton);
		for(int i = 0; i < ja.length(); i++)
		{
			try {
				JSONObject obj = ja.getJSONObject(i);
				Log.v("objsubbuttontext", obj.toString());
				if(obj.get("model_type").equals("MatchEntry"))
				{
					if(match.getId() == Integer.parseInt(obj.get("model_id").toString()))
					{	
						subButton.setText("Subscribed");
						return;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		subButton.setText("Unsubscribed");
	}

	private boolean isMatchSubbed()
	{
		MatchSubscriptionManager msm = new MatchSubscriptionManager();
		ArrayList<Match> matches = msm.getSubbedMatches(this);
		for(Match m : matches)
		{
			if(m.equalsMatch(match))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void onResume()
	{
		AppConsts.currentActivity = this;
		super.onResume();
	}


	public void matchSubUnsub(View v) throws IOException, JSONException
	{
		MatchSubscriptionManager msm = new MatchSubscriptionManager();
		Button subButton = (Button) findViewById(com.james.erebus.R.id.matchSubscribeButton);
		subButton.setEnabled(false);
		if(!msm.isMatchSubbed(this, match))
		{

			msm.subToMatch(match, this, subButton);
		}
		else
		{
			msm.unsubFromMatch(this, match, subButton);
		}
		subButton.setEnabled(true);
	}

	private void displayData(JSONObject data)
	{
		TextView tvTitle = (TextView)findViewById(com.james.erebus.R.id.matchButtonTitleBox);
		tvTitle.setTextSize(50f);
		TextView tvDate = (TextView)findViewById(com.james.erebus.R.id.matchButtonDateBox);
		tvDate.setTextSize(30f);
		TextView tvLinks = (TextView)findViewById(com.james.erebus.R.id.matchButtonLinksBox);
		tvLinks.setTextSize(30f);
		TextView tvParentTourny = (TextView)findViewById(com.james.erebus.R.id.matchButtonParentTournyBox);
		tvParentTourny.setTextSize(30f);
		TextView tvTime = (TextView)findViewById(com.james.erebus.R.id.matchButtonTimeBox);
		tvTime.setTextSize(30f);


		match = MiscJsonHelpers.jsonToMatch(data);
		tvTitle.setText(match.getPlayer1() + " vs " + match.getPlayer2());
		tvDate.setText("Match date: " + match.getDate());
		tvTime.setText("Match time: " + match.getTime());
		tvLinks.setText(match.getLinks());
		tvParentTourny.setText(match.getParentTourny());
		setSubButtonText();
	}

}