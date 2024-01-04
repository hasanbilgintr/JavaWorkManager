package com.hasanbilgin.javaworkmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //veri gönderimi
        Data data = new Data.Builder().putInt("intKey", 1).build();

        //setRequiredNetworkType bağlantı durumu şartı
        //  .setRequiredNetworkType(NetworkType.CONNECTED) nete bağlı olsun
        //  .setRequiredNetworkType(NetworkType.NOT_ROAMING) yurt dışında bağlantı kullanmıyo olsun
        //setRequiredNetworkType(NetworkType.METERED) paralı bi şekilden net kullanıyo olsun vs..
        //setRequiresBatteryNotLow pil az olmasın
        //.setRequiresCharging() o sırada şarza bağlı olsun //false pasife edilir

        Constraints constraints = new Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();

        //WorkRequest //genel için kullanılır
        //OneTimeWorkRequest birkere çalıştırılır
        //PeriodicWorkRequest günlük haftalık vs
        //.setInitialDelay() zamanı öteleme
        //.setInputData(data) datayı aldık

        /*

        WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setConstraints(constraints)
                .setInputData(data)
                //.setInitialDelay(5, TimeUnit.MINUTES)
                //.addTag("myTag")
                .build();

        //çalıştırmak için
        //.enqueue(workRequest) sıraya alıp çalıştırma
        WorkManager.getInstance(this).enqueue(workRequest);

        */
        /***********************************************/
        //periyodik workrequest
        //en az 15dakika //illa 15dk içinde çalışmak olmayabilir ha 1 dk geri ha ileri
        WorkRequest periyodicWorkRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build();
        WorkManager.getInstance(this).enqueue(periyodicWorkRequest);

        //değişikler sonucunda bildirim istersek
        //getWorkInfoByIdLiveData hem id hemde gözlemyebiliriz hemde değişikleride inceleyebilicez
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periyodicWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                //workInfo.getState() şuanki işin durumus verir
                //WorkInfo.State.FAILED başarısız olması /SUCCEEDED başarlı oldu /RUNNING şuan çalışıyor / BLOCKED bloklandı / CANCELLED iptal edildi / ENQUEUED sırada
                //
                if (workInfo.getState() == WorkInfo.State.RUNNING) {
                    System.out.println("running");
                } else if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    System.out.println("succeded");
                } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                    System.out.println("failed");
                }
            }
        });


        /*********************/
        //cancelAllWork() tüm workleri iptal eder
        //cancelAllWorkByTag() tag göre iptal et
        //cancelWorkById() id göre iptal et
        //WorkManager.getInstance(this).cancelAllWork()

        //Chaining (zincir , bağlama)
        //bikere çalıştırılanları bağlayabiliriz periyodikler olmaz
        //OneTimeWorkRequest başında bu olmak zorunda!!!
        /*
        OneTimeWorkRequest oneTimeWorkRequest=new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        //beginWith() listede olabilir tekte yazılabilir (sonra whenle eklenebiliyo)
        //oneTimeWorkRequest requesti farklı olduğunu düzşüenbiliriz
        //birbirine bağlayıp çalıştırabiliriz anlamında
        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .enqueue();
        */
        /**********************/





    }
}