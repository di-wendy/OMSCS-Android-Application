package com.parse.omscs_starter.Fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.omscs_starter.R;


public class Fragment_log_in extends Fragment {

    private EditText username;
    private EditText password;
    private TextView current_user;
    private TextView you_are;
    private Button log_out;

    public Fragment_log_in() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_log_in, container, false);
        TextView signUp = (TextView) view.findViewById(R.id.sign_up);
        Button signIn = (Button) view.findViewById(R.id.Sign_in);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        current_user = (TextView) view.findViewById(R.id.current_user);
        you_are = (TextView) view.findViewById(R.id.you_are);
        log_out = (Button) view.findViewById(R.id.log_out);

        signUp.setPaintFlags(signUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {

            current_user.setText(currentUser.getUsername().toString());

            current_user.setVisibility(view.VISIBLE);
            you_are.setVisibility(view.VISIBLE);
            log_out.setVisibility(view.VISIBLE);

            username.setVisibility(view.GONE);
            password.setVisibility(view.GONE);
            signIn.setVisibility(view.GONE);
            signIn.setVisibility(view.GONE);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().length() == 0 && password.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please set your username and password", Toast.LENGTH_SHORT).show();
                } else {
                    if (username.getText().toString().length() < 5) {
                        username.setError("Username must be longer than 5");
                    } else if (password.getText().toString().length() < 5 || password.getText().toString().length() > 10) {
                        username.setError("Password length should be 5-10");
                    } else {
                        ParseUser user = new ParseUser();
                        user.setUsername(username.getText().toString());
                        user.setPassword(password.getText().toString());

                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Show a simple Toast message upon successful registration
                                    Toast.makeText(getActivity(),
                                            "Successfully Signed up, please log in.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(),
                                            e.getMessage(), Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                    }
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username_txt = username.getText().toString();
                String password_txt = password.getText().toString();

                ParseUser.logInInBackground(username_txt, password_txt,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    Toast.makeText(getActivity(), "Successfully Logged in",Toast.LENGTH_LONG).show();
                                    MainPageFragment fragment = new MainPageFragment();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                                            getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                                    fragmentTransaction.commit();

                                } else {
                                    Toast.makeText(getActivity(),
                                            "Incorrect Username or Password, please try again or signup",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().logOut();
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_LONG).show();

                MainPageFragment fragment = new MainPageFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

            }
        });
        return view;
    }
}
