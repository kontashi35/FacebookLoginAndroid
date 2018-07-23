package co.realmentlabs.downloadme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LoginButton mLoginBtn;
    private TextView mFname;
    private TextView mLname;
    private TextView mEmail;
    private TextView mId;

    private CallbackManager mCallBackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginBtn=findViewById(R.id.login_button);
        mFname=findViewById(R.id.fname);
        mLname=findViewById(R.id.lname);
        mEmail=findViewById(R.id.email);
        mId=findViewById(R.id.id);
        mLoginBtn.setReadPermissions(Arrays.asList("email","public_profile"));

        mCallBackManager=CallbackManager.Factory.create();

        mLoginBtn.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userid=loginResult.getAccessToken().getUserId();
                GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                            displayUserInfo(object);
                    }
                });
                Bundle parameter=new Bundle();
                parameter.putString("fields","first_name,last_name,email,id");
                request.setParameters(parameter);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void displayUserInfo(JSONObject object) {
        String first_name,last_name,email,id;
        try {
            first_name=object.getString("first_name");
            last_name=object.getString("last_name");
            email=object.getString("email");
            id=object.getString("id");
            mFname.setText(first_name);
            mLname.setText(last_name);
            mEmail.setText(email);
            mId.setText(id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
