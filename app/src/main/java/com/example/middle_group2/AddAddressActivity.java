package com.example.middle_group2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.AddressAdapter;
import com.example.implement.AddressImp;
import com.example.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import entities.Address;

public class AddAddressActivity extends AppCompatActivity {

    private String baseUrlProvince = "https://provinces.open-api.vn/api/";
    private AddressAdapter cityAdapter;
    private AddressAdapter districtAdapter, wardAdapter;

    // component
    Spinner spinnerCity;
    Spinner spinnerDistrict;
    Spinner spinnerWard;
    Button btnAddAddress;
    EditText edtFullName, edtPhone, edtStreet;

    private Context context = this;

    // data
    private ArrayList<HashMap<String, String>> listCity = new ArrayList<>();
    private ArrayList<HashMap<String, String>> listDistrict = new ArrayList<>();
    private ArrayList<HashMap<String, String>> listWard = new ArrayList<>();

    // imp
    AddressImp addressImp = new AddressImp();

    private String codeCity, city;
    private String codeDistrict, district;
    private String codeWard, ward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);
        setControl();
        initial();
        handleOnClickAddAddress();

    }

    private void initial() {
        districtAdapter = new AddressAdapter(context, R.layout.address_item, listDistrict);
        wardAdapter = new AddressAdapter(context, R.layout.address_item, listWard);
        spinnerDistrict.setAdapter(districtAdapter);
        spinnerWard.setAdapter(wardAdapter);
        initialProvince();
    }

    private void setControl() {
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        spinnerWard = findViewById(R.id.spinnerWard);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtStreet = findViewById(R.id.edtStreet);
    }

    private void initialProvince() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,baseUrlProvince, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length() ; i++) {
                        String name = response.getJSONObject(i).getString("name");
                        String code = response.getJSONObject(i).getString("code");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", name);
                        map.put("code", code);
                        listCity.add(map);
                    }
                } catch (JSONException e) {
                    System.out.println("Error when get data from api" + e);
                }
                cityAdapter = new AddressAdapter(context, R.layout.address_item, listCity);
                spinnerCity.setAdapter(cityAdapter);
                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        codeCity = listCity.get(i).get("code");
                        city = listCity.get(i).get("name");
                        listDistrict.clear();
                        initialDistrict(codeCity);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAddressActivity.this, "Call API Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void initialDistrict(String code) {
        String url =  baseUrlProvince + "p/" + code + "?depth=2";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray districts = response.getJSONArray("districts");
                    for (int i = 0; i < districts.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", districts.getJSONObject(i).getString("name"));
                        map.put("code", districts.getJSONObject(i).getString("code"));
                        listDistrict.add(map);
                    }
                    districtAdapter.notifyDataSetChanged();
                    spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            codeDistrict = listDistrict.get(i).get("code");
                            district = listDistrict.get(i).get("name");
                            initialWard(codeDistrict);
                            listWard.clear();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Call API Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void initialWard(String code) {
        String url =  baseUrlProvince + "d/" + code + "?depth=2";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray wards = response.getJSONArray("wards");
                    for (int i = 0; i < wards.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", wards.getJSONObject(i).getString("name"));
                        map.put("code", wards.getJSONObject(i).getString("code"));
                        listWard.add(map);
                    }
                    wardAdapter.notifyDataSetChanged();

                    spinnerWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            codeWard = listWard.get(i).get("code");
                            ward = listWard.get(i).get("name");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Call API Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void handleOnClickAddAddress() {
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idAddress = generateId();
                String idUser = LoginActivity.idUserLogin;
                String fullName = edtFullName.getText().toString();
                String phoneNumber = edtPhone.getText().toString();
                String street = edtStreet.getText().toString();
                if(fullName.equals("") || phoneNumber.equals("") || street.equals("")) {
                    Toast.makeText(context, "Please enter full your information", Toast.LENGTH_SHORT).show();
                } else {
                    Address address = new Address(idAddress, idUser, phoneNumber,
                            Integer.parseInt(codeCity), city,
                            Integer.parseInt(codeDistrict), district,
                            Integer.parseInt(codeWard), ward,
                            false, street, fullName);

                    if(addressImp.save(address)) {
                        Toast.makeText(context, "Add new address successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(context, AddressActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Add new address failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String generateId() {
        String id;
        Boolean isExistID = false;
        do {
            id = new Helper().generateRandomString();
            isExistID = addressImp.isExistId(id, "id_address");
        } while(isExistID);

        return id;
    }

}