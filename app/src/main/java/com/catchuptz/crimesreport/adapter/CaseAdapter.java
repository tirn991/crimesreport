package com.catchuptz.crimesreport.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.catchuptz.crimesreport.R;
import com.catchuptz.crimesreport.SingleCase;
import com.catchuptz.crimesreport.model.CaseItem;
import com.catchuptz.crimesreport.util.SharedPrefManager;

import java.util.ArrayList;

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.ViewHolder> {
    private ArrayList<CaseItem> cases;
    private Context context;

    public CaseAdapter(ArrayList<CaseItem> cases, Context context) {
        this.context=context;
        this.cases=cases;
    }

    @Override
    public CaseAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_case_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CaseAdapter.ViewHolder viewHolder, final int i) {


        final String status = cases.get(i).getStatus();
        final String file_number = cases.get(i).getFile_number();
        if (status.equals("1")){
            viewHolder.status.setText("STATUS: Pending");
        }if (status.equals("2")){
            viewHolder.status.setText("STATUS: On Going");
        }if (status.equals("3")){
            viewHolder.status.setText("STATUS: Closed");
        }if (status.equals("4")){
            viewHolder.status.setText("STATUS: Deleted");
        }

        if (file_number.equals("")){
            viewHolder.file_number.setText("");
        }if (!file_number.equals("")){
            viewHolder.file_number.setText(file_number);
        }
        viewHolder.name.setText(cases.get(i).getName());
        viewHolder.title.setText(cases.get(i).getTitle());
        viewHolder.tribe.setText(cases.get(i).getTribe());
        viewHolder.age.setText(cases.get(i).getAge());
        viewHolder.residence.setText(cases.get(i).getResidence());
        viewHolder.religion.setText(cases.get(i).getReligion());
        viewHolder.phone.setText(cases.get(i).getPhone());
        viewHolder.case_details.setText(cases.get(i).getCase_details());

        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        String actor = sharedPrefManager.getSpActorType();

        if (actor.equals("police")){
            viewHolder.deleteActn.setText("CLOSE THE CASE");
            viewHolder.deleteActn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                            //set message, title, and icon
                            .setTitle("Information")
                            .setMessage("Are you Sure To Close this Case as its already finished? ")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myQuittingDialogBox.show();
                }
            });
        }else{
            viewHolder.deleteActn.setVisibility(View.GONE);
        }

        viewHolder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (file_number.equals("")){
//                    AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
//                            //set message, title, and icon
//                            .setTitle("Information")
//                            .setMessage("This Case is not Yet Attended")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .create();
//                    myQuittingDialogBox.show();
//                }else {
                    Intent veh = new Intent(context, SingleCase.class);
                    veh.putExtra("name", cases.get(i).getName());
                    veh.putExtra("case_id", cases.get(i).getCase_id());
                    veh.putExtra("case_user_id", cases.get(i).getUser_id());
                    veh.putExtra("casetitle", cases.get(i).getTitle());
                    veh.putExtra("casedetails", cases.get(i).getCase_details());
                    veh.putExtra("userage", cases.get(i).getAge());
                    veh.putExtra("residence", cases.get(i).getResidence());
                    veh.putExtra("file_number", cases.get(i).getFile_number());
                    veh.putExtra("casestatus", status);
                    veh.putExtra("phonenumber", cases.get(i).getPhone());
                    context.startActivity(veh);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView case_details, file_number, name, tribe, age, religion, residence, phone, status, title, btnFollow, deleteActn;


        public ViewHolder(View view) {
            super(view);
            case_details = (TextView)view.findViewById(R.id.case_details);
            file_number = (TextView)view.findViewById(R.id.file_number);
            name = (TextView)view.findViewById(R.id.name);
            tribe = (TextView)view.findViewById(R.id.tribe);
            age = (TextView)view.findViewById(R.id.age);
            religion = (TextView)view.findViewById(R.id.religion);
            residence = (TextView)view.findViewById(R.id.residence);
            phone = (TextView)view.findViewById(R.id.phone);
            status = (TextView)view.findViewById(R.id.status);
            title = (TextView)view.findViewById(R.id.title);
            btnFollow = (TextView)view.findViewById(R.id.btnFollow);
            deleteActn = (TextView)view.findViewById(R.id.deleteActn);
        }
    }

}