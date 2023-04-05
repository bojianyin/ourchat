package com.app.ourchat.utils;


public class DeviceUtil {

    public static String getUUID() {

        String uuid = SPUtil.INSTANCE.getString(SPUtil.UUID);
        if("".equals(uuid)){
            if(getHardware().equals("kirin9000")){
                SPUtil.INSTANCE.putString(SPUtil.UUID,Constant.userId1);
                IMUtil.currentUid = Constant.userId1;
                return Constant.userId1;
            }else{
                SPUtil.INSTANCE.putString(SPUtil.UUID,Constant.userId2);
                IMUtil.currentUid = Constant.userId2;
                return Constant.userId2;
            }
        }else{
            return uuid;
        }


//        String uuid = SPUtil.INSTANCE.getString(SPUtil.UUID);
//        if("".equals(uuid)){
//            uuid = (android.os.Build.BRAND+android.os.Build.HARDWARE+UUID.randomUUID().toString()).replace("-", "")
//                    .replace("_", "")
//                    .replace("/", "");
//            SPUtil.INSTANCE.putString(SPUtil.UUID,uuid);
//        }
//        return uuid;

    }

    public static String getHardware(){
        return android.os.Build.HARDWARE;
    }

}
