package com.example.possapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText id_Login_edit, pass_Login_edit;
    progress progress;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mAuth = FirebaseAuth.getInstance();

        id_Login_edit = findViewById(R.id.id_Login_edit);
        pass_Login_edit = findViewById(R.id.pass_Login_edit);
        context=getApplicationContext();
    }

    public void doit(View view) {
        switch (view.getId()) {
            case R.id.btn_RegistScreen:
                Intent intent = new Intent(this, signup.class);
                startActivity(intent);
                break;
            case R.id.btn_Login:
                String stremail = id_Login_edit.getText().toString();
                String strpasswd = pass_Login_edit.getText().toString();
                if (stremail.indexOf('@') < 0) {
                    Toast.makeText(signin.this, "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                } else if (strpasswd.length() < 8) {
                    Toast.makeText(signin.this, "비밀번호는 8자리 이상이여야 합니다", Toast.LENGTH_SHORT).show();
                } else {
                    progress = new progress(signin.this);
                    lohinUser(stremail, strpasswd);
                }
                break;
        }
    }

    public void lohinUser(String strEmail, String strPasswd) {
        mAuth.signInWithEmailAndPassword(strEmail, strPasswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(signin.this, mainscreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    progress.stop();
                    Toast.makeText(signin.this, "로그인 실패", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}