package com.james.erebus.core;

import java.util.ArrayList;



import android.app.Activity;
import android.app.DialogFragment;

public abstract class ParentPreferencesFragment extends DialogFragment {
	
	
	
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
	protected NoticeDialogListener mListener;
    

	//protected static ArrayList<TournyMatchOptions> selectedItems;
	protected boolean mIsLargeLayout;
	
	protected int filterPrefsNumber;
	protected int filterTitleNumber;
	


	
	
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
