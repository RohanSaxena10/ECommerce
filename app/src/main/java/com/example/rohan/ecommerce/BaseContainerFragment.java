package com.example.rohan.ecommerce;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;


public class BaseContainerFragment extends Fragment {

	// To replace with the current fragment in the container with the required fragment and with animation
	public void replaceFragment(Fragment fragment, boolean addToBackStack)
	{
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left,
				R.animator.slide_out_right, R.animator.slide_in_right);
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment); // Note that this method is using REPLACE and not ADD
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}

	// To pop the current fragment and go back to the previous fragment of the current container
	public boolean popFragment()
	{
		Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
		boolean isPop = false;
		if (getChildFragmentManager().getBackStackEntryCount() > 0) {
			isPop = true;
			getChildFragmentManager().popBackStack();
		}
		return isPop;
	}

	// To pop the entire back stack and go back to the first fragment in the container
	public void popentirebackstack()
	{
		getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // This means that pop til the bottom of stack is reached
	}


}
