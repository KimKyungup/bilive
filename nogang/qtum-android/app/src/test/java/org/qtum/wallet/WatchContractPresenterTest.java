package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.watch_contract_fragment.OnTemplateClickListener;
import org.qtum.wallet.ui.fragment.watch_contract_fragment.WatchContractInteractor;
import org.qtum.wallet.ui.fragment.watch_contract_fragment.WatchContractPresenterImpl;
import org.qtum.wallet.ui.fragment.watch_contract_fragment.WatchContractView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class WatchContractPresenterTest {

    @Mock
    private WatchContractView view;
    @Mock
    private WatchContractInteractor interactor;
    private WatchContractPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new WatchContractPresenterImpl(view, interactor);
    }

    @Test
    public void initialize() {

        presenter.initializeViews();

        verify(view, times(1)).setUpTemplatesList(anyList(), (OnTemplateClickListener) any());
    }

    private static final String TEST_NAME = "Name";
    private static final String TEST_ADDRESS = "Address";
    private static final String TEST_ABIINTERFACE = "Interface";

    @Test
    public void onOkClick_InvalidContractAddressError() {
        when(interactor.isValidContractAddress(anyString()))
                .thenReturn(false);

        presenter.onOkClick(TEST_NAME, TEST_ADDRESS, TEST_ABIINTERFACE);

        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), (BaseFragment.PopUpType) any());
        verify(view, never()).setAlertDialog(anyInt(), anyString(), anyInt(), (BaseFragment.PopUpType) any(), (BaseFragment.AlertDialogCallBack) any());

    }

    private static final List<Contract> TEST_CONTRACTS_WITH_DUPLICATE_TOKEN_ADDRESS = Arrays.asList(new Contract("some address"),
            new Contract(TEST_ADDRESS));

    @Test
    public void onOkClick_DuplicateTokenError() {
        when(interactor.isValidContractAddress(anyString()))
                .thenReturn(true);
        when(interactor.getContracts())
                .thenReturn(TEST_CONTRACTS_WITH_DUPLICATE_TOKEN_ADDRESS);

        presenter.onOkClick(TEST_NAME, TEST_ADDRESS, TEST_ABIINTERFACE);

        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), (BaseFragment.PopUpType) any());
        verify(view, never()).setAlertDialog(anyInt(), anyString(), anyInt(), (BaseFragment.PopUpType) any(), (BaseFragment.AlertDialogCallBack) any());
    }

    private static final List<Contract> TEST_VALID_CONTRACTS = Arrays.asList(new Contract("some address"),
            new Contract("other address"));

    @Test
    public void onOkClick_ContractWithoutToken_Success() {
        when(interactor.isValidContractAddress(anyString()))
                .thenReturn(true);
        when(interactor.getContracts())
                .thenReturn(TEST_VALID_CONTRACTS);

        presenter.onOkClick(TEST_NAME, TEST_ADDRESS, TEST_ABIINTERFACE);

        verify(interactor, times(1)).handleContractWithoutToken(anyString(), anyString(), anyString());
        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyInt(), (BaseFragment.PopUpType) any(), (BaseFragment.AlertDialogCallBack) any());

        verify(view, never()).setAlertDialog(anyInt(), anyInt(), (BaseFragment.PopUpType) any());
    }

    @Test
    public void onTemplateClick() {
        presenter.onTemplateClick(new ContractTemplate());

        verify(interactor, times(1)).readAbiContract(anyString());
        verify(view, times(1)).setABIInterface(anyString(), anyString());
    }

}
