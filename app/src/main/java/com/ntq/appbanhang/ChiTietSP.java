package com.ntq.appbanhang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChiTietSP extends AppCompatActivity {

    ImageView imvHinhCTSP;
    TextView txtTenSPCT, txtGiaSPCT, txtGiaSaleSPCT;
    ImageView imvStarCTSP1, imvStarCTSP2, imvStarCTSP3, imvStarCTSP4, imvStarCTSP5, imvhHearCTSP;
    ImageButton imvbtnGioHang;
    NotificationBadge notificationBadge;
    TextView txtMota, txtSold;
    ArrayList<SanPham> mangSP;
    SanPhamAdapter sanPhamAdapter;
    SanPham sp;
    RatingBar ratingBar;

    Button btnBackCTSP, btnDecrease, btnIncrease, btnAmoutProduct, btnMua, btnGioHang;

    int count = 0;
    int heartCount = 0;
    int countClickSize = 0;
    int mauClick = 0;

    int ID = 0;
    String tenChiTiet = "";
    Integer giaSPChiTiet = 0;
    Integer giaSPSaleChiTiet = 0;
    String hinhAnhSPChiTiet = "";
    String MoTaSPChiTiet = "";
    String HeartChiTiet = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sp);
        setControl();
        GetDuLieu();
        setSeeMore();
        setBack();
        setAddGioHang();
        setMuaHang();
        ClickGioHang();
        SetDanhGia();
    }

    private void SetDanhGia() {
        RecyclerView recyclerViewx;
        Button fbAdd;
        JSONArray jsonArray = new JSONArray();
        FeedbackAdapter mainAdapter;
        recyclerViewx = findViewById(R.id.recyCTSP);
        fbAdd = findViewById(R.id.fb_add);

        recyclerViewx.setLayoutManager(new GridLayoutManager(this, 3));
        mainAdapter = new FeedbackAdapter(this, jsonArray);
        recyclerViewx.setAdapter(mainAdapter);
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ChiTietSP.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_feedback);
                dialog.show();
                RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                Button btnSubmit = dialog.findViewById(R.id.btn_submit);
                ImageView ratingImage = dialog.findViewById(R.id.ratingImage);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
//                        txtRating.setText(String.format("(%s)",rating));
                        if(rating <= 1) {
                            ratingImage.setBackgroundResource(R.drawable.smile5);
                        }
                        else if(rating <= 2) {
                            ratingImage.setBackgroundResource(R.drawable.smile4);
                        }
                        else if(rating <= 3) {
                            ratingImage.setBackgroundResource(R.drawable.smile3);
                        }
                        else if(rating <= 4) {
                            ratingImage.setBackgroundResource(R.drawable.smile2);
                        }
                        else {
                            ratingImage.setBackgroundResource(R.drawable.smile1);
                        }
                        AnimateImage(ratingImage);
                    }
                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sRating = String.valueOf(ratingBar.getRating());
                        try {
                            jsonArray.put(0, new JSONObject().put("rating", sRating));
                            recyclerViewx.setAdapter(mainAdapter);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setMuaHang() {
        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogGioHangCTSP(Gravity.BOTTOM, R.layout.mua_hang);
            }
        });

    }
    private void AnimateImage(ImageView ratingImage) {
        ScaleAnimation animation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(200);
        ratingImage.setAnimation(animation);
    }




    private void setAddGioHang() {
        imvbtnGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogGioHangCTSP(Gravity.BOTTOM, R.layout.gio_hang_chitiet_sp);
            }
        });
    }

    private void setBack() {
        btnBackCTSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietSP.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSeeMore() {
        ExpandableTextView textView = findViewById(R.id.expand_text_view);
        textView.setText(txtMota.getText());

    }


    private void GetDuLieu() {

        String Sold = "";
        int IdSP = 0;
        sp = (SanPham) getIntent().getSerializableExtra("chitiet");
        ID = sp.getID();
        tenChiTiet = sp.getTenSP();
        giaSPChiTiet = sp.getGiaSP();
        giaSPSaleChiTiet = sp.getGiaSale();
        hinhAnhSPChiTiet = sp.getHinhAnhSP();
        MoTaSPChiTiet = sp.getMoTaSP();
        HeartChiTiet = sp.getHeart();
        Sold = sp.getSold();
        IdSP = sp.getIdSP();
        txtTenSPCT.setText(tenChiTiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtGiaSPCT.setText(decimalFormat.format(giaSPChiTiet) + "??");
        txtGiaSaleSPCT.setText(decimalFormat.format(giaSPSaleChiTiet) + "??");
        txtGiaSaleSPCT.setPaintFlags(txtGiaSaleSPCT.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        txtSold.setText("???? b??n: 4" );
        Glide.with(getApplicationContext()).load(hinhAnhSPChiTiet)
                .placeholder(R.drawable.account)
                .error(R.drawable.cart)
                .into(imvHinhCTSP);
        txtMota.setText(MoTaSPChiTiet);

    }


    private void setControl() {
        imvHinhCTSP = findViewById(R.id.imvChiTietSP);
        txtTenSPCT = findViewById(R.id.tenChiTietSP);
        txtGiaSPCT = findViewById(R.id.txtGiaChiTietSP);
        txtGiaSaleSPCT = findViewById(R.id.txtGiaSaleChiTietSP);
        txtMota = findViewById(R.id.expandable_text);
        btnBackCTSP = findViewById(R.id.btnBackCTSP);
        imvbtnGioHang = findViewById(R.id.btnThemVaoGioHang);

        btnMua = findViewById(R.id.btnMua);
        btnGioHang = findViewById(R.id.btnGioHang);
        notificationBadge = findViewById(R.id.slcart);
        if (Server.listGioHang != null) {
            int total = 0;
            for (int i = 0; i < Server.listGioHang.size(); i++) {
                total = total + Server.listGioHang.get(i).getSoLuong();
            }
            notificationBadge.setText(String.valueOf(total));
        }

    }


    private void DialogGioHangCTSP(int gravity, int view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //X??c ?????nh v??? tr?? c???a dialog
        WindowManager.LayoutParams windowAtribute = window.getAttributes();
        windowAtribute.gravity = gravity;
        window.setAttributes(windowAtribute);

        //================= khi click ra ngo??i diaolog s??? t???t dialog =================
        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }

        // =====================================================
        if (view == R.layout.gio_hang_chitiet_sp) {
            Button btnAddSP = dialog.findViewById(R.id.btnAddSP);
            btnAddSP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Toast.makeText(ChiTietSP.this, "???? th??m v??o gi??? h??ng", Toast.LENGTH_SHORT).show();
                    themGioHang();
                }
            });
        } else if (view == R.layout.mua_hang) {
            Button btnMuaHang = dialog.findViewById(R.id.btnAddMuaHang);
            btnMuaHang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Toast.makeText(ChiTietSP.this, "??i ?????n thanh to??n", Toast.LENGTH_SHORT).show();
                    int soluongmua = Integer.parseInt(btnAmoutProduct.getText().toString());
                    Server.listMuaHang.add(new GioHang(sp.getID(),sp.getTenSP(), sp.getGiaSP(), sp.getHinhAnhSP(), soluongmua));
                    Intent intent = new Intent(ChiTietSP.this, DonHangActivity.class);
                    long money = sp.getGiaSP() * soluongmua;
                    intent.putExtra("tongtien", money);
                    startActivity(intent);
                    finish();

                }
            });
        }

        dialog.show();
        // ================== T??ng gi???m s??? l?????ng mua sp ==================
        btnDecrease = dialog.findViewById(R.id.btnDecrease);
        btnIncrease = dialog.findViewById(R.id.btnIncrease);
        btnAmoutProduct = dialog.findViewById(R.id.numberProduct);
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    count = 0;
                } else if (count >= 1) {
                    count = count - 1;
                }
                btnAmoutProduct.setText(count + "");
            }
        });
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count + 1;
                btnAmoutProduct.setText(count + "");
            }
        });
        //======================= G??n csdl cho s???n ph???m  ===========================
        SanPham sp = (SanPham) getIntent().getSerializableExtra("chitiet");
        Integer giaSP = 0;
        TextView txtGia = dialog.findViewById(R.id.txtGiaCTSPGioHang);
        giaSP = sp.getGiaSP();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtGia.setText("Gi??: " + decimalFormat.format(giaSP) + "??");

        String kho = "";
        TextView txtKho = dialog.findViewById(R.id.kho);
        kho = sp.getKho();
        txtKho.setText("Kho: 10" );

        String hinhAnh = "";
        ImageView imvHinh = dialog.findViewById(R.id.imvHinh);
        hinhAnh = sp.getHinhAnhSP();
        Glide.with(getApplicationContext()).load(hinhAnh)
                .placeholder(R.drawable.account)
                .error(R.drawable.cart)
                .into(imvHinh);
        // ========================= G??n csdl cho m??u  =========================
        String mau1 = "", mau2 = "", mau3 = "", mau4 = "";
        ImageView imvMau1 = dialog.findViewById(R.id.imvmau1);
        ImageView imvMau2 = dialog.findViewById(R.id.imvmau2);
        ImageView imvMau3 = dialog.findViewById(R.id.imvmau3);
        ImageView imvMau4 = dialog.findViewById(R.id.imvmau4);
        mau1 = sp.getMau1();
        mau2 = sp.getMau2();
        mau3 = sp.getMau3();
        mau4 = sp.getMau4();

        Glide.with(getApplicationContext()).load(mau1)
                .placeholder(R.drawable.account)
                .error(R.drawable.cart)
                .into(imvMau1);

        Glide.with(getApplicationContext()).load(mau2)
                .placeholder(R.drawable.account)
                .error(R.drawable.cart)
                .into(imvMau2);

        Glide.with(getApplicationContext()).load(mau3)
                .placeholder(R.drawable.account)
                .error(R.drawable.cart)
                .into(imvMau3);

        Glide.with(getApplicationContext()).load(mau4)
                .placeholder(R.drawable.account)
                .error(R.drawable.cart)
                .into(imvMau4);

        //==================================================================

        Button btnSize = dialog.findViewById(R.id.size);
        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countClickSize++;
                if (countClickSize % 2 == 0) {
                    btnSize.setBackgroundColor(Color.parseColor("#d4d4d4"));
                } else {
                    btnSize.setBackgroundColor(Color.parseColor("#facd92"));
                }
            }
        });


        TextView txtMauChon = dialog.findViewById(R.id.txtMauDcChon);
        setMauClick(imvMau1, txtMauChon);
        setMauClick(imvMau2, txtMauChon);
        setMauClick(imvMau3, txtMauChon);
        setMauClick(imvMau4, txtMauChon);


    }

    private void themGioHang() {
        if (Server.listGioHang.size() > 0) {
            int soluong = Integer.parseInt(btnAmoutProduct.getText().toString());
            boolean flag = false;
            for (int i = 0; i < Server.listGioHang.size(); i++) {
                if (Server.listGioHang.get(i).getIdSP() == sp.getID()) {
                    Server.listGioHang.get(i).setSoLuong(soluong + Server.listGioHang.get(i).getSoLuong());
                    Server.listGioHang.get(i).setGiaSP(sp.getGiaSP());
                    flag = true;
                }
            }
            if (flag == false) {
                int gia = sp.getGiaSP();
                GioHang gioHang = new GioHang();
                gioHang.setGiaSP(gia);
                gioHang.setTenSP(sp.getTenSP());
                gioHang.setIdSP(sp.getID());
                gioHang.setSoLuong(soluong);
                gioHang.setHinhSP(sp.getHinhAnhSP());
                Server.listGioHang.add(gioHang);
            }
        } else {
            int soluong = Integer.parseInt(btnAmoutProduct.getText().toString());
            int gia = sp.getGiaSP();
            GioHang gioHang = new GioHang();
            gioHang.setGiaSP(gia);
            gioHang.setTenSP(sp.getTenSP());
            gioHang.setIdSP(sp.getID());
            gioHang.setSoLuong(soluong);
            gioHang.setHinhSP(sp.getHinhAnhSP());
            Server.listGioHang.add(gioHang);

        }
        int total = 0;
        for (int i = 0; i < Server.listGioHang.size(); i++) {
            total = total + Server.listGioHang.get(i).getSoLuong();
        }
        notificationBadge.setText(String.valueOf(total));

    }

    private void setMauClick(ImageView imv, TextView txt) {
        float xScale = imv.getScaleX();
        float ySacle = imv.getScaleY();
        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauClick++;
                if (mauClick % 2 == 0) {
                    txt.setText("Ch??a c?? m??u ???????c ch???n");
                    imv.setScaleX((xScale));
                    imv.setScaleY((float) (ySacle));
                } else {

                    imv.setScaleX((float) (xScale + 0.5));
                    imv.setScaleY((float) (ySacle + 0.5));
                    txt.setText("");
                }
            }
        });

    }
    // ==============================================================


    @Override
    protected void onResume() {
        super.onResume();
        if (Server.listGioHang != null) {
            int total = 0;
            for (int i = 0; i < Server.listGioHang.size(); i++) {
                total = total + Server.listGioHang.get(i).getSoLuong();
            }
            notificationBadge.setText(String.valueOf(total));
        }
    }

    private void ClickGioHang() {
        btnGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietSP.this, GioHangActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}