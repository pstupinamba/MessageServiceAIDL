package com.senai.servicemessenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IMessageService messageService;
    private boolean isBound = false; //flag para indicar se está ou não conectado com o servico


    private Button buttonSend;
    private TextView editTextMessage;

    private Button buttonGetMessages;
    private TextView textViewHistory;


    //Criar conexao com o serviço
    private ServiceConnection serverConnection = new ServiceConnection() {
        //faz a conexao
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            messageService = IMessageService.Stub.asInterface(iBinder);
            isBound = true;
        }

        //termina a conexao
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            messageService = null;
        }
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        buttonSend = findViewById(R.id.buttonSend);
        editTextMessage = findViewById(R.id.editTextMessage);

        Intent intent = new Intent();
        intent.setComponent( new ComponentName("com.senai.servicemessenger","com.senai.servicemessenger.MessageService"));

        bindService(intent, serverConnection, BIND_AUTO_CREATE);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    messageService.sendMessage(editTextMessage.getText().toString());
                }catch (RemoteException e){
                    e.printStackTrace();
                }

            }
        });

        buttonGetMessages = findViewById(R.id.buttonGetMessages);
        textViewHistory = findViewById(R.id.textViewHistory);

        buttonGetMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<String> messages = messageService.getMessages();
                    textViewHistory.setText(TextUtils.join("\n", messages));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( isBound ){
            isBound = false;
            unbindService(serverConnection);
        }
    }
}