package marquesapp.com.br.simuladooab.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import java.io.Serializable;

import marquesapp.com.br.simuladooab.Fragments.FrameFragment;
import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.dao.LembreteDAO;
import marquesapp.com.br.simuladooab.suporte.Lembrete;

public class InicioActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Serializable {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Menu menu;
    private TimePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_layout);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setTitle("Simulados");
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new FrameFragment());
        tx.commit();
    }

    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_simu) {
            getSupportActionBar().setTitle("Simulados");
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.content_frame, new FrameFragment());
            tx.commit();
        } else if (id == R.id.nav_esta) {
            startActivity(new Intent(this, EstatisticasActivity.class));
        } else if (id == R.id.nav_lembre) {
            LembreteDAO dao = new LembreteDAO(this);
            alertaLembrete(dao);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alertaLembrete(final LembreteDAO dao) {
        final View view = getTimePicker(dao.getLembrete());
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setView(view);
        b.setTitle("Horário do lembrete:");
        b.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CheckBox box = (CheckBox) view.findViewById(R.id.check_lembre);
                if (!box.isChecked()) {
                    dao.setLembrete(new Lembrete());
                } else {
                    TimePicker time = (TimePicker) view.findViewById(R.id.picker_lembre);
                    Lembrete l = new Lembrete();
                    dao.setLembrete(new Lembrete(true, time.getCurrentHour(), time.getCurrentMinute()));
                }
                Toast.makeText(InicioActivity.this,"Salvo",Toast.LENGTH_SHORT).show();
            }
        });
        b.setNegativeButton("Cancelar", null);
        b.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dao.close();
            }
        });
        b.show();
    }

    private View getTimePicker(Lembrete l) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_dialog, null);

        picker = (TimePicker) view.findViewById(R.id.picker_lembre);
        picker.setIs24HourView(true);
        picker.setCurrentHour(l.getHora());
        picker.setCurrentMinute(l.getMinuto());

        CheckBox box = (CheckBox) view.findViewById(R.id.check_lembre);

        if (l.isLembrete()) {
            picker.setVisibility(View.VISIBLE);
        } else {
            picker.setVisibility(View.GONE);
            box.setChecked(false);
        }

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    picker.setVisibility(View.VISIBLE);
                } else {
                    picker.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

}