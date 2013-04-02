package com.james.erebus.core;

import java.util.ArrayList;

import com.james.erebus.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class TournamentPreferencesFragment extends ParentPreferencesFragment{
	
	private ArrayList<TournyMatchOptions> selectedItems;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		filterTitleNumber = R.string.tournament_filter_prefs;
		filterPrefsNumber = R.array.tournament_filter_preferences;
		
		mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
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
		builder.setTitle(R.string.tournament_filter_prefs)
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
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Log.d("dialogLog", "pressed cancel!");
				mListener.onDialogNegativeClick(TournamentPreferencesFragment.this);
				//do nothing
			}
		});

		return builder.create();
	}

}
