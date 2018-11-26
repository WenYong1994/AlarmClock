package com.weny.mystudioapp.alarmclock.activity;

import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.weny.mystudioapp.alarmclock.R;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func9;
import rx.schedulers.Schedulers;

public class RxJavaStudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_studio);
        initListener();
    }

    private void initListener() {
        findViewById(R.id.test_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                test1();
//                test3();
                test01();
            }


        });
    }

    private void test1() {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //这里操作数据库或者是耗时操作
                SystemClock.sleep(2000);
                subscriber.onNext("数据获取完成了");


            }})
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Toast.makeText(RxJavaStudioActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });


//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                String s= "";
//                SystemClock.sleep(2000);
//                s="数据完成了";
//                final String finalS = s;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(RxJavaStudioActivity.this, finalS, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }.start();



    }



    private void test2(){
        String[] arr = new String[]{"12","23","43"};
        Observable.from(arr)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        char[] chars = s.toCharArray();
                        String[] arrs = new String[chars.length];
                        for(int i=0;i<chars.length;i++){
                            arrs[i] = String.valueOf(chars[i]);
                        }
                        return Observable.from(arrs);
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String c) {
                        return c=="2";
                    }
                })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /*.subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Toast.makeText(RxJavaStudioActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });*/
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {


                    }

                    @Override
                    public void onNext(String s) {


                    }
                });
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (String str : new String[]{"12","23","43"}) {
                    for (final char c : str.toCharArray()) {
                        if (c=='2') {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RxJavaStudioActivity.this, c, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        }.start();

    }

    public static void main(String[] args){
        //test01();
//        test3();
    }


    public void test01(){
        ArrayList<String[]> arrayList =new ArrayList<>();
        arrayList.add(new String[]{"1a2","3bb4","5aaassssa6"});
        arrayList.add(new String[]{"7ss8","9aa"});




        Observable.from(arrayList)
                .flatMap(new Func1<String[], Observable<String>>() {
                    @Override
                    public Observable<String> call(String[] strings) {
                        Log.e("sssss","flatMap1111+++++++"+Thread.currentThread().getName());
                        return Observable.from(strings);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String string) {
                        Log.e("sssss","filter11111+++++++"+Thread.currentThread().getName());
                        return string.length()>3;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String string) {
                        Log.e("sssss","filter22222+++++++"+Thread.currentThread().getName());
                        return string;
                    }
                })
//                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String string) {
                        Log.e("sssss","filter33333+++++++"+Thread.currentThread().getName());
                        return string.length()<=5;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String string) {
                        Log.e("sssss","filter44444+++++++"+Thread.currentThread().getName());
                        return string.length()<=5;
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String string) {
                        Log.e("sssss","filter555555+++++++"+Thread.currentThread().getName());
                        return string.length()<=5;
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String string) {
                        Log.e("sssss","filter22222+++++++"+Thread.currentThread().getName());
                        return string.length()<=5;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<String, Observable<Character>>() {
                    @Override
                    public Observable<Character> call(String s) {
                        Log.e("sssss","flatMap2222+++++++"+Thread.currentThread().getName());
                        char[] chars = s.toCharArray();
                        ArrayList<Character> list = new ArrayList<>();
                        for(char c:chars){
                            list.add(c);
                        }
                        return Observable.from(list);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Character>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Character character) {
                        if(character == '4'){
                            Toast.makeText(RxJavaStudioActivity.this, character+"", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("sss","++++++++++++++++++++++"+character);
                    }
                });


    }



    public void test3(){


        Observable.from(new String[]{"1aa1123","321","242","402","as4"})
                .map(new Func1<String, Object>() {
                    @Override
                    public Object call(String s) {
                        String s1= new String(s);
                        s1+=s;
                        return s1;
                    }
                })
                .flatMap(new Func1<Object, Observable<Character>>() {
                    @Override
                    public Observable<Character> call(Object s) {
                        char[] chars = s.toString().toCharArray();
                        Character[] charArray=new Character[chars.length];
                        for(int i=0;i<charArray.length;i++){
                            charArray[i] = chars[i];
                        }
                        return Observable.from(charArray);
                    }
                }).filter(new Func1<Character, Boolean>() {
                    @Override
                    public Boolean call(Character character) {
                        int a = character;
                        return '9'>a&&a>'0';
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Character>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Character character) {
                        if(character=='4'){
                            Toast.makeText(RxJavaStudioActivity.this, character+"", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("ssss","++++++++++++++++++++++++++++++++++++++++++++++++++++++:::"+character);
                    }

                });




    }




}
