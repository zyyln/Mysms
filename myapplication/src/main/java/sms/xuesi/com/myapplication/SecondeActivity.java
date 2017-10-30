package sms.xuesi.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sms.xuesi.com.adapter.GalleryAdapter;
import sms.xuesi.com.myview.MyRecyclerView;

public class SecondeActivity extends AppCompatActivity {
    private MyRecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> mDatas;
    private ImageView mImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconde);
        mImg = (ImageView) findViewById(R.id.id_content);
        mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.garden02,
                R.drawable.garden03, R.drawable.garden02, R.drawable.garden04, R.drawable.garden05,
                R.drawable.garden06, R.drawable.garden07));
        mRecyclerView = (MyRecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new GalleryAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemScrollChangeListener(new MyRecyclerView.OnItemScrollChangeListener() {
            @Override
            public void onChange(View view, int position) {
                Toast.makeText(SecondeActivity.this, position + "", Toast.LENGTH_SHORT)
                        .show();
                mImg.setImageResource(mDatas.get(position));
            }
        });
        mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(SecondeActivity.this, position + "", Toast.LENGTH_SHORT)
                        .show();
                mImg.setImageResource(mDatas.get(position));
            }
        });
    }


}
