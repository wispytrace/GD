package com.zkteco.biometric;

public class LightControl {
    private int ret = -1;
    public   LightControl(long Device, String Color){
        int len = 4;
        byte[] value = new byte[4];
        value[0] = 1;
        value[1] = 0;
        value[2] = 0;
        value[3] = 0;
        switch (Color) {
            case "green":
                ret = ZKFPService.SetParameter(Device, 102, value, len);
                break;
            case "red":
                ret = ZKFPService.SetParameter(Device, 103, value, len);
                break;
            case "white":
                ret = ZKFPService.SetParameter(Device, 101, value, len);
                break;
        }
    }
    public  int getRet(){
        return ret;
    }
}
