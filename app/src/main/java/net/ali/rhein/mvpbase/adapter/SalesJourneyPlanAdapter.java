package net.ali.rhein.mvpbase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.models.SalesJourneyPlanData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhein on 4/1/18.
 */

public class SalesJourneyPlanAdapter extends RecyclerView.Adapter<SalesJourneyPlanAdapter.MyHolder> implements Filterable {

    private Context context;
    private int rowLayout;
    private LayoutInflater inflater;
    private List<SalesJourneyPlanData> salesPlanDataList, newList;

    int currentPos = 0;

    public SalesJourneyPlanAdapter(List<SalesJourneyPlanData> data, int rowLayout, Context context){
        this.context                  = context;
        this.inflater                 = LayoutInflater.from(context);
        this.salesPlanDataList        = data;
        this.rowLayout                = rowLayout;
        this.newList                  = data;
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        TextView textViewNamaProvinsi, textViewNamaKabupaten, textViewTanggalPlan;

        MyHolder(View itemView) {
            super(itemView);
            textViewNamaProvinsi    = (TextView)itemView.findViewById(R.id.text_view_nama_provinsi);
            textViewNamaKabupaten   = (TextView)itemView.findViewById(R.id.text_view_nama_kabupaten);
            textViewTanggalPlan     = (TextView)itemView.findViewById(R.id.text_view_tanggal_plan);
        }
    }

    @Override
    public SalesJourneyPlanAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textViewNamaProvinsi.setText(salesPlanDataList.get(position).getNamaProvinsi());
        holder.textViewNamaKabupaten.setText(salesPlanDataList.get(position).getNamaKabupaten());
        holder.textViewTanggalPlan.setText("Plan Date : "+ salesPlanDataList.get(position).getTglPlan());
    }

    @Override
    public int getItemCount() {
        return salesPlanDataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    salesPlanDataList = newList;
                }
                else {
                    ArrayList<SalesJourneyPlanData> tempCompList = new ArrayList<>();
                    for (SalesJourneyPlanData salesJourneyPlan : newList) {

                        if (salesJourneyPlan.getNamaKabupaten().toLowerCase().contains(charString)) {
                            //Log.e("SalesJourneyAdapter : ", "IN IF SAMA");
                            tempCompList.add(salesJourneyPlan);
                        }
                    }
                    salesPlanDataList = tempCompList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = salesPlanDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                salesPlanDataList = (ArrayList<SalesJourneyPlanData>)filterResults.values;
                Log.e("Before ", "notifyDataSetChanged");
                notifyDataSetChanged();
            }
        };
    }
}
