package com.applozic.mobicomkit.uiwidgets.conversation.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.applozic.mobicomkit.api.account.user.AlUserSearchTask;
import com.applozic.mobicomkit.broadcast.ConnectivityReceiver;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.contact.database.ContactDatabase;
import com.applozic.mobicomkit.uiwidgets.AlCustomizationSettings;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.people.contact.ContactSelectionFragment;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.file.FileUtils;
import com.applozic.mobicommons.json.GsonUtils;
import com.applozic.mobicommons.people.SearchListFragment;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.contact.Contact;

import java.util.List;

/**
 * Created by sunil on 6/2/16.
 */
public class ContactSelectionActivity extends AppCompatActivity implements SearchView
        .OnQueryTextListener {
    public static final String CHANNEL = "CHANNEL_NAME";
    public static final String CHANNEL_OBJECT = "CHANNEL";
    public static final String CHECK_BOX = "CHECK_BOX";
    public static final String IMAGE_LINK = "IMAGE_LINK";
    public static final String GROUP_TYPE = "GROUP_TYPE";
    public static boolean isSearching = false;
    protected SearchView searchView;
    Channel channel;
    boolean disableCheckBox;
    int groupType;
    ContactDatabase contactDatabase;
    ContactSelectionFragment contactSelectionFragment;
    AlCustomizationSettings alCustomizationSettings;
    private String name;
    private String imageUrl;
    private ActionBar mActionBar;
    private SearchListFragment searchListFragment;
    private boolean isSearchResultView = false;
    private String mSearchTerm;
    private AppContactService contactService;
    private ConnectivityReceiver connectivityReceiver;

    public static void addFragment(FragmentActivity fragmentActivity, Fragment fragmentToAdd,
                                   String fragmentTag) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = supportFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.layout_child_activity, fragmentToAdd,
                fragmentTag);

        if (supportFragmentManager.getBackStackEntryCount() > 1) {
            supportFragmentManager.popBackStack();
        }
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_select_layout);
        contactDatabase = new ContactDatabase(this);
        contactSelectionFragment = new ContactSelectionFragment();
        setSearchListFragment(contactSelectionFragment);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        contactService = new AppContactService(this);
        mActionBar = getSupportActionBar();
        String jsonString = FileUtils.loadSettingsJsonFile(getApplicationContext());
        if (!TextUtils.isEmpty(jsonString)) {
            alCustomizationSettings = (AlCustomizationSettings) GsonUtils.getObjectFromJson
                    (jsonString, AlCustomizationSettings.class);
        } else {
            alCustomizationSettings = new AlCustomizationSettings();
        }
        if (!TextUtils.isEmpty(alCustomizationSettings.getThemeColorPrimary()) && !TextUtils
                .isEmpty(alCustomizationSettings.getThemeColorPrimaryDark())) {
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor
                    (alCustomizationSettings.getThemeColorPrimary())));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor(alCustomizationSettings
                        .getThemeColorPrimaryDark()));
            }
        }
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null) {
            channel = (Channel) getIntent().getSerializableExtra(CHANNEL_OBJECT);
            disableCheckBox = getIntent().getBooleanExtra(CHECK_BOX, false);
            mActionBar.setTitle(R.string.channel_member_title);
            name = getIntent().getStringExtra(CHANNEL);
            imageUrl = getIntent().getStringExtra(IMAGE_LINK);
            groupType = getIntent().getIntExtra(GROUP_TYPE, Channel.GroupType.PUBLIC.getValue()
                    .intValue());
        } else {
            mActionBar.setTitle(R.string.channel_members_title);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHANNEL_OBJECT, channel);
        bundle.putBoolean(CHECK_BOX, disableCheckBox);
        bundle.putString(CHANNEL, name);
        bundle.putString(IMAGE_LINK, imageUrl);
        bundle.putInt(GROUP_TYPE, groupType);
        contactSelectionFragment.setArguments(bundle);
        addFragment(this, contactSelectionFragment, "ContactSelectionFragment");
        connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager
                .CONNECTIVITY_ACTION));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_create_menu, menu);
        menu.removeItem(R.id.Next);
        if (disableCheckBox) {
            menu.removeItem(R.id.Done);
        }
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        if (Utils.hasICS()) {
            searchItem.collapseActionView();
        }
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            onSearchRequested();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        // Don't allow another search if this activity instance is already showing
        // search results. Only used pre-HC.
        return !isSearchResultView && super.onSearchRequested();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.mSearchTerm = query;
        isSearching = false;

        if (alCustomizationSettings.isContactSearchFromServer()) {
            processSearchCall(query);
        }

        return false;
    }

    public void processSearchCall(String query) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage(getResources().getString(R.string.applozic_contacts_loading_info));
        dialog.show();

        new AlUserSearchTask(this, query, new AlUserSearchTask.AlUserSearchHandler() {
            @Override
            public void onSuccess(List<Contact> contacts, Context context) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (!contacts.isEmpty() && contactSelectionFragment != null) {
                    contactSelectionFragment.restartLoader();
                }
            }

            @Override
            public void onFailure(Exception e, Context context) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Toast.makeText(context, R.string.applozic_server_error, Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    @Override
    public boolean onQueryTextChange(String query) {
        this.mSearchTerm = query;
        if (getSearchListFragment() != null) {
            getSearchListFragment().onQueryTextChange(query);

            isSearching = !query.isEmpty();
        }
        return true;
    }

    public SearchListFragment getSearchListFragment() {
        return searchListFragment;
    }

    public void setSearchListFragment(SearchListFragment searchListFragment) {
        this.searchListFragment = searchListFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (connectivityReceiver != null) {
                unregisterReceiver(connectivityReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
