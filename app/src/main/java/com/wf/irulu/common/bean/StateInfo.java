package com.wf.irulu.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.wf.irulu.common.utils.ILog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by iRULU on 2016/3/26.
 */
public class StateInfo {


    public static HashMap<String, ArrayList<ListEntity>> parseState(String data) {
        ILog.d("TB", "StateInfo=data=" + data);
        int total = 0;
        HashMap<String, ArrayList<ListEntity>> vMap = new HashMap<String, ArrayList<ListEntity>>();
        try {

            JSONObject vJSONObject = new JSONObject(data);

            total = vJSONObject.getInt("total");
            Object object = vJSONObject.get("list");
            JSONArray vJSONArray = vJSONObject.getJSONArray("list");

            if (vJSONArray == null) {
                return vMap;
            }

            for (int i = 0; i < vJSONArray.length(); i++) {
                JSONObject vItemObject = vJSONArray.getJSONObject(i);
                ListEntity vListEntity = new ListEntity();
                vListEntity.code = vItemObject.getString("code");
                vListEntity.name = vItemObject.getString("name");
                vListEntity.country_code = vItemObject.getString("country_code");
                vListEntity.id = vItemObject.getString("id");
                vListEntity.country_id = vItemObject.getString("country_id");

                ArrayList<ListEntity> vListEntities = vMap.get(vListEntity.country_code);
                if (vListEntities == null) {
                    vListEntities = new ArrayList<ListEntity>();
                    vMap.put(vListEntity.country_code, vListEntities);
                }
                vListEntities.add(vListEntity);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            vMap.clear();
        }
        return vMap;

    }

    public static class ListEntity implements Parcelable {
        private String id;
        private String country_id;
        private String name;
        private String code;
        private String status;
        private String country_code;

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.country_id);
            dest.writeString(this.name);
            dest.writeString(this.code);
            dest.writeString(this.status);
            dest.writeString(this.country_code);
        }

        public ListEntity() {
        }

        protected ListEntity(Parcel in) {
            this.id = in.readString();
            this.country_id = in.readString();
            this.name = in.readString();
            this.code = in.readString();
            this.status = in.readString();
            this.country_code = in.readString();
        }

        public static final Parcelable.Creator<ListEntity> CREATOR = new Parcelable.Creator<ListEntity>() {
            @Override
            public ListEntity createFromParcel(Parcel source) {
                return new ListEntity(source);
            }

            @Override
            public ListEntity[] newArray(int size) {
                return new ListEntity[size];
            }
        };
    }
}
