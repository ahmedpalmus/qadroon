package com.example.qadroon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class CompanyReg extends AppCompatActivity {

    EditText userN, pass, pass2, perName, phone, email,eaddress;
    Spinner comp_type;
    ImageView image;
    TextView l1, l2, l3, l4, l5, l6,l7;

    String user, password, name, thePhone, Email,address,c_type,  Image = "none";
    String URL = Server.ip + "register_comp.php";
    Button reg, cancel, img_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_reg);
        userN = findViewById(R.id.reg_username);
        pass = findViewById(R.id.reg_pass);
        pass2 = findViewById(R.id.reg_pass2);
        perName = findViewById(R.id.reg_name);
        phone = findViewById(R.id.reg_phone);
        email = findViewById(R.id.reg_email);
        eaddress = findViewById(R.id.reg_address);
        comp_type = findViewById(R.id.comp_type);
        image = findViewById(R.id.fm_image);
        img_btn = findViewById(R.id.fm_image_btn);

        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);
        l5 = findViewById(R.id.l5);
        l6 = findViewById(R.id.l6);
        l7 = findViewById(R.id.l7);

        reg = findViewById(R.id.reg_reg);
        cancel = findViewById(R.id.reg_cancel);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegOnclick(view);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser(400);
            }

        });

    }

    private void showFileChooser(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedimg = data.getData();
            try {
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap univLogo = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                univLogo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void RegOnclick(View view) {
        if (view.getId() == R.id.reg_reg) {

            l1.setText(getResources().getString(R.string.username));
            l1.setTextColor(Color.BLACK);
            l2.setText(getResources().getString(R.string.password));
            l2.setTextColor(Color.BLACK);
            l3.setText(getResources().getString(R.string.conform_pasword));
            l3.setTextColor(Color.BLACK);
            l4.setText(getResources().getString(R.string.pername));
            l4.setTextColor(Color.BLACK);
            l5.setText(getResources().getString(R.string.email));
            l5.setTextColor(Color.BLACK);
            l6.setText(getResources().getString(R.string.phone));
            l6.setTextColor(Color.BLACK);
            l7.setText(getResources().getString(R.string.address));
            l7.setTextColor(Color.BLACK);

            user = userN.getText().toString().trim();
            password = pass.getText().toString().trim();
            String password2 = pass2.getText().toString().trim();
            name = perName.getText().toString().trim();
            thePhone = phone.getText().toString().trim();
            Email = email.getText().toString().trim();
            address = eaddress.getText().toString().trim();
            c_type = comp_type.getSelectedItem().toString();

            boolean error = false;
            if (user.length() < 3 || user.contains(" ")) {
                error = true;
                String temp = l1.getText().toString();
                l1.setText(temp + " *\n"+getResources().getString(R.string.enterUsername));
                l1.setTextColor(Color.RED);
            }
            if (password.length() < 8) {
                error = true;
                String temp = l2.getText().toString();
                l2.setText(temp + " *\n"+ getResources().getString(R.string.enterPass));
                l2.setTextColor(Color.RED);
            }if (!isValidEmail(Email)) {
                error = true;
                String temp = l5.getText().toString();
                l5.setText(temp + " *\n"+getResources().getString(R.string.enterEmail));
                l5.setTextColor(Color.RED);
            }
            if (!isValidPassword(password)) {
                error = true;
                String temp = l2.getText().toString();
                l2.setText(temp + " *\n"+"The password must contain capital and small letters,special chars and numbers");
                l2.setTextColor(Color.RED);
            }
            if (!password.equals(password2)) {
                error = true;
                String temp = l2.getText().toString();
                l2.setText(temp + " *");
                l2.setTextColor(Color.RED);
                temp = l3.getText().toString();
                l3.setText(temp + " *\n"+getResources().getString(R.string.enterPass2));
                l3.setTextColor(Color.RED);
            }
            if (name.length() < 2 || name.length() > 30 || hasNumbers(name)) {
                error = true;
                String temp = l4.getText().toString();
                l4.setText(temp + " *\n"+getResources().getString(R.string.enterPersonal));
                l4.setTextColor(Color.RED);
            }

            if (thePhone.length() != 10 || !thePhone.startsWith("0")) {
                error = true;
                String temp = l6.getText().toString();
                l6.setText(temp + " *\n"+getResources().getString(R.string.enterPhone));
                l6.setTextColor(Color.RED);
            }
            if (address.length() < 3) {
                error = true;
                String temp = l7.getText().toString();
                l7.setText(temp + " *\n"+getResources().getString(R.string.enterAddress));
                l7.setTextColor(Color.RED);
            } if (Image.length() < 3) {
                error = true;
                String temp = img_btn.getText().toString();
                img_btn.setText(temp + " ***");
                img_btn.setTextColor(Color.RED);
            }
            if (!error) {
                Registration();
            }

        } else if (view.getId() == R.id.reg_cancel) {
            finish();
        }

    }

    private void Registration() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(CompanyReg.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", user);
                data.put("password", password);
                data.put("name", name);
                data.put("phone", thePhone);
                data.put("email", Email);
                data.put("address", address);
                data.put("comp_type", c_type);
                data.put("image", Image);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noResult), Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("Exist")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enterREgistration), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }

    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    public boolean hasNumbers(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (Character.isDigit(name.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}

