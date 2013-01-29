package com.james.erebus;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

public class ParentPreferencesFragment extends DialogFragment {
	
	
	
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
	protected NoticeDialogListener mListener;
    

    @SuppressWarnings("rawtypes")
	protected static ArrayList selectedItems;
	protected boolean mIsLargeLayout;
	
	protected int filterPrefsNumber;
	protected int filterTitleNumber;
	


	
	public void showDialog() {
	    FragmentManager fragmentManager = getFragmentManager();
	    MatchPreferencesFragment newFragment = new MatchPreferencesFragment();
	    
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
	
	protected TournyMatchOptions idToEnum(int id)
	{
		switch(id)
		{
		case 0: return TournyMatchOptions.subbed;
		case 1: return TournyMatchOptions.unsubbed; 
		case 2: return TournyMatchOptions.ongoing; 
		case 3: return TournyMatchOptions.future; 
		case 4: return TournyMatchOptions.past; 
		default: return null;
		}
	}
	
	public boolean[] generateTickedBoxes(@SuppressWarnings("rawtypes") ArrayList prefs)
	{
		boolean[] bArr = new boolean[5];
		int i = 0;
		for(TournyMatchOptions mp : TournyMatchOptions.values())
		{
			bArr[i] = prefs.contains(mp);
			i++;
		}
		return bArr;
	}

	
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
	@SuppressWarnings("rawtypes")
	public static ArrayList getSelectedItems()
	{
		return selectedItems;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
		ArrayList items = MatchActivity.getSelectedItems();
		if(items != null)
			System.out.println("is items empty?:" + items.isEmpty());
		if(items == null || (items.isEmpty()))// Where we track the selected items
		{
			selectedItems = new ArrayList();
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
		builder.setTitle(filterTitleNumber)
		// Specify the list array, the items to be selected by default (null for none),
		// and the listener through which to receive callbacks when items are selected
				.setMultiChoiceItems(filterPrefsNumber, generateTickedBoxes(selectedItems),

				new DialogInterface.OnMultiChoiceClickListener() {
			@SuppressWarnings("unchecked")
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
				mListener.onDialogPositiveClick(ParentPreferencesFragment.this);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Log.d("dialogLog", "pressed cancel!");
				mListener.onDialogNegativeClick(ParentPreferencesFragment.this);
				//do nothing
			}
		});

		return builder.create();
	}


}
