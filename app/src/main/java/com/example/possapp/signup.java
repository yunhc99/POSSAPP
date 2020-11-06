package com.example.possapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    EditText id_Regist_edit, pass_Regist_edit, pass2_Regist_edit, username_Regist_name;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    boolean ok=false;
    progress progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        id_Regist_edit = findViewById(R.id.id_Regist_edit);
        pass_Regist_edit = findViewById(R.id.pass_Regist_edit);
        pass2_Regist_edit = findViewById(R.id.pass2_Regist_edit);
        username_Regist_name = findViewById(R.id.username_Regist_name);
        pass2_Regist_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String a = pass_Regist_edit.getText().toString();
                String b = pass2_Regist_edit.getText().toString();
                if (!a.equals(b)) {
                    pass2_Regist_edit.setBackgroundColor(0xFFFF0000);
                    ok=false;
                }
                else{
                    ok=true;
                    pass2_Regist_edit.setBackgroundColor(0xFFFFFFFF);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mAuth = FirebaseAuth.getInstance();

    }

    public void doit(View view) {
        switch (view.getId()) {
            case R.id.btn_Register:
                String stremail = id_Regist_edit.getText().toString();
                String strpasswd = pass2_Regist_edit.getText().toString();
                String name=username_Regist_name.getText().toString();

                if (stremail.indexOf('@') < 0) {
                    Toast.makeText(signup.this, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                } else if (strpasswd.length() < 8) {
                    Toast.makeText(signup.this, "비밀번호는 8자리 이상이여야 합니다", Toast.LENGTH_SHORT).show();
                }
                else if(!ok){
                    Toast.makeText(signup.this, "비밀번호가 같지않습니다", Toast.LENGTH_SHORT).show();
                }
                else if(name.equals("")){
                    Toast.makeText(signup.this, "이름이 없습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    progress=new progress(signup.this);
                    registUser(stremail, strpasswd,name);
                }
                break;
            case R.id.btn_LoginScreen:
                finish();
                break;
        }
    }
    public void registUser(String strEmail, String strPasswd,final String name) {
        mAuth.createUserWithEmailAndPassword(strEmail, strPasswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String setId=user.getUid();
                    Toast.makeText(signup.this, "등록 성공", Toast.LENGTH_SHORT).show();
                    database=FirebaseDatabase.getInstance();
                    myRef=database.getReference("name").child(setId).push();
                    myRef.setValue(name);
                    finish();
                } else {
                    progress.stop();
                    Toast.makeText(signup.this, "등록 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}