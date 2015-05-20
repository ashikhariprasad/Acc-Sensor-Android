package com.sjsu.proj.accsensor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ashik on 4/1/2015.
 */
public class HomeFragment extends Fragment {
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            userId = extras.getString("EXTRA_USER_ID");
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home,parent,false);

        Button button = (Button)v.findViewById(R.id.btnStartDemo);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),DetectorActivity.class);
                i.putExtra("EXTRA_USER_ID",userId);
                startActivity(i);
            }
        });

        Button updateButton = (Button)v.findViewById(R.id.btnUpdateProfile);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateIntent = new Intent(getActivity(),UpdateProfileActivity.class);
                updateIntent.putExtra("EXTRA_USER_ID",userId);
                startActivity(updateIntent);
            }
        });


        return v;
    }
}
