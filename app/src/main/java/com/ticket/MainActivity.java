  package com.ticket;

  import androidx.appcompat.app.AppCompatActivity;

  import android.content.Context;
  import android.content.Intent;
  import android.content.SharedPreferences;
  import android.os.Bundle;
  import android.view.View;
  import android.widget.Button;
  import android.widget.EditText;
  import android.widget.TextView;
import com.ticket.objects.UserLogin;
  import com.ticket.ServerConnection.AsyncTaskClass;

  import java.net.MalformedURLException;

  public class MainActivity extends AppCompatActivity {
      private EditText usernameEditText;
      private EditText passwordEditText;
      private Button loginButton;
      private TextView registerLink;
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("User_Details", Context.MODE_PRIVATE);
          String email = sharedPreferences.getString("email","");
          String pass = sharedPreferences.getString("password","");
          if(!email.isEmpty() && !pass.isEmpty() ){
              try {
//                        System.out.println(user.getJsonString());
                  UserLogin user = new UserLogin(email,pass);
                  AsyncTaskClass obj = new AsyncTaskClass(MainActivity.this,user.getJsonString());
                  obj.send("login");
              } catch (MalformedURLException e) {
                  System.out.println("Errorrrrrrr");
                  e.printStackTrace();
              }
          }
          setContentView(R.layout.activity_main);

          usernameEditText = findViewById(R.id.activity_main_usernameEditText);
          passwordEditText = findViewById(R.id.activity_main_passwordEditText);
          loginButton = findViewById(R.id.activity_main_loginButton);
          registerLink = findViewById(R.id.register_link);
          loginButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if (usernameEditText.getText().length() > 0 && passwordEditText.getText().length() > 0) {
//                      String toastMessage = "Username: " + usernameEditText.getText().toString() + ", Password: " + passwordEditText.getText().toString();
//                      Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                      String email = usernameEditText.getText().toString();
                      String password = passwordEditText.getText().toString();
//
                      try {
//                        System.out.println(user.getJsonString());
                          UserLogin user = new UserLogin(email,password);
                          AsyncTaskClass obj = new AsyncTaskClass(MainActivity.this,user.getJsonString());
                          obj.send("login");
                      } catch (MalformedURLException e) {
                          System.out.println("Errorrrrrrr");
                          e.printStackTrace();
                      }

                  }
              }
          });
          registerLink.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);

                  startActivity(intent);
              }
          });
      }
  }