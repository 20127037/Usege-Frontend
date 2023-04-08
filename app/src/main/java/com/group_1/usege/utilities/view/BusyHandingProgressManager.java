package com.group_1.usege.utilities.view;

import androidx.fragment.app.FragmentTransaction;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <pre>
 * Manager of {@link BusyHandlingProgressFragment} (for reusing fragment purpose)
 * Use {@link Inject} to inject the singleton instance
 * </pre>
 */
@Singleton
public class BusyHandingProgressManager {

    public BusyHandingProgressManager()
    {

    }
    private BusyHandlingProgressFragment wrappedFragment;
    private static final String TAG = "BusyProgressTag";

    /**
     * Show {@link BusyHandlingProgressFragment} occupying current screen
     * @param transaction current activity fragment transaction
     */
    public void show(FragmentTransaction transaction)
    {
        if (wrappedFragment == null)
            wrappedFragment = new BusyHandlingProgressFragment();
        wrappedFragment.show(transaction, TAG);
    }

    /**
     * Hide {@link BusyHandlingProgressFragment}
     */
    public void hide()
    {
        wrappedFragment.dismiss();
    }
}
