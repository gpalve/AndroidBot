package com.example.assistant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Path;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class MainActivity extends AppCompatActivity {
    ChatView chatView;
    ChatBot chatBot;
    File dbpath;
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatView = (ChatView) findViewById(R.id.chat_view);
        Context ctx = this;// for Activity, or Service. Otherwise simply get the context.
        String dbname = ChatBot.DATABASE_NAME;
        dbpath = ctx.getDatabasePath(dbname);
        ChatBot.DATABASE = dbpath.getAbsolutePath();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private void init() {
        chatView = (ChatView) findViewById(R.id.chat_view);
        try{
            chatBot = new ChatBot();
            chatView.addMessage(new ChatMessage(ChatBot.getBotIntro(), System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
            addListeners();
        }catch (Exception e){
            chatView.addMessage(new ChatMessage(ChatBot.getSystemError()+"\n\n"+e.getMessage(), System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
        }
    }

    private void addListeners(){
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                System.out.println(chatMessage.getMessage());
                MessageProcessor messageProcessor = new MessageProcessor(chatView,chatBot);
                if(!ChatBot.readyToLearnSomethingNew) {
                    messageProcessor.execute(chatMessage.getMessage());
                }else{
                    messageProcessor.execute(ChatBot.question,chatMessage.getMessage());
                }
                return true;
            }
        });

        chatView.setTypingListener(new ChatView.TypingListener(){
            @Override
            public void userStartedTyping(){
                // will be called when the user starts typing

            }

            @Override
            public void userStoppedTyping(){
                // will be called when the user stops typing

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            chatBot.checkExit("exit");
            if(KnowledgeBase.mydatabase != null) {
                KnowledgeBase.mydatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
