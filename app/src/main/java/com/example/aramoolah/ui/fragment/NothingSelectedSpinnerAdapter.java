package com.example.aramoolah.ui.fragment;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;

public class NothingSelectedSpinnerAdapter implements SpinnerAdapter {
    protected static final int EXTRA = 1;
    protected SpinnerAdapter adapter;
    protected Context context;
    protected int nothingSelectedLayout;
    protected int nothingSelectedDropdownLayout;
    protected LayoutInflater layoutInflater;

    /**
     * Use this constructor to have NO 'Select One...' item, instead use
     * the standard prompt or nothing at all.
     * @param spinnerAdapter wrapped Adapter.
     * @param nothingSelectedLayout layout for nothing selected, perhaps
     * you want text grayed out like a prompt...
     * @param context
     */
    public NothingSelectedSpinnerAdapter(
            SpinnerAdapter spinnerAdapter,
            int nothingSelectedLayout,
            Context context
    ) {

        this(spinnerAdapter, nothingSelectedLayout, -1, context);
    }

    /**
     * Use this constructor to Define your 'Select One...' layout as the first
     * row in the returned choices.
     * If you do this, you probably don't want a prompt on your transactionType_sp or it'll
     * have two 'Select' rows.
     * @param spinnerAdapter wrapped Adapter. Should probably return false for isEnabled(0)
     * @param nothingSelectedLayout layout for nothing selected, perhaps you want
     * text grayed out like a prompt...
     * @param nothingSelectedDropdownLayout layout for your 'Select an Item...' in
     * the dropdown.
     * @param context
     */
    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
                                         int nothingSelectedLayout, int nothingSelectedDropdownLayout, Context context) {
        this.adapter = spinnerAdapter;
        this.context = context;
        this.nothingSelectedLayout = nothingSelectedLayout;
        this.nothingSelectedDropdownLayout = nothingSelectedDropdownLayout;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        // Android BUG! http://code.google.com/p/android/issues/detail?id=17128 -
        // Spinner does not support multiple view types
        if (i == 0) {
            return nothingSelectedDropdownLayout == -1 ?
                    new View(context) :
                    getNothingSelectedDropdownView(viewGroup);
        }

        // Could re-use the convertView if possible, use setTag...
        return adapter.getDropDownView(i - EXTRA, null, viewGroup);
    }
    /**
     * Override this to do something dynamic... For example, "Pick your favorite
     * of these 37".
     * @param viewGroup
     * @return
     */
    protected View getNothingSelectedDropdownView(ViewGroup viewGroup) {
        return layoutInflater.inflate(nothingSelectedDropdownLayout, viewGroup, false);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        adapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        adapter.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public int getCount() {
        int count = adapter.getCount();
        return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public Object getItem(int i) {
        return i == 0 ? null : adapter.getItem(i - EXTRA);
    }

    @Override
    public long getItemId(int i) {
        return i >= EXTRA ? adapter.getItemId(i - EXTRA) : i - EXTRA;
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // This provides the View for the Selected Item in the Spinner, not
        // the dropdown (unless dropdownView is not set).
        if (i == 0) {
            return getNothingSelectedView(viewGroup);
        }
        return adapter.getView(i - EXTRA, null, viewGroup); // Could re-use
        // the convertView if possible.
    }

    /**
     * View to show in Spinner with Nothing Selected
     * Override this to do something dynamic... e.g. "37 Options Found"
     * @param viewGroup
     * @return
     */
    protected View getNothingSelectedView(ViewGroup viewGroup) {
        return layoutInflater.inflate(nothingSelectedLayout, viewGroup, false);
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }
}
