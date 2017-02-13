package net.noahgao.freeiot.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import net.noahgao.freeiot.R;

import us.feras.mdv.MarkdownView;

public class ReadmeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        }
        MarkdownView markdownView = new MarkdownView(this);
        setContentView(markdownView);
        markdownView.loadMarkdown(getIntent().getStringExtra("readme"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ReadmeActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
