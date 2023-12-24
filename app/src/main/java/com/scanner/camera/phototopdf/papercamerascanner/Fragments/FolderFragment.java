package com.scanner.camera.phototopdf.papercamerascanner.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.camera.phototopdf.papercamerascanner.Activity.Camera_Scanner_GallaryActivity;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.ImageAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Model.ImageModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FolderFragment extends Fragment implements OnItemClickListener {
    String filePath;
    ImageAdapter imageAdapter;
    List<ImageModel> imageModelList = new ArrayList();
    RecyclerView rvImageList;
    ArrayList<String> videoList = new ArrayList<>();

    public FolderFragment() {
    }

    public FolderFragment(String str) {
        this.filePath = str;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_folder, viewGroup, false);
        this.rvImageList = (RecyclerView) inflate.findViewById(R.id.rvImageList);
        this.imageModelList.clear();
        findVideos(new File(this.filePath), this.videoList);
        for (int i = 0; i < Camera_Scanner_GallaryActivity.mainImageModelList.size(); i++) {
            for (int i2 = 0; i2 < this.imageModelList.size(); i2++) {
                if (Camera_Scanner_GallaryActivity.mainImageModelList.get(i).equals(this.imageModelList.get(i2).getFilePath())) {
                    this.imageModelList.get(i2).setSelected(true);
                }
            }
        }
        this.imageAdapter = new ImageAdapter(getActivity(), this.imageModelList, this);
        this.rvImageList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        this.rvImageList.setItemAnimator(new DefaultItemAnimator());
        this.rvImageList.setAdapter(this.imageAdapter);
        return inflate;
    }


    public void findVideos(File file, ArrayList<String> arrayList) {
        for (File file2 : Objects.requireNonNull(file.listFiles())) {
            if (file2.isDirectory()) {
                if (!file2.getName().startsWith(".")) {
                    findVideos(file2, arrayList);
                }
            } else if (file2.getAbsolutePath().toLowerCase().contains(".png") || file2.getAbsolutePath().toLowerCase().contains(".jpg") || file2.getAbsolutePath().toLowerCase().contains(".jpeg")) {
                arrayList.add(file2.getAbsolutePath());
                this.imageModelList.add(new ImageModel(file2.getAbsolutePath(), false));
            }
        }
    }

    public void OnClick(View view, int i, String str) {
        if (!this.imageModelList.get(i).isSelected()) {
            this.imageModelList.get(i).setSelected(true);
            selectedOrNot(this.imageModelList.get(i).getFilePath(), true);
        } else {
            this.imageModelList.get(i).setSelected(false);
            selectedOrNot(this.imageModelList.get(i).getFilePath(), false);
        }
        this.imageAdapter.notifyDataSetChanged();
    }

    public static void selectedOrNot(String str, boolean z) {
        if (z) {
            Camera_Scanner_GallaryActivity.mainImageModelList.add(str);
        } else {
            for (int i = 0; i < Camera_Scanner_GallaryActivity.mainImageModelList.size(); i++) {
                if (Camera_Scanner_GallaryActivity.mainImageModelList.get(i).equals(str)) {
                    Camera_Scanner_GallaryActivity.mainImageModelList.remove(i);
                }
            }
        }
        Camera_Scanner_GallaryActivity.selectedImageAdapter.notifyDataSetChanged();
        TextView textView = Camera_Scanner_GallaryActivity.tvCount;
        textView.setText(Camera_Scanner_GallaryActivity.mainImageModelList.size() + "");
    }
}
