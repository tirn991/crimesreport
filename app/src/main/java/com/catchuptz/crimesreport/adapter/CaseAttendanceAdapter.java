package com.catchuptz.crimesreport.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.catchuptz.crimesreport.R;
import com.catchuptz.crimesreport.model.CaseAttendance;
import com.catchuptz.crimesreport.util.SharedPrefManager;

import java.util.ArrayList;

public class CaseAttendanceAdapter extends RecyclerView.Adapter<CaseAttendanceAdapter.ViewHolder> {
private ArrayList<CaseAttendance> cases;
private Context context;

public CaseAttendanceAdapter(ArrayList<CaseAttendance> cases, Context context) {
        this.context=context;
        this.cases=cases;
        }

@Override
public CaseAttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_single_attend, viewGroup, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(CaseAttendanceAdapter.ViewHolder viewHolder, final int i) {


final String text_comment = cases.get(i).getContent();
final String text_user_name = cases.get(i).getAttendername();
final String text_message_time = cases.get(i).getCreated();
final String senderTitle = cases.get(i).getAttendertitle();
final String smsUserID = cases.get(i).getUser_id();

    SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
    String userId = sharedPrefManager.getSpUserid();

    if(smsUserID.equals(userId)){
        viewHolder.layoutme.setVisibility(View.VISIBLE);
        viewHolder.text_comment2.setText(text_comment);
        viewHolder.text_message_time2.setText(text_message_time);
    }else {
        viewHolder.notme.setVisibility(View.VISIBLE);
        viewHolder.text_comment.setText(text_comment);
        viewHolder.text_user_name.setText(text_user_name + "  [ " + senderTitle + " ]");
        viewHolder.text_message_time.setText(text_message_time);
    }
        }

@Override
public int getItemCount() {
        return cases.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView text_user_name, text_comment, text_message_time;
    private TextView  text_comment2, text_message_time2;
    ConstraintLayout layoutme, notme;


    public ViewHolder(View view) {
        super(view);
//        case_details = (TextView)view.findViewById(R.id.case_details);
        text_user_name = (TextView)view.findViewById(R.id.text_message_name);
        text_comment = (TextView)view.findViewById(R.id.text_message_body);
        text_message_time = (TextView)view.findViewById(R.id.text_message_time);

        text_comment2 = (TextView)view.findViewById(R.id.text_message_body2);
        text_message_time2 = (TextView)view.findViewById(R.id.text_message_time2);

        layoutme = view.findViewById(R.id.me);
        notme = view.findViewById(R.id.notme);
    }
}

}
