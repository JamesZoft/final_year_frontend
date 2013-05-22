package com.james.erebus.core;

import java.util.ArrayList;
import android.app.Activity;
import android.app.DialogFragment;

/**
 * Parent class for both PreferencesFragments that implements some common functionality
 */

public abstract class ParentPreferencesFragment extends DialogFragment {
	
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
	protected NoticeDialogListener mListener;
    
	protected boolean mIsLargeLayout;
	
	protected int filterPrefsNumber;
	protected int filterTitleNumber;
	
	
	/**
	 * Converts the selected item ID to one of the enums in {@link com.james.erebus.core.TournyMatchOptions}
	 * @param id the selected item ID
	 * @return The enum entry that the id refers to
	 */
	protected TournyMatchOptions idToEnum(int id)
	{
		switch(id)
		{
		case 0: return TournyMatchOptions.subbed;
		case 1: return TournyMatchOptions.unsubbed;
		case 2: return TournyMatchOptions.ongoing; 
		case 3: return TournyMatchOptions.past; 
		case 4: return TournyMatchOptions.future; 
		default: return null;
		}
	}
	
	/**
	 * Makes sure that boxes that were previously ticked in the filtering Fragment stay ticked when it's re-opened
	 * @param prefs The list of preferences to be used
	 * @return An array of booleans representing ticked/unticked state of the options
	 */
	public boolean[] generateTickedBoxes(ArrayList<TournyMatchOptions> prefs)
	{
		boolean[] bArr = new boolean[TournyMatchOptions.values().length];
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
}