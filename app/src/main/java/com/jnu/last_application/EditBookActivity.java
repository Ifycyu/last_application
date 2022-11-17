package com.jnu.last_application;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditBookActivity extends AppCompatActivity {

    public static final int RESULT_CODE_SUCCESS = 666;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        String title;

        title = this.getIntent().getStringExtra("title");
        int Order = this.getIntent().getIntExtra("Order",0);


        EditText book_title_edit_text = findViewById(R.id.book_title_edit_text);
        Button button = findViewById(R.id.button_ok);

        if(null!=title)
        {
            book_title_edit_text.setText(title);
            Toast.makeText(this,title,Toast.LENGTH_SHORT).show();
        }
        int finalOrder = Order;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                String book_title_edit_text_string = book_title_edit_text.getText().toString();
                if("".equals(book_title_edit_text_string))
                    book_title_edit_text_string = "Unnamed";
                bundle.putString("title",book_title_edit_text_string);
                bundle.putInt("Order", finalOrder);


                intent.putExtras(bundle);
                setResult(RESULT_CODE_SUCCESS,intent);
                EditBookActivity.this.finish();
            }
        });
        Button button2 = findViewById(R.id.button_cancel);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditBookActivity.this.finish();
            }
        });
    }
}