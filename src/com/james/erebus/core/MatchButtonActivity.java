package com.james.erebus.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
		//Log.i("intent action values", this.getIntent().getStringExtra("com.james.erebus.MatchButtonActivity.dataValues"));
		displayData(this.getIntent().getStringExtra("com.james.erebus.MatchButtonActivity.dataValues"));
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
	
	private void displayData(String data)
	{
		String[] dataArr = data.split(",");
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
		
		
		Pattern patt = Pattern.compile("\"");
		match = new Match();
		for(int i = 0; i < dataArr.length; i++)
		{
			Matcher m = patt.matcher(dataArr[i]);
			String entry = dataArr[i];
			dataArr[i] = m.replaceAll("");
			Log.i("as", entry);
			if(entry.contains("player1"))
			{
				patt = Pattern.compile(".*player1.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				match.setPlayer1(entry);
			}
			if(entry.contains("player2"))
			{
				patt = Pattern.compile(".*player2.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				match.setPlayer2(entry);
			}
			if(entry.contains("parentTournament"))
			{
				patt = Pattern.compile(".*parentTournament.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				match.setParentTourny(entry);
			}
			if(entry.contains("date"))
			{
				Log.i("here", entry);
				patt = Pattern.compile(".*date.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				//Log.i("here", entry);
				if(entry.charAt(entry.length() - 1) == '}')
					entry = entry.substring(0, (entry.length() - 1)); //get rid of erroneous } that regex won't remove
				Log.i("here2", entry);
				match.setDate(entry);
			}
			if(entry.contains("links"))
			{
				patt = Pattern.compile(".*links.*:");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				if(entry.equals("null"))
					match.setLinks("No links are available for this match");
				else
					match.setLinks(entry);
			}
			if(entry.contains("time"))
			{
				patt = Pattern.compile(".*time.*T");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				patt = Pattern.compile("Z");
				m = patt.matcher(entry);
				entry = m.replaceAll("");
				match.setTime(entry);
			}
		}
		tvTitle.setText(match.getPlayer1() + " vs " + match.getPlayer2());
		tvDate.setText("Match date: " + match.getDate());
		tvTime.setText("Match time: " + match.getTime());
		tvLinks.setText(match.getLinks());
		tvParentTourny.setText(match.getParentTourny());
	}

}
