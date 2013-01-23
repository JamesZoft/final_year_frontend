package com.james.erebus;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.util.Log;


public class PreferencesFragment extends DialogFragment {

	private ArrayList selectedItems;
	private boolean mIsLargeLayout;

	
	public void showDialog() {
	    FragmentManager fragmentManager = getFragmentManager();
	    PreferencesFragment newFragment = new PreferencesFragment();
	    
	    if (mIsLargeLayout) {
	        // The device is using a large layout, so show the fragment as a dialog
	        newFragment.show(fragmentManager, "dialog");
	    } else {
	        // The device is smaller, so show the fragment fullscreen
	        FragmentTransaction transaction = fragmentManager.beginTransaction();
	        // For a little polish, specify a transition animation
	        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        // To make it fullscreen, use the 'content' root view as the container
	        // for the fragment, which is always the root view for the activity
	        transaction.add(android.R.id.content, newFragment)
	                   .addToBackStack(null).commit();
	    }
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
		selectedItems = new ArrayList();  // Where we track the selected items
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflator = getActivity().getLayoutInflater();
		builder.setView(inflator.inflate(R.layout.dialog_filter_preferences, null));
		// Set the dialog title
		builder.setTitle(R.string.pick_toppings)
		// Specify the list array, the items to be selected by default (null for none),
		// and the listener through which to receive callbacks when items are selected
		.setMultiChoiceItems(R.array.toppings, null,
				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				if (isChecked) {
					// If the user checked the item, add it to the selected items
					selectedItems.add(which);
				} else if (selectedItems.contains(which)) {
					// Else, if the item is already in the array, remove it 
					selectedItems.remove(Integer.valueOf(which));
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
				MatchActivity.okButtonPressed(selectedItems);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Log.d("dialogLog", "pressed cancel!");
			}
		});

		return builder.create();
	}



}
