package com.ticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ticket.ServerConnection.AsyncTaskClass;
import com.ticket.fragments.DatePickerFragment;
import com.ticket.objects.UserRegister;
import com.ticket.objects.UserRegister;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button signup;
    private EditText email,password,dob;
    private String pswd,confpswd;
    private EditText confirmPassword;
    private EditText firstName, lastName;
    private static final String PASSWORD_PATTERN ="^(?=.*[0-9])(?=.*[A-Z])(?=.*[az])(?=.*[@#$%+=!])(?=\\S+$).{8,}$";
    //Rules for validation of the password
    Pattern psPattern = Pattern.compile(PASSWORD_PATTERN);
    //Compiling the regular expression stored inside string for which regex.Pattern class is implemented
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        firstName = findViewById(R.id.register_first_name);
        lastName = findViewById(R.id.register_last_name);
        signup = findViewById(R.id.register_btn);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        dob = findViewById(R.id.dob);

        //

        dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                return false;
            }

        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = email.getText().toString();//getting text from usname converted tostring
                pswd = password.getText().toString();//getting text from password converted tostring
                confpswd = confirmPassword.getText().toString();
                //checking whether username and password is empty or not using isEmpty()
                if(uname.isEmpty()||pswd.isEmpty() || uname.isEmpty()){


                    Toast.makeText(RegisterActivity.this,"Please enter All the details",Toast.LENGTH_SHORT).show();//Raising toast message
                }
                else if(pswd.equals(confpswd) == false){
                    Toast.makeText(RegisterActivity.this,"Passwords Do not match. Please try again.",Toast.LENGTH_SHORT).show();
                }
                else if(isvalid()){
                    String mail = email.getText().toString();
                    String first = firstName.getText().toString();
                    String last = lastName.getText().toString();
                    String pass = password.getText().toString();
                    String date = dob.getText().toString();
//                    User user = new User("brooo@gmail.com", "Hrithikesh","V M", "pass123","2000-07-30");
                    UserRegister user= new UserRegister(mail,first,last,pass,date);
                    try {
//                        System.out.println(user.getJsonString());
                        AsyncTaskClass obj = new AsyncTaskClass(RegisterActivity.this,user.getJsonString());
                        obj.send("register");
                    } catch (MalformedURLException e) {
                        System.out.println("Errorrrrrrr");
                        e.printStackTrace();
                    }


                }
            }
        });
    } //To check the validity of the function we use isvalid()


    private boolean isvalid(){
        //matcher() is used to create matcher for compiled regular expression which is pswd andmatches() check whether the password matches or not with psPatten
        if(!psPattern.matcher(pswd).matches()){
            Toast.makeText(RegisterActivity.this, "1) Password should contain Atleast 1  uppercase , 1 lowercase letters\n"+ "2) Password should contain one letter,number and special characters.\n"+"3)Minimum length of the password is 8.",Toast.LENGTH_LONG ).show();
            return false;
        }
        else
            return true;
    }


}






