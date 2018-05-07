package com.scribble.scribble.ui.activity.main_activity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.scribble.scribble.R;
import com.scribble.scribble.dataprovider.Ethereum.EthereumService;
import com.scribble.scribble.model.gson.Transaction;
import com.scribble.scribble.model.gson.TxListResponse;
import com.scribble.scribble.ui.base.base_activity.BaseActivity;
import com.scribble.scribble.ui.base.base_fragment.BaseFragment;

import java.util.List;
import java.util.Observable;

import butterknife.BindView;
import retrofit2.Call;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements MainActivityView {
    private static final int LAYOUT = R.layout.activity_main;
    //private static final int LAYOUT_LIGHT = R.layout.activity_main_light;
    private MainActivityPresenter mMainActivityPresenterImpl;

//    @BindView(R.id.bottom_navigation_view)
//    BottomNavigationView mBottomNavigationView;


    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
//                    Call<List<Transaction>> call = EthereumService.newInstance()
//                            .getCallTransactionList("0x88FD84480E245E0294Cdd71eCeCdfe47B4bEb5d6");
//
//                    try {
//                        result = call.execute();
//                        Log.d("Scribble",result.toString());
//                    }
//                    catch(Exception e){
//
//                    }

                    EthereumService.newInstance()
                            .getTransactionList("0x88FD84480E245E0294Cdd71eCeCdfe47B4bEb5d6")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<TxListResponse>() {
                                           @Override
                                           public void onCompleted() {
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               e.getStackTrace();
                                           }

                                           @Override
                                           public void onNext(TxListResponse result) {

                                               Log.d("Log", result.toString());
//                                    TokenHistoryList.newInstance().getTokenHistories().addAll(historyResponse.getItems());
                                               //getView().addHistory(currentItemCount, getInteractor().getHistoryList().size() - currentItemCount + 1,
                                               //        getInteractor().getHistoryList());
                                           }
                                       });


                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    public MainActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void initializeViews() {

//        initBottomNavViewWithFont((ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK) ? R.string.simplonMonoRegular : R.string.proximaNovaRegular));

//        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//
//                    case R.id.navigation_dashboard:
//                        List<Transaction> result = EthereumService.newInstance()
//                                .getCallTransactionList("0x88FD84480E245E0294Cdd71eCeCdfe47B4bEb5d6");
//
//                        Log.d("Scribble",result.toString());
//                        break;
//
////                    case R.id.item_wallet:
////                        if (mRootFragment != null && mRootFragment.getClass().getSimpleName().contains(WalletMainFragment.class.getSimpleName())) {
////                            popBackStack();
////                            return true;
////                        }
////                        mRootFragment = WalletMainFragment.newInstance(getContext());
////                        break;
////                    case R.id.item_profile:
////                        if (mRootFragment != null && mRootFragment.getClass().getSimpleName().contains(ProfileFragment.class.getSimpleName())) {
////                            popBackStack();
////                            return true;
////                        }
////                        mRootFragment = ProfileFragment.newInstance(getContext());
////                        break;
////                    case R.id.item_news:
////                        if (mRootFragment != null && mRootFragment.getClass().getSimpleName().contains(WriteFragment.class.getSimpleName())) {
////                            popBackStack();
////                            return true;
////                        }
////                        mRootFragment = WriteFragment.newInstance(getContext());
////                        break;
////                    case R.id.item_send:
////                        if (mRootFragment != null && mRootFragment.getClass().getSimpleName().contains(SendFragment.class.getSimpleName())) {
////                            popBackStack();
////                            return true;
////                        }
////
////                        mRootFragment = SendFragment.newInstance(false, null, null, null, getContext());
////
////                        break;
//                    default:
//                        return false;
//                }
//                //openRootFragment(mRootFragment);
//                return true;
//            }
//        });

//        Intent intent = getIntent();
//        switch (intent.getAction()) {
//            case QtumIntent.SEND_FROM_SDK:
//                getPresenter().setSendFromIntent(true);
//                mAddressForSendAction = intent.getStringExtra(QtumIntent.SEND_ADDRESS);
//                mAmountForSendAction = intent.getStringExtra(QtumIntent.SEND_AMOUNT);
//                mTokenAddressForSendAction = intent.getStringExtra(QtumIntent.SEND_TOKEN);
//                break;
//            case NfcAdapter.ACTION_NDEF_DISCOVERED:
//                getPresenter().setSendFromIntent(true);
//                mAddressForSendAction = "QbShaLBf1nAX3kznmGU7vM85HFRYJVG6ut";
//                mAmountForSendAction = "1.431";
//                break;
//            default:
//                break;
//        }
//
//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { //Update wallet balance change listener
//            @Override
//            public void onBackStackChanged() {
//                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
//                    if (fragments != null) {
//                        if (startPageExists(fragments)) {
//                            return;
//                        }
//                        for (Fragment fr : fragments) {
//                            if (fr != null && fr.getClass() != null) {
//                                if (fr instanceof WalletFragment) {
//                                    showBottomNavigationView(false);
//                                } else if (fr instanceof NewsFragment) {
//                                    showBottomNavigationView(false);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });

//        mNetworkStateListener = new NetworkStateListener() {
//            @Override
//            public void onNetworkStateChanged(boolean networkConnectedFlag) {
//                getPresenter().updateNetworkSate(networkConnectedFlag);
//            }
//        };
//        mNetworkReceiver.addNetworkStateListener(mNetworkStateListener);
    }



    @Override
    protected void createPresenter() {
        mMainActivityPresenterImpl = new MainActivityPresenterImpl(this, new MainActivityInteractorImpl(getContext()));
    }

    @Override
    public MainActivityPresenter getPresenter() {
        return mMainActivityPresenterImpl;
    }


    @Override
    public void openRootFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(BaseFragment.BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(BaseFragment.BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void openFragment(Fragment fragment) {
        hideKeyBoard();
        getSupportFragmentManager()
                .beginTransaction()
                //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack(BaseFragment.BACK_STACK_ROOT_TAG, 0);
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
