package com.james.erebus.core;

import java.io.IOException;
import java.util.ArrayList;

import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.misc.MiscJsonHelpers;
import com.james.erebus.networking.TournamentSubscriptionManager;

import android.os.Bundle;
import android.app.Activity;
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
		Button subButton = (Button) findViewById(com.james.erebus.R.id.tournamentSubscribeButton);
		
		String buttonText = isTournamentSubbed() ? "Subscribed" : "Unsubscribed";
		subButton.setText(buttonText);
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

		TournamentSubscriptionManager tsm = new TournamentSubscriptionManager();
		if(!tsm.isTournamentSubbed(this, tournament))
		{

			tsm.subToTournament(tournament, this);
		}
		else
		{
			tsm.unsubFromTournament(this, tournament);
		}
		setSubButtonText();
	}
	/*
	private void displayData(String data)
	{
		String[] dataArr = data.split(",");
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
		
		
		Pattern patt = Pattern.compile("\"");
		Tournament tournament = new Tournament();
		for(int i = 0; i < dataArr.length; i++)
		{
			Matcher m = patt.matcher(dataArr[i]);
			String entry = dataArr[i];
			dataArr[i] = m.replaceAll("");
			if(entry.contains("name"))
			{
				patt = Pattern.compile(".*name.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				patt = Pattern.compile("\"");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				tournament.setName(entry);
			}
			if(entry.contains("location"))
			{
				patt = Pattern.compile(".*location.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				tournament.setLocation(entry);
			}
			if(entry.contains("format"))
			{
				patt = Pattern.compile(".*format.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				tournament.setFormat(entry);
			}
			if(entry.contains("subscribed"))
			{
				patt = Pattern.compile(".*subscribed.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				tournament.setSubscribed(entry);
			}
			if(entry.contains("start_date"))
			{
				Log.i("here", entry);
				patt = Pattern.compile(".*start_date.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				//Log.i("here", entry);
				if(entry.charAt(entry.length() - 1) == '}')
					entry = entry.substring(0, (entry.length() - 1)); //get rid of erroneous } that regex won't remove
				Log.i("here2", entry);
				tournament.setStartDate(entry);
			}
			if(entry.contains("links"))
			{
				patt = Pattern.compile(".*links.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				if(entry.charAt(entry.length() - 1) == '\"')
					entry = entry.substring(0, (entry.length() - 1));
				if(entry.equals("null"))
					tournament.setLinks("No links are available for this tournament");
				else
					tournament.setLinks(entry);
			}
			
			if(entry.contains("sponsor"))
			{
				Log.i("sponsors", entry);
				patt = Pattern.compile(".*sponsor.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				Log.i("sponsors1", entry);
				tournament.setSponsor(entry);
			}
			if(entry.contains("entry_reqs"))
			{
				Log.i("entry_reqs", entry);
				patt = Pattern.compile(".*entry_reqs.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				Log.i("entry_reqs1", entry);
				tournament.setEntryReqs(entry);
			}
			if(entry.contains("ongoing"))
			{
				patt = Pattern.compile(".*ongoing.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				tournament.setOngoing(entry);
			}
			if(entry.contains("past"))
			{
				patt = Pattern.compile(".*past.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				tournament.setPast(entry);
			}
			if(entry.contains("future"))
			{
				patt = Pattern.compile(".*future.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				tournament.setFuture(entry);
			}
			if(entry.contains("prizes"))
			{
				Log.i("prizes", entry);
				patt = Pattern.compile(".*prizes.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				Log.i("prizes1", entry);
				tournament.setPrizes(entry);
			}
			
		}
		tvTitle.setText(tournament.getName());
		tvDate.setText("Tournament date: " + tournament.getStartDate());
		tvLinks.setText("More info: " + tournament.getLinks());
		tvLocation.setText("Location: " + tournament.getLocation());
		tvFormat.setText("Format: " + tournament.getFormat());
		tvSponsor.setText("Sponsors: " + tournament.getSponsor());
		tvEntryReqs.setText("Entry requirements: " + tournament.getEntryReqs());
		tvPrizes.setText("Prize(s): " + tournament.getPrizes());
		tvStatus.setText("Status: " + tournament.getStatus());
	}*/

}
