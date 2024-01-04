package com.hasanbilgin.javaworkmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RefreshDatabase extends Worker {


    //        Android Jetpack ile birlikte gelen güzelliklerden biri de WorkManager. Peki ne işe yarıyor, nedir bu WorkManager? Ufak bir örnekle açıklayalım, geliştirdiğiniz uygulama içerisinde arkaplanda yapılması gereken bir görev var ve bu görevin uygulamadan çıkılsa veya cihaz yeniden başlatılsa bile çalışması gerekiyor. İşte WorkManager buna benzer ertelenebilen, bir defalık veya periyodik olarak çalıştırılabilen, birlikte görev zincirleri oluşturulması gereken problemlerin çözümünü kolaylaştırmak için aramıza katıldı.
    //en azz 26 sdk ister
    Context myContext;



    public RefreshDatabase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.myContext = context;
    }


    @NonNull
    @Override
    //çalıştırılcak yer
    public Result doWork() {
        //veri alımı
        Data data = getInputData();
        int myNumber = data.getInt("intKey", 0);

        refreshDatebase(myNumber);
        //succes başarı //failure hata //retry bidaha
        return Result.success();
    }

    private void refreshDatebase(int mynumber) {
        //        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.hasanbilgin.javaworkmanager",Context.MODE_PRIVATE);
//        buda olur
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("com.hasanbilgin.javaworkmanager", Context.MODE_PRIVATE);
        int mySaveNumber = sharedPreferences.getInt("myNumber", 0);
        mySaveNumber = mySaveNumber + mynumber;
        System.out.println(mySaveNumber);

        sharedPreferences.edit().putInt("myNumber", mySaveNumber).apply();
    }

    //4.videodayız
}
