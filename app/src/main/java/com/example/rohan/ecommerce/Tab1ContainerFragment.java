package com.example.rohan.ecommerce;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;


public class Tab1ContainerFragment extends BaseContainerFragment {

	private boolean mIsViewInited;
	String tag = "hello";

	private static final Field sChildFragmentManagerField;
	static {
		Field f = null;
		try {
			f = Fragment.class.getDeclaredField("mChildFragmentManager");
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
			Log.e("Logged", "Error getting mChildFragmentManager field", e);
		}
		sChildFragmentManagerField = f;
	}

	// Generic newInstance method
	public static Tab1ContainerFragment newInstance(int index) {
		Tab1ContainerFragment f = new Tab1ContainerFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.e("test", "tab 1 oncreateview");
		setRetainInstance(true);
		Log.e("test", "tab 1 container on activity created");
		if (!mIsViewInited) {
			mIsViewInited = true;
			initView();
		}
		return inflater.inflate(R.layout.container_fragment, null);
	}
	

	private void initView()
	{
		Log.e("test", "tab 1 init view");
			//updatesEventsPage.setArguments(getArguments());
			//new YourEventsAsyncTask(updatesEventsPage).execute(getActivity());
			MainFragment mainFragment = new MainFragment();
		if(getArguments()!= null)
		{
			mainFragment.setArguments(getArguments());
		}
		replaceFragmentstart(mainFragment, false); // This is so that container 1 contains the event flow


	}

	// To ADD the required child fragment in the current fragment container
	public void replaceFragment(Fragment fragment, boolean addToBackStack)
	{
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		 transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left, // Contains animation
				R.animator.slide_out_right, R.animator.slide_in_right);

		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment);

		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}

	// To REPLACE the required child fragment in the current fragment container at THE START
	public void replaceFragmentstart(Fragment fragment, boolean addToBackStack)
	{
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		//transaction.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment,tag);// REPLACE and not ADD

		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}


	public void onBack()
	{
		Fragment CurrentTab = getChildFragmentManager().findFragmentByTag(tag);
		boolean isPopFragment = false;


		isPopFragment = ((BaseContainerFragment) CurrentTab).popFragment();
		if (!isPopFragment) {
			//FragmentActivity.finish();
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();

		if (sChildFragmentManagerField != null) {
			try {
				sChildFragmentManagerField.set(this, null);
			} catch (Exception e) {
				Log.e("Logged", "Error setting mChildFragmentManager field", e);
			}
		}
	}





}
