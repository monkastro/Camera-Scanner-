package com.scanner.camera.phototopdf.papercamerascanner.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.camera.phototopdf.papercamerascanner.Activity.Camera_Scanner_CreationListActivity;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.CreationParentAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Model.CreationParentModel;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.ItemClickSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IDCardFragment extends Fragment {
    CreationParentAdapter adapter;

    ArrayList<CreationParentModel> creationParentModels = new ArrayList<>();
    LinearLayout llCreation;

    TextView noData;
    String path = (Environment.getExternalStorageDirectory().toString() + "/Document Scanner/ID Card");
    int pos;
    RecyclerView rvCreation;


    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.fragment_id_card, viewGroup, false);

        this.rvCreation = (RecyclerView) viewGroup2.findViewById(R.id.rvIdCardCreation);
        this.noData = (TextView) viewGroup2.findViewById(R.id.noMuteData);
        this.llCreation = (LinearLayout) viewGroup2.findViewById(R.id.llCreation);
        ItemClickSupport.addTo(this.rvCreation).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int i, View view) {
                IDCardFragment.this.pos = i;
                if (IDCardFragment.this == null) {
                    Intent intent = new Intent(IDCardFragment.this.getActivity(), Camera_Scanner_CreationListActivity.class);
                    intent.putExtra("folderPath", IDCardFragment.this.creationParentModels.get(i).getFolderPath());
                    IDCardFragment.this.startActivity(intent);
                }  else {
                    Intent intent2 = new Intent(IDCardFragment.this.getActivity(), Camera_Scanner_CreationListActivity.class);
                    intent2.putExtra("folderPath", IDCardFragment.this.creationParentModels.get(i).getFolderPath());
                    IDCardFragment.this.startActivity(intent2);
                }
            }
        });
        return viewGroup2;
    }

    public void bindMyCreation(File file) {
        long j;
        int i;
        String str;
        String str2;
        String str3;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
        File[] listFiles = file.listFiles();
        String str4 = null;
        if (listFiles == null || listFiles.length <= 0) {
            if (this.creationParentModels.size() == 0) {
                this.noData.setVisibility(0);
                this.llCreation.setGravity(17);
                this.noData.setGravity(17);
            }
            str3 = null;
            str2 = null;
            str = null;
            j = 0;
            i = 0;
        } else {
            String str5 = null;
            long j2 = 0;
            int i2 = 0;
            String str6 = null;
            for (int i3 = 0; i3 < listFiles.length; i3++) {
                if (listFiles[i3].isDirectory()) {
                    bindMyCreation(listFiles[i3]);
                } else {
                    i2++;
                    str5 = listFiles[i3].getParent();
                    j2 += listFiles[i3].length();
                    str4 = listFiles[i3].getParentFile().getName();
                    str6 = simpleDateFormat.format(new Date(listFiles[i3].lastModified())) + " " + simpleDateFormat2.format(new Date(listFiles[i3].lastModified()));
                }
            }
            this.rvCreation.setHasFixedSize(true);
            this.rvCreation.setLayoutManager(new LinearLayoutManager(getActivity()));
            CreationParentAdapter creationParentAdapter = new CreationParentAdapter(getActivity(), this.creationParentModels);
            this.adapter = creationParentAdapter;
            this.rvCreation.setAdapter(creationParentAdapter);
            this.noData.setVisibility(8);
            this.llCreation.setGravity(48);
            this.noData.setGravity(48);
            str = str6;
            i = i2;
            j = j2;
            str3 = str4;
            str2 = str5;
        }
        if (i > 0) {
            this.creationParentModels.add(new CreationParentModel(str3, str2, str, i, j));
        }
    }

    public void onResume() {
        super.onResume();
        this.creationParentModels.clear();
        bindMyCreation(new File(this.path));
    }
}
