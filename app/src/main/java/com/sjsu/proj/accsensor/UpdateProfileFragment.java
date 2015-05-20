package com.sjsu.proj.accsensor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sjsu.proj.accsensor.UserManager.AsyncUpdateProfile;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateProfileFragment extends Fragment implements AsyncResponse{

    EditText mName, mAge, mGender, mAddress, mEContact1Name, mEContact1Email, mEContact1Phone,
            mEContact2Name, mEContact2Email, mEContact2Phone,mEContact3Name, mEContact3Email,
            mEContact3Phone;

    String sName, sAge, sGender, sAddress, sEContact1Name, sEContact1Email, sEContact1Phone,
            sEContact2Name, sEContact2Email, sEContact2Phone,sEContact3Name, sEContact3Email,
            sEContact3Phone;

    Button mUpdateConfirm,mUpdateCancel;
    Context mContext;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_profile,parent,false);
        mName = (EditText)v.findViewById(R.id.name);
        mAge = (EditText)v.findViewById(R.id.age);
        mGender = (EditText)v.findViewById(R.id.gender);
        mAddress = (EditText)v.findViewById(R.id.address);
        mEContact1Name = (EditText)v.findViewById(R.id.econtact1Name);
        mEContact1Email = (EditText)v.findViewById(R.id.econtact1Email);
        mEContact1Phone = (EditText)v.findViewById(R.id.econtact1Phone);

        mEContact2Name = (EditText)v.findViewById(R.id.econtact2Name);
        mEContact2Email = (EditText)v.findViewById(R.id.econtact2Email);
        mEContact2Phone = (EditText)v.findViewById(R.id.econtact2Phone);

        mEContact3Name = (EditText)v.findViewById(R.id.econtact3Name);
        mEContact3Email = (EditText)v.findViewById(R.id.econtact3Email);
        mEContact3Phone = (EditText)v.findViewById(R.id.econtact3Phone);

        mUpdateConfirm = (Button)v.findViewById(R.id.btnUpdateConfirm);
        mUpdateCancel = (Button)v.findViewById(R.id.btnUpdateCancel);

        mContext = getActivity();
        final UpdateProfileFragment ref = this;

        mUpdateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = mName.getText().toString();
                sAge = mAge.getText().toString();
                sGender = mGender.getText().toString();
                sAddress = mAddress.getText().toString();
                sEContact1Name = mEContact1Name.getText().toString();
                sEContact1Phone = mEContact1Phone.getText().toString();
                sEContact1Email = mEContact1Email.getText().toString();
                sEContact2Name = mEContact2Name.getText().toString();
                sEContact2Phone = mEContact2Phone.getText().toString();
                sEContact2Email = mEContact2Email.getText().toString();
                sEContact3Name = mEContact3Name.getText().toString();
                sEContact3Phone = mEContact3Phone.getText().toString();
                sEContact3Email = mEContact3Email.getText().toString();

                AsyncUpdateProfile update = new AsyncUpdateProfile(mContext,ref);
                update.execute(new String[] {sName,sAge,sGender,sAddress,sEContact1Name,
                        sEContact1Phone,sEContact1Email,sEContact2Name,
                        sEContact2Phone,sEContact2Email,sEContact3Name,
                        sEContact3Phone,sEContact3Email,userId});
            }
        });
        mUpdateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void postRegistrationHandler(Object s) {
        JSONObject updateResponse = (JSONObject) s;
        String strResponse = null;
        if(updateResponse != null){
            try {
                strResponse = updateResponse.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (strResponse.equalsIgnoreCase("data updated")) {
                //getFragmentManager().popBackStackImmediate();
                getActivity().onBackPressed();
            } else {
                Toast.makeText(getActivity(), "Failed to update", Toast.LENGTH_SHORT).show();
                //getFragmentManager().popBackStackImmediate();
                getActivity().onBackPressed();
            }
        }
        else{
            Toast.makeText(getActivity(), "Failed to update", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
