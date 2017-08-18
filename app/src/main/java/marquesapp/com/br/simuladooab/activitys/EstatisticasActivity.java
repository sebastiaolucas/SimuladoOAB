package marquesapp.com.br.simuladooab.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import marquesapp.com.br.simuladooab.R;
import marquesapp.com.br.simuladooab.dao.ResolvidasDAO;
import marquesapp.com.br.simuladooab.suporte.Resultado;

public class EstatisticasActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Menu menu;
    private LinearLayout ll;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        getSupportActionBar().setTitle("Estatísticas");

        Resultado res = new Resultado();

        ResolvidasDAO dao = new ResolvidasDAO(this);

        if(!dao.respostasIsEmpty()){
            res = dao.getResultado();
        }

        dao.close();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), res);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ll = (LinearLayout) findViewById(R.id.frente);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        testeLayout();
        return super.onCreateOptionsMenu(menu);
    }

    private boolean menuEstatisticas(){
        MenuItem mi = menu.add("Limpar");
        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                alertaLimpar();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void alertaLimpar(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Limpar?");
        b.setMessage("Deseja limpar as estatísticas?");

        b.setNegativeButton("Cancelar", null);

        b.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ResolvidasDAO dao = new ResolvidasDAO(EstatisticasActivity.this);
                dao.limpar();
                dao.close();
                testeLayout();
            }
        });
        b.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void testeLayout(){
        ResolvidasDAO dao = new ResolvidasDAO(this);
        if(dao.respostasIsEmpty()){
            tabLayout.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            menu.clear();
        }else{
            tabLayout.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);
            menuEstatisticas();
        }
        dao.close();
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, Resultado res) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable("RESULTADO", res);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_estatisticas, container, false);

            Resultado res = (Resultado) getArguments().getSerializable("RESULTADO");
            if(res.getTempo() > 0){
                montar(rootView,res);
            }
            return rootView;
        }

        private void montar(View rootView, Resultado res){

            int id = getArguments().getInt(ARG_SECTION_NUMBER);

            if(id == 1){
                TextView texto_total = (TextView) rootView.findViewById(R.id.numero_total);
                texto_total.setText(res.getTOTAL()+"");

                int por = 100 * res.getAcertos() / res.getTOTAL();

                TextView texto_acertos = (TextView) rootView.findViewById(R.id.numero_acertos);
                texto_acertos.setText(res.getAcertos() + " ("+por+"%)");

                int erros = res.getTOTAL() - res.getAcertos();
                por = 100 - por;

                TextView texto_erros = (TextView) rootView.findViewById(R.id.numero_erros);
                texto_erros.setText(erros + " ("+por+"%)");
            }else if(id == 2){
                Date date = new Date();

                Calendar c = new GregorianCalendar();
                c.setTime(date);
                c.add(Calendar.HOUR, 0-date.getHours());
                c.add(Calendar.MINUTE, 0-date.getMinutes());
                c.add(Calendar.SECOND, 0-date.getSeconds());
                date = c.getTime();

                long tempo = date.getTime();

                date.setTime(tempo + res.getTempo());

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                TextView texto_total = (TextView) rootView.findViewById(R.id.text_total);
                texto_total.setText("Tempo gasto:");

                TextView numero_total = (TextView) rootView.findViewById(R.id.numero_total);
                numero_total.setText(sdf.format(date));

                TextView texto_acertos = (TextView) rootView.findViewById(R.id.text_acertos);
                texto_acertos.setText("Tempo/Questão:");

                date.setTime(tempo + res.getTempo() / res.getTOTAL());

                TextView numero_acertos = (TextView) rootView.findViewById(R.id.numero_acertos);
                numero_acertos.setText(sdf.format(date));

                TextView texto_erros = (TextView) rootView.findViewById(R.id.text_erros);
                texto_erros.setVisibility(View.GONE);

                TextView numero_erros = (TextView) rootView.findViewById(R.id.numero_erros);
                numero_erros.setVisibility(View.GONE);
            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Resultado res;

        public SectionsPagerAdapter(FragmentManager fm, Resultado res) {
            super(fm);
            this.res = res;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1, res);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Questões";
                case 1:
                    return "Tempo";
            }
            return null;
        }
    }
}
