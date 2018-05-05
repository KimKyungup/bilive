package bilive.forevertext

import android.content.Context
import org.web3j.crypto.Bip39Wallet
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.nio.file.Path

class WalletETH(val context: Context){
    var bip39Wallet : Bip39Wallet? = null
    var credentials : Credentials? = null
    var mnemonic : String? = null
    val password = "bilive"
    var address : String? = null

    public fun createWallet(){
        val path = context.filesDir

        //bip39Wallet = WalletUtils.generateBip39Wallet("bilive",path)
    }

    public fun importWallet(mnemonic:String){
        this.mnemonic = mnemonic
        credentials = WalletUtils.loadBip39Credentials(password,mnemonic)
        address = credentials!!.address
    }
}