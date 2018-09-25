package bd.com.universal.eparking.seeker.OtherClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import bd.com.universal.eparking.seeker.PoJoClasses.Consumer;

/**
 * Created by Mobile App Develop on 4/8/2018.
 */

public class LoginPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String EVENT_ITEMS = "event_items";
    private static final String TEACHER_EMAIL = "teacher_email";
    private static final String TEACHER_PASSWORD = "teacher_password";
    private static final String ISLOGGEDIN = "isLoggedIn";
    private static final String DEFAULT_MESSAGE = "User not found";


    public LoginPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    public String registerAdmin(String email, String pass){
        editor.putString(TEACHER_EMAIL,email);
        editor.putString(TEACHER_PASSWORD,pass);
        editor.commit();
        return "Registered Successfully";
    }

    public String getAdminEmail() {
        return sharedPreferences.getString(TEACHER_EMAIL,DEFAULT_MESSAGE);
    }

    public String getAdminPassword() {
        return sharedPreferences.getString(TEACHER_PASSWORD,DEFAULT_MESSAGE);
    }

    public void setStatus(boolean status){
        editor.putBoolean(ISLOGGEDIN,status);
        editor.commit();
    }
    public boolean getStatus(){
        return sharedPreferences.getBoolean(ISLOGGEDIN,false);
    }


    public String registerConsumer(Consumer event){

        Gson gson = new Gson();
        String json = gson.toJson(event);
        System.out.println(json);
        editor.putString(EVENT_ITEMS,json);
        editor.commit();
        return "Registered Successfully";
    }


}
