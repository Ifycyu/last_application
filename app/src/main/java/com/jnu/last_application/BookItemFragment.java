package com.jnu.last_application;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.last_application.data.BookItem;
import com.jnu.last_application.data.DataSaver;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookItemFragment extends Fragment {


    public static final int menu_id_add = 1;
    public static final int menu_id_delete = 2;
    public static final int menu_id_edit = 3;
    private BookItemFragment.MainRecycleViewAdapter mainRecycleViewAdapter;
    private ArrayList<BookItem> bookItems;


    // 返回activity
    private ActivityResultLauncher<Intent> addDataLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            ,result -> {
                if(null!=result){
                    Intent intent =result.getData();
                    if(result.getResultCode()==EditBookActivity.RESULT_CODE_SUCCESS)
                    {
                        Bundle bundle = intent.getExtras();
                        String title = bundle.getString("title");
                        int Order = bundle.getInt("Order");
                        bookItems.add(Order,new BookItem(title, R.drawable.book_no_name));
                        new DataSaver().save(this.getContext(),bookItems);
                        mainRecycleViewAdapter.notifyItemInserted(Order);//把新书放在最后

                    }
                }
            });
    //
    private ActivityResultLauncher<Intent> editDataLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            ,result -> {
                if(null!=result){
                    Intent intent =result.getData();
                    if(result.getResultCode()==EditBookActivity.RESULT_CODE_SUCCESS)
                    {
                        Bundle bundle = intent.getExtras();
                        String title = bundle.getString("title");
                        int Order = bundle.getInt("Order");
                        bookItems.get(Order).setTitle(title);
                        new DataSaver().save(this.getContext(),bookItems);
                        mainRecycleViewAdapter.notifyDataSetChanged();
                    }
                }
            });


    public BookItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment BookItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookItemFragment newInstance() {
        BookItemFragment fragment = new BookItemFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_item, container, false);
        RecyclerView recyclerViewMain=rootView.findViewById(R.id.recycle_view_books);
        //布局
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMain.setLayoutManager(linearLayoutManager);

        DataSaver dataSaver = new DataSaver();
        bookItems = dataSaver.Load(this.getContext());

        bookItems.add(new BookItem("信息安全数学基础（第2版）", R.drawable.book_1));
//        bookItems.add(new BookItem("软件项目管理案例教程（第4版）", R.drawable.book_2));
//        bookItems.add(new BookItem("书", R.drawable.book_no_name));


        //设置数据接收渲染器
        mainRecycleViewAdapter=new MainRecycleViewAdapter(bookItems);
        recyclerViewMain.setAdapter(mainRecycleViewAdapter);

        return rootView;
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //菜单menu的选项执行事件
        switch (item.getItemId())
        {

            case menu_id_add:
                Intent intent;
                intent = new Intent(this.getContext(), EditBookActivity.class);
                intent.putExtra("Order",item.getOrder());
                addDataLauncher.launch(intent);
                Toast.makeText(this.getContext(),"item add "+ item.getOrder()+" clicked",Toast.LENGTH_LONG).show();
                break;
            case menu_id_delete:
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext())
                        .setTitle(R.string.confirmation)
                        .setMessage(R.string.sure_to_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bookItems.remove(item.getOrder());
                                new DataSaver().save(BookItemFragment.this.getContext(),bookItems);
                                mainRecycleViewAdapter.notifyItemRemoved(item.getOrder());
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                alertDialog.show();
                break;
            case menu_id_edit:
                int RESULT_CODE_SUCCESS = 666;
//                Toast.makeText(this,"item edit "+ item.getOrder()+" clicked",Toast.LENGTH_LONG).show();
//                Toast.makeText(this,"name "+ bookItems.get(item.getOrder()).getTitle() ,Toast.LENGTH_LONG).show();
                Intent intentEdit = new Intent(BookItemFragment.this.getContext(), EditBookActivity.class);
                String book_title_edit_text_string = bookItems.get(item.getOrder()).getTitle();
                intentEdit.putExtra("title",book_title_edit_text_string);
                intentEdit.putExtra("Order",item.getOrder());
                editDataLauncher.launch(intentEdit);
                break;

        }
        return super.onContextItemSelected(item);
    }

    //adapter重写三个方法
    // 在内部类设置viewholder类
    public class MainRecycleViewAdapter extends RecyclerView.Adapter<MainRecycleViewAdapter.ViewHolder> {
        //private String[]localDataset;
        private ArrayList<BookItem>localDataset;
        //创建viewholder，针对每一个item生成一个viewholder
        //相当一个容器，里面的东西自定义
        public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textView;
            private final ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                //找到view
                imageView=view.findViewById(R.id.imageView_item_image);
                textView = view.findViewById(R.id.textView_item_caption);

                //holder的监听事件
                view.setOnCreateContextMenuListener(this);
            }
            public TextView getTextView() {
                return textView;
            }

            public ImageView getImageView() {
                return imageView;
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                //监听事件的菜单选项样式，选项，item，显示信息
                contextMenu.add(0,menu_id_add,getAdapterPosition(),"add"+getAdapterPosition());
                contextMenu.add(0, menu_id_delete,getAdapterPosition(),"delete"+getAdapterPosition());
                contextMenu.add(0, menu_id_edit,getAdapterPosition(),"edit"+getAdapterPosition());
            }
        }
        public MainRecycleViewAdapter(ArrayList<BookItem> dataset){
            localDataset=dataset;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            //提取出view 用在viewholder
            View view= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_main,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            //holder设置数据
            holder.getTextView().setText(localDataset.get(position).getTitle());
            holder.getImageView().setImageResource(localDataset.get(position).getCoverResourceId());
        }

        @Override
        public int getItemCount() {
            return localDataset.size();
        }
    }
}