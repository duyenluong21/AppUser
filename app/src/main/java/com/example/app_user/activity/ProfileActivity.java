package com.example.app_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_user.R;
import com.example.app_user.model.User;

public class ProfileActivity extends AppCompatActivity {
    TextView txtNameUser, txtNgaySinhUser, txtsdtUser, txtchucVu, txttrinhdo, txtkinhnghiem;
    ImageView backButton;
    private User user;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        sessionManager = new SessionManager(getApplicationContext());
        backButton = findViewById(R.id.backButton);
        txtNameUser = findViewById(R.id.txtNameUser);
        txtNgaySinhUser = findViewById(R.id.txtNgaySinhUser);
        txtsdtUser = findViewById(R.id.txtsdtUser);
        txtchucVu = findViewById(R.id.txtchucVu);
        txttrinhdo = findViewById(R.id.txttrinhdo);
        txtkinhnghiem = findViewById(R.id.txtkinhnghiem);

        Bundle bundleRecevie = getIntent().getExtras();
        if (bundleRecevie != null) {
            user = bundleRecevie.getParcelable("object_user");
        }
        if (user != null) {
            if (user != null) {
                txtNameUser.setText(user.getHoNV() + " " + user.getTenNV());
                txtNgaySinhUser.setText(user.getNgaySinhNV());
                txtsdtUser.setText(user.getSdtNV());
                txtchucVu.setText(user.getChucVu());
                txttrinhdo.setText(user.getTrinhDoHocVan());
                txtkinhnghiem.setText(user.getKinhNghiem());
            }else {
                Log.e("ProfileActivity", "User is null when displaying profile");
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, home_activity.class);
                intent.putExtra("object_user", user);

                // Đặt kết quả là RESULT_OK để chỉ ra rằng hoạt động đã thành công
                setResult(RESULT_OK, intent);

                // Kết thúc hoạt động và trở về hoạt động gọi
                finish();
            }
        });
    }
}
