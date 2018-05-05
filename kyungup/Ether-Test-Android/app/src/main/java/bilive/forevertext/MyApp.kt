package bilive.forevertext

import android.app.Application
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.appKodein


open class MyApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy  {
        import(createKodein())
    }

    //val appDatabase: AppDatabase by LazyKodein(appKodein).instance()

    open fun createKodein(): Kodein.Module{
        return Kodein.Module{
            bind<WalletETH>() with singleton{WalletETH(applicationContext)}
            //bind<AppDatabase>() with singleton { Room.databaseBuilder(applicationContext, AppDatabase::class.java, "maindb3").fallbackToDestructiveMigration().build() }
            //bind<KeyStore>() with singleton { KeyStore(applicationContext)}
            //bind<MyManager>() with singleton { MyManager(instance(),instance())}
            //bind<SmartContract>() with singleton{SmartContract(instance(),instance())}
            //bind<Transfer>() with singleton{Transfer(instance(),instance())}
        }
    }
}