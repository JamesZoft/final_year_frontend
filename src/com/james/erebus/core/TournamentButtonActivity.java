package com.james.erebus.core;

import java.io.IOException;
import java.util.ArrayList;

import com.james.erebus.JSONJava.JSONArray;
import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.misc.AppConsts;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.SubscriptionRetriever;
import com.james.erebus.networking.TournamentSubscriptionManager;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class TournamentButtonActivity extends Activity {

	
	Tournament tournament;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.james.erebus.R.layout.activity_tournament_button);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setTitle("");
		Bundle b = this.getIntent().getExtras();
		if(b != null)
		{
			JSONObject o = (JSONObject) b.get("com.james.erebus.TournamentButtonActivity.dataValues");
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
	
	private void displayData(JSONObject data)
	{
		TextView tvTitle = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonTitleBox);
		tvTitle.setTextSize(50f);
		TextView tvDate = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonDateBox);
		tvDate.setTextSize(20f);
		TextView tvLinks = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonLinksBox);
		tvLinks.setTextSize(20f);
		TextView tvLocation = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonLocationBox);
		tvLocation.setTextSize(20f);
		TextView tvFormat = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonFormatBox);
		tvFormat.setTextSize(20f);
		TextView tvStatus = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonStatusBox);
		tvStatus.setTextSize(20f);
		TextView tvSponsor = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonSponsorBox);
		tvSponsor.setTextSize(20f);
		TextView tvEntryReqs = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonEntryReqsBox);
		tvEntryReqs.setTextSize(20f);
		TextView tvPrizes = (TextView)findViewById(com.james.erebus.R.id.tournamentButtonPrizesBox);
		tvPrizes.setTextSize(20f);


		tournament = MiscJsonHelpers.jsonToTournament(data);
		tvTitle.setText(tournament.getName());
		tvDate.setText("Tournament date: " + tournament.getStartDate());
		tvLinks.setText(tournament.getLinks());
		tvLocation.setText("Location: " + tournament.getLocation());
		tvFormat.setText("Format: " + tournament.getFormat());
		tvStatus.setText("Status: " + tournament.getStatus());
		tvSponsor.setText("Sponsor(s): " + tournament.getSponsor());
		tvEntryReqs.setText("Entry requirements: " + tournament.getEntryReqs());
		tvPrizes.setText("Prize(s): " + tournament.getPrizes());
		setSubButtonText();
	}
	
	private void setSubButtonText()
	{
		SubscriptionRetriever sr = new SubscriptionRetriever();
		JSONArray ja = sr.retrieve(sr.getURI(), sr.getSubscriptionsFilename());
		Button subButton = (Button) findViewById(com.james.erebus.R.id.tournamentSubscribeButton);
		for(int i = 0; i < ja.length(); i++)
		{
			try {
				JSONObject obj = ja.getJSONObject(i);
				Log.v("objsubbuttontext", obj.toString());
				if(obj.get("model_type").equals("TournamentEntry"))
				{
					
					//Log.v("tournyid", Integer.toString(tournament.getId()));
					//Log.v("retrievedtournyid", Integer.toString(retrievedTournament.getId()));
					if(tournament.getId() == Integer.parseInt(obj.get("model_id").toString()))
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
	
	private boolean isTournamentSubbed()
	{
		TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
		ArrayList<Tournament> tournaments = tsm.getSubbedTournaments(this);
		for(Tournament t : tournaments)
		{
			if(t.equalsTournament(tournament))
			{
				return true;
			}
		}
		return false;
	}
	
	public void tournamentSubUnsub(View v) throws IOException, JSONException
	{
		Button subButton = (Button) findViewById(com.james.erebus.R.id.tournamentSubscribeButton);
		TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
		if(!tsm.isTournamentSubbed(this, tournament))
		{

			tsm.subToTournament(tournament, this, subButton);
		}
		else
		{
			tsm.unsubFromTournament(this, tournament, subButton);
		}
		//setSubButtonText();
	}
	
	@Override
	public void onResume()
	{
		AppConsts.currentActivity = this;
		super.onResume();
	}

}