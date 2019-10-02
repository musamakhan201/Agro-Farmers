package example.com.agrofarmers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int signOut = Menu.FIRST;
    private static final int log_out = Menu.FIRST;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    TextView tvName;
    NavigationView navigationView;
    TextView tvEmail;
    CircleImageView cvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        setUpNavigationDrawer();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signout, menu);
        return true;
    }
        private void bind () {
            drawerLayout = findViewById(R.id.drawer_layout);
            toolbar = findViewById(R.id.toolbar);
            navigationView = findViewById(R.id.nav_view);
            tvName = findViewById(R.id.name);
            tvEmail = findViewById(R.id.email);
            cvImage = findViewById(R.id.image_view);
        }

        private void setUpNavigationDrawer () {
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
            drawerLayout.addDrawerListener(toggle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        @Override
        public void onBackPressed () {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.signOut)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (id==R.id.help)
        {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.log_out:
            {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.nav_seeds:
            {
                Toast.makeText(getApplicationContext(),"SEEDS",Toast.LENGTH_SHORT).show();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
