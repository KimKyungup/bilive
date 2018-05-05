package bilive.forevertext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import kotlin.concurrent.thread
import kotlin.experimental.and
import android.content.Intent
import android.net.Uri

class MainActivity : AppCompatActivity(), AnkoLogger {

    private val walletETH: WalletETH by LazyKodein(appKodein).instance()

    fun utf8ToHex(utf8: String): String {
        var s = utf8
        try {
            while (s.toByteArray().size < 5) {
                s += " "
            }

            val bytes = s.toByteArray(charset("utf-8"))
            val stringBuilder = StringBuilder()

            for (b in bytes) {
                stringBuilder.append(String.format("%02x", b ))
            }
            return stringBuilder.toString()
        } catch (e: java.io.UnsupportedEncodingException) {
            return ""
        }

    }

    fun hexToUtf8(hex: String): String {
        var bytes: ByteArray
        bytes = BigInteger(hex, 16).toByteArray()
        return String(bytes, Charset.forName("UTF-8"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val walletETH = WalletETH(context = applicationContext)

        createWallet.setOnClickListener {
            walletETH.createWallet()

            //mnemonic.text = walletETH.mnemonic
        }

        importWallet.setOnClickListener {
            walletETH.importWallet(mnemonic.text.toString())

            address.text = walletETH.address
            //mnemonic.text = walletETH.mnemonic
        }

        address.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            try {
                val clip = ClipData.newPlainText("Copied Text", address.text)
                clipboard.primaryClip = clip
                Snackbar.make(it!!, "Copied to clipboard.", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }catch(e:Exception){
                e.printStackTrace()
            }
        }

        scribble.setOnClickListener {
            val toAddr = "0xAf44747484436cc65327794cD1B12f085bea618a"
//            async(UI){
//                async(CommonPool) {
            thread {
                val credentials = walletETH.credentials
                val textHEX =  utf8ToHex(text.text.toString())
                val textAscii = hexToUtf8(textHEX)

                info("TEXT = ${text.text}, HEX = ${textHEX}. TEXT = ${textAscii}")

                val web3 = Web3jFactory.build(HttpService("https://rinkeby.infura.io/gGHwulfhVK8ouWn8aZMz"))

                val gasPrice = web3.ethGasPrice().sendAsync().get().gasPrice
                val gasLimit = BigInteger.valueOf(300000)

                val rawTxManager = RawTransactionManager(web3, credentials)

                val tx = rawTxManager.sendTransaction(
                        gasPrice,
                        gasLimit,
                        toAddr,
                        textHEX,
                        BigInteger.ZERO
                );

                txHash.text = tx.transactionHash
                info("TX Hash = ${tx.transactionHash}")
            }
        }

        txHash.setOnClickListener {

            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://rinkeby.etherscan.io/tx/${txHash.text}"))
                startActivity(browserIntent)

                /*val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", txHash.text)
                clipboard.primaryClip = clip
                Snackbar.make(it!!, "Copied to clipboard.", Snackbar.LENGTH_LONG).setAction("Action", null).show()*/
            }catch(e:Exception){
                e.printStackTrace()
            }
        }
    }
}
