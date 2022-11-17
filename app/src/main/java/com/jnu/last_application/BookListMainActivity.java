package com.jnu.last_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BookListMainActivity extends AppCompatActivity {

    public class PageViewMyFragmentAdapter extends FragmentStateAdapter{
        public PageViewMyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position)
            {
                case 0:
                    return BookItemFragment.newInstance();
                case 1:
                    return WebViewFragment.newInstance();
                case 2:
                    return MapViewFragment.newInstance();


            }
            return BookItemFragment.newInstance();

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager2 viewPager2Main = findViewById(R.id.viewpager2_main);
        viewPager2Main.setAdapter(new PageViewMyFragmentAdapter(getSupportFragmentManager(),getLifecycle()));

        TabLayout tabLayout = findViewById((R.id.tablayout_head));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2Main, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position)
                {
                    case 0:
                        tab.setText("图书");
                        break;
                    case 1:
                        tab.setText("新闻");
                        break;
                    case 2:
                        tab.setText("卖家");
                        break;

                }

            }
        });
        tabLayoutMediator.attach();


    }


}