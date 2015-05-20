package com.sjsu.proj.accsensor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashik on 4/14/2015.
 */
public class GPSLocation extends Service implements LocationListener{

    private Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;
    protected LocationManager locationManager;
    Handler mHandler;
    ResultReceiver resultReceiver;

   /*public GPSLocation(Context context, Handler h)
    {
        this.mContext = context;
        this.mHandler = h;
        Log.d("GPS CONSTRUCTOR","DONE");
        //mHandler = new Handler(context.getMainLooper());
    }*/
    public GPSLocation(){}

    public void onCreate(){
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            Log.d("LINE","LINE 1");
            resultReceiver = intent.getParcelableExtra("Receiver");
            mContext = this;
            Log.d("LINE","LINE 1");

            locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
            Log.d("LINE","LINE 2");

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.d("LINE","LINE 3");

            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d("LINE","LINE 4");

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.d("No Network", "No Network");
            } else {
                this.canGetLocation = true;

                //First get location from Network Provider
                if (isNetworkEnabled) {
                    Log.d("isNetworkEnabled", "isNetworkEnabled");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateGPSCoordinates();
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            updateGPSCoordinates();
                        }
                    }
                }
                Log.d("GPS DATA", String.valueOf(this.latitude)+String.valueOf(this.longitude)+this.getCountryName(mContext)+
                        this.getLocality(mContext)+this.getPostalCode(mContext)+this.getAddressLine(mContext));
                Bundle bundle = new Bundle();
                String[] sendString = {String.valueOf(this.latitude),
                        String.valueOf(this.longitude),this.getCountryName(mContext),
                        this.getLocality(mContext),this.getPostalCode(mContext),this.getAddressLine(mContext)};
                bundle.putStringArray("GPS",sendString);
                resultReceiver.send(100,bundle);

                stopUsingGPS();
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            Log.d("Unable To Get Location", e.toString());
        }
        return 1;
     }

    public void updateGPSCoordinates()
    {
        if (location != null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    public void stopUsingGPS()
    {
        if (locationManager != null)
        {
            locationManager.removeUpdates(GPSLocation.this);
        }
    }
    public double getLatitude()
    {
        if (location != null)
        {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude()
    {
        if (location != null)
        {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public List<Address> getGeoCoderAddress(Context context)
    {
        if (location != null)
        {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            try
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                return addresses;
            }
            catch (IOException e)
            {
                //e.printStackTrace();
                Log.e("Error : GeoCoder", "Impossible to connect to Geocoder", e);
            }
        }

        return null;
    }

    public String getAddressLine(Context context)
    {
        List<Address> addresses = getGeoCoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);

            return addressLine;
        }
        else
        {
            return null;
        }
    }

    /**
     * Try to get Locality
     * @return null or locality
     */
    public String getLocality(Context context)
    {
        List<Address> addresses = getGeoCoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            String locality = address.getLocality();

            return locality;
        }
        else
        {
            return null;
        }
    }

    /**
     * Try to get Postal Code
     * @return null or postalCode
     */
    public String getPostalCode(Context context)
    {
        List<Address> addresses = getGeoCoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            String postalCode = address.getPostalCode();

            return postalCode;
        }
        else
        {
            return null;
        }
    }

    /**
     * Try to get CountryName
     * @return null or postalCode
     */
    public String getCountryName(Context context)
    {
        List<Address> addresses = getGeoCoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            String countryName = address.getCountryName();

            return countryName;
        }
        else
        {
            return null;
        }
    }
    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        stopUsingGPS();
        Log.d("ON_DESTROY","GPS SERVICE DESTROYED");
    }
}
