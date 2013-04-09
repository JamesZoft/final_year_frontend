package com.james.erebus.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.james.erebus.JSONJava.JSONException;
import com.james.erebus.JSONJava.JSONObject;
import com.james.erebus.core.Match;

public class MiscJsonHelpers {

	/**
	 * @param args
	 */
	public static String getValuesFromJsonObject(JSONObject o)
	{
		String obj = o.toString();
		String[] values = obj.split(",");
		Pattern patt = Pattern.compile(".*:");
		obj = "";
		for(int i = 0; i < values.length; i++)
		{
			Matcher m = patt.matcher(values[i]);
			values[i] = m.replaceFirst(",");
			obj += values [i];
		}
		patt = Pattern.compile("\""); //get rid of extraneous quote marks and brackets
		Matcher m = patt.matcher(obj);
		obj = m.replaceAll("");
		patt = Pattern.compile(Pattern.quote("}"));
		Matcher m2 = patt.matcher(obj);
		obj = m2.replaceAll("");
		patt = Pattern.compile(",");
		Matcher m3 = patt.matcher(obj);
		obj = m3.replaceFirst("");
		return obj;
	}
	
	public static JSONObject matchToJson(Match m)
	{
		JSONObject match = new JSONObject();
		try {
			match.put("date", m.getDate());
			match.put("links", m.getLinks());
			match.put("parentTournament", m.getParentTourny());
			match.put("player1", m.getPlayer1());
			match.put("player2", m.getPlayer2());
			match.put("status", m.getStatus());
			match.put("time", m.getTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return match;
	}
	
	
	
	public static Match jsonToMatch(JSONObject obj)
	{
		Match match = new Match();

		try{
			match.setPlayer1(obj.getString("player1"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for player1");
		}
		try{
			match.setPlayer2(obj.getString("player2"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for player2");
		}
		try{
			match.setParentTourny(obj.getString("parentTournament"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for parentTournament");
		}
		try{
			match.setDate(obj.getString("date"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for date");
		}
		try{
			match.setLinks(obj.getString("links"));
		}
		catch(JSONException e)
		{
			System.out.println("No value for links");
		}
		try{
			String entry = obj.getString("time");
			Pattern patt = Pattern.compile(".*T");
			Matcher m = patt.matcher(entry);
			entry = m.replaceAll("");
			patt = Pattern.compile("Z");
			m = patt.matcher(entry);
			entry = m.replaceAll("");
			match.setTime(entry);
		}
		catch(JSONException e)
		{
			System.out.println("No value for time");
		}

		return match;

	}
	
	/*matchactivity
	  private void filterResults()
	  {
		  @SuppressWarnings("unused")
		  TableLayout matches = (TableLayout)findViewById(R.id.tournyTable);
		  if(selectedItems == null)
		  {
			  throw new IllegalStateException("selectedItems null");
		  }
		  if(selectedItems.contains(TournyMatchOptions.subbed))
		  {
			  Log.d("filterResults2", "removed subbed things from tournament things");
		  }
		  if(selectedItems.contains(TournyMatchOptions.unsubbed))
		  {
			  Log.d("filterResults2", "removed subbed things from tournament things");
		  }

		  for(TournyMatchOptions o : selectedItems)
		  {
			  System.out.println("tournyitem: " + o);
		  }
	  }*/
	
	/*tournyact
	  private void filterResults()
	  {
		  //TableLayout matches = (TableLayout)findViewById(R.id.matchButtonsLayout);
		  if(selectedItems == null)
		  {
			  throw new IllegalStateException("selectedItems null");
		  }
		  if(selectedItems.contains(TournyMatchOptions.subbed))
		  {
			  Log.d("filterResults2", "removed subbed things from match things");
		  }
		  if(selectedItems.contains(TournyMatchOptions.unsubbed))
		  {
			  Log.d("filterResults2", "removed unsubbed things from match things");
		  }

		  for(Object o : selectedItems)
		  {
			  System.out.println("matchitem: " +o);
		  }
	  }*/
	
	// try { 

			//} catch (MalformedURLException e) {
			//	e.printStackTrace();
			//} catch (IOException e) {
			//	e.printStackTrace();
			//}

			/*
		  AndroidHttpClient c = AndroidHttpClient.newInstance("");
			HttpGet g;
			try {
				g = new HttpGet(new URI("192.168.0.11:3000"));
				HttpResponse r = c.execute(null, g); 
				Log.i("trying http","" + r.getStatusLine());
				r.getEntity().consumeContent();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/

			/*

	}
	
	
			 */
	/*tournyprefsfrag
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		filterTitleNumber = com.james.erebus.R.string.tournament_filter_prefs;
		filterPrefsNumber = com.james.erebus.R.array.tournament_filter_preferences;
		
		mIsLargeLayout = getResources().getBoolean(com.james.erebus.R.bool.large_layout);
		ArrayList<TournyMatchOptions> items = TournamentActivity.getSelectedItems();
		if(items != null)
			System.out.println("is items empty?:" + items.isEmpty());
		if(items == null || (items.isEmpty()))// Where we track the selected items
		{
			selectedItems = new ArrayList<TournyMatchOptions>();
			System.out.println("empty/null");
		}
		else
		{
			System.out.println("previous items");
			selectedItems = items;
			for(Object o : selectedItems)
			{
				System.out.println("an object: " + o);
			}
			
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title
		builder.setTitle(com.james.erebus.R.string.tournament_filter_prefs)
		// Specify the list array, the items to be selected by default (null for none),
		// and the listener through which to receive callbacks when items are selected
				.setMultiChoiceItems(filterPrefsNumber, generateTickedBoxes(selectedItems),

				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				TournyMatchOptions mp = idToEnum(which);
				if (isChecked) {
					// If the user checked the item, add it to the selected items
					selectedItems.add(mp);
				} else if (selectedItems.contains(mp)) {
					// Else, if the item is already in the array, remove it 
					selectedItems.remove(mp);
				}
			}
		})
		// Set the action buttons
		.setPositiveButton(com.james.erebus.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK, so save the mSelectedItems results somewhere
				// or return them to the component that opened the dialog
				Log.d("dialogLog", "pressed ok!");
				System.out.println("printing mListener:" + mListener);
				if(mListener == null)
					Log.d("nul1", "mlistener null");
				mListener.onDialogPositiveClick(TournamentPreferencesFragment.this);
			}
		})
		.setNegativeButton(com.james.erebus.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Log.d("dialogLog", "pressed cancel!");
				mListener.onDialogNegativeClick(TournamentPreferencesFragment.this);
				//do nothing
			}
		});

		return builder.create();
	}
*/

}
