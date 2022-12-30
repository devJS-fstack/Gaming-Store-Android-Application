package com.example.middle_group2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.implement.UserImp;
import com.example.utils.ApiService;
import com.example.utils.Constants;
import com.example.utils.ResponseFB;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import entities.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInOptions ggOptions;
    GoogleSignInClient ggClient;
    Button btnLoginGG, btnLoginFB, btnLogin;
    EditText edtAccountLogin, edtPasswordLogin;
    RelativeLayout rltAccountLogin, rltPassLogin;
    TextView msgLogin;

    private UserImp userImp = new UserImp();
    private int RC_SIGN_IN = 100;
    private CallbackManager callbackManager;

    public static String idUserLogin = "";
    public static String emailUserLogin = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setControl();
        initial();
    }

    private void initial() {
        LoginManager.getInstance().logOut();
        loginGG();
        loginFB();
        handleOnLogin();
    }

    private void loginGG() {
        ggOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        ggClient = GoogleSignIn.getClient(this, ggOptions);
        ggClient.signOut();
        btnLoginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignInGG();
            }
        });
    }

    private void handleSignInGG() {
        Intent signInIntent = ggClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginFB() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ApiService.apiService.fetchDataFromFacebook("last_name,id,first_name,email,picture{url}, name", loginResult.getAccessToken().getToken()).enqueue(new Callback<ResponseFB>() {
                    @Override
                    public void onResponse(Call<ResponseFB> call, Response<ResponseFB> response) {
                        ResponseFB responseFB = response.body();
                        handleResponseFB(responseFB);
                    }

                    @Override
                    public void onFailure(Call<ResponseFB> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                System.out.println("Login Facebook Failed" + e);
            }
        });

        btnLoginFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_photos"));
            }
        });
    }

    private void handleResponseFB(ResponseFB responseFB) {
        String email = responseFB.getEmail();
        String idUser = responseFB.getId();
        Boolean isExist = userImp.isExistUserFB(email, idUser);
        if(!isExist) {
            String firstName = responseFB.getFirst_name();
            String lastName = responseFB.getLast_name();
            String url = responseFB.getPicture().getData().getUrl();
            String username = convertNameToUsername(responseFB.getName());
            String password = generateHashPassword();
            User newUserFb = new User(idUser, username, password, firstName, lastName, url, "", Constants.FACEBOOK_LOGIN, email);
            if(userImp.save(newUserFb)) {
                Toast.makeText(this, "Create new account success", Toast.LENGTH_SHORT).show();
                isExist = true;
                idUserLogin = idUser;
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                isExist = false;
            }
        }

        if(isExist) transformHome();
    }

    private String convertNameToUsername(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        temp = temp.replaceAll("\\s", "");
        return temp.replaceAll("đ", "d").toLowerCase(Locale.ROOT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Login fb
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Login gg
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                handleLoginGoogle(account);
            } catch (Exception e) {
                System.out.println("exception: " + e);
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void transformHome() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void handleLoginGoogle(GoogleSignInAccount account) {
        if (account != null) {
            String firstName = account.getFamilyName();
            String lastName = account.getGivenName();
            String email = account.getEmail();
            String url = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";
            String username = email.split("@")[0];
            Boolean isExist = userImp.isExistUser(email, "email");
            System.out.println("is exist: " + isExist);
            if(isExist) {
                transformHome();
            } else {
                String idUser = generateIdUser();
                String passwordGG = generateHashPassword();
                User newUserGG = new User(idUser, username, passwordGG, firstName, lastName, url, "", Constants.GOOGLE_LOGIN, email);
                if(userImp.save(newUserGG)) {
                    idUserLogin = idUser;
                    Toast.makeText(this, "Create account google success", Toast.LENGTH_SHORT).show();
                    transformHome();
                } else {
                    Toast.makeText(this, "Create account google failed", Toast.LENGTH_SHORT).show();
                }
            }

//            ggClient.signOut();
        }
    }

    private String generateHashPassword() {
        String passwordGG = generateRandomString();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(passwordGG);
    }

    private String generateIdUser() {
        String idUser;
        Boolean isExistID = false;
        do {
            idUser = generateRandomString();
            isExistID = userImp.isExistUser(idUser, "id_user");
        } while(isExistID);

        return idUser;
    }

    private String generateRandomString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        alphabet = alphabet.toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 6;
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    private void handleOnLogin() {
        onFocusAccount();
        onFocusPassword();
        onSubmitLogin();
    }

    private void onFocusAccount() {
        edtAccountLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    setMessage("", View.INVISIBLE);
                    rltAccountLogin.setBackground(LoginActivity.this.getDrawable(R.drawable.active_input));
                } else {
                    rltAccountLogin.setBackground(LoginActivity.this.getDrawable(R.drawable.border_input));
                }
            }
        });

        edtAccountLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setMessage("",View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onFocusPassword() {
        edtPasswordLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    setMessage("", View.INVISIBLE);
                    rltPassLogin.setBackground(LoginActivity.this.getDrawable(R.drawable.active_input));
                } else {
                    rltPassLogin.setBackground(LoginActivity.this.getDrawable(R.drawable.border_input));
                }
            }
        });

        edtPasswordLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setMessage("",View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onSubmitLogin()    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String acc = edtAccountLogin.getText().toString();
                String pass = edtPasswordLogin.getText().toString();
                if(acc.equals("")) {
                    setMessage("Please input your email", View.VISIBLE);
                } else {
                    if(pass.equals("")) {
                        setMessage("Please input your password", View.VISIBLE);
                    } else {
                        User user = userImp.checkUser(acc, pass);
                        if(user == null) {
                            setMessage("Oops! Your email or password is not correct !", View.VISIBLE);
                        } else {
                            setMessage("", View.INVISIBLE);
                            idUserLogin = user.getIdUser();
                            emailUserLogin = user.getEmail();
                            transformHome();
                        }
                    }
                }
            }
        });
    }

    private void setMessage(String message, int visible) {
        msgLogin.setText(message);
        msgLogin.setVisibility(visible);
    }

    private void setControl() {
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGG = findViewById(R.id.btnLoginGG);
        btnLoginFB = findViewById(R.id.btnLoginFB);
        edtAccountLogin = findViewById(R.id.edtAccountLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        rltAccountLogin = findViewById(R.id.rltAccLogin);
        rltPassLogin = findViewById(R.id.rltPassLogin);
        msgLogin = findViewById(R.id.msgLogin);
    }
}