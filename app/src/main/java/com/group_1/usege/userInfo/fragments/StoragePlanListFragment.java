package com.group_1.usege.userInfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group_1.usege.R;
import com.group_1.usege.userInfo.model.StoragePlan;
import com.group_1.usege.userInfo.model.UserPlan;
import com.group_1.usege.utilities.interfaces.SubmitSignalReceiver;
import com.group_1.usege.utilities.interfaces.ViewDetailsSignalReceiver;

import java.util.function.Consumer;

public class StoragePlanListFragment extends Fragment {

    private static final int GET_DESCRIPTION_LINE = 2;
    private TextView txtCurrentPlanName;
    private StoragePlanListAdapter adapter;
    private ViewDetailsSignalReceiver viewDetailsSignalReceiver;
    private SubmitSignalReceiver submitSignalReceiver;

    public StoragePlanListFragment()
    {
        super(R.layout.fragment_storage_plan_list);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ViewDetailsSignalReceiver)
            this.viewDetailsSignalReceiver = (ViewDetailsSignalReceiver)context;
        if (context instanceof SubmitSignalReceiver)
            this.submitSignalReceiver = (SubmitSignalReceiver)context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtCurrentPlanName = view.findViewById(R.id.txt_current_plan_name);
        ListView planList = view.findViewById(R.id.list_plans);
        adapter = new StoragePlanListAdapter(getContext(), id -> this.viewDetailsSignalReceiver.view(id), id -> this.submitSignalReceiver.submit(id));
        planList.setAdapter(adapter);
    }


    public void setUserPlan(UserPlan userPlan)
    {
        txtCurrentPlanName.setText(userPlan.getCurrentPlan());
        adapter.clear();
        adapter.addAll(userPlan.getPlans());
    }

    private static class StoragePlanListAdapter extends ArrayAdapter<StoragePlan> {

        private final Consumer<String> viewDetailsCallback;
        private final Consumer<String> purchaseCallback;

        public StoragePlanListAdapter(@NonNull Context context, Consumer<String> viewDetailsCallback, Consumer<String> purchaseCallback) {
            super(context, R.layout.item_plan, R.id.txt_name);
            this.viewDetailsCallback = viewDetailsCallback;
            this.purchaseCallback = purchaseCallback;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View createdView = super.getView(position, convertView, parent);
            StoragePlan plan = getItem(position);
            TextView txtName = createdView.findViewById(R.id.txt_name);
            TextView txtPrice = createdView.findViewById(R.id.txt_price);
            TextView txtDescription = createdView.findViewById(R.id.txt_description);
            Button btnViewDetails = createdView.findViewById(R.id.btn_view_details);
            Button btnPurchase = createdView.findViewById(R.id.btn_purchase);

            txtPrice.setText(plan.getFormatPrice(getContext()));
            txtDescription.setText(plan.getDescriptionMultiline(GET_DESCRIPTION_LINE));
            txtName.setText(plan.getName());
            btnViewDetails.setOnClickListener(v -> viewDetailsCallback.accept(plan.getName()));
            btnPurchase.setEnabled(plan.isCanPurchased());
            btnPurchase.setOnClickListener(v -> {
                if (purchaseCallback != null)
                    purchaseCallback.accept(plan.getName());
            });
            return createdView;
        }
    }

}
