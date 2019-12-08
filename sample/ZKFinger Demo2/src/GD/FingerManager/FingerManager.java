package GD.FingerManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.org.apache.xalan.internal.xsltc.runtime.ErrorMessages_zh_CN;
import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
import com.zkteco.biometric.*;


/**
 * Copyright (C), 20019-2020, HeFei.
 * FileName: FingerManager
 * It's FingerManager control the hardware of fingersensor.
 *
 * @author Wispytrace
 * @Date   2019/11/08
 * @version 1.00
 */
public class FingerManager {
    public static long myDevice = 0;
    private static long myAlgorithms = 0;
    public static int figureHeight = 0;
    public static int figureWidth = 0;
    public static final int DPI = 500;
    public static final int templateLen = 2048;
    private static final int ACCURACY_GATE = 60;
    private static boolean isEnroll = false;
    public static final int CONFIRM_TIMES = 3;
    public static HashMap<Integer, Integer> Online = new HashMap<Integer, Integer>();
    /**
     * Init device
     *
     * param
     * return
     * throws Exception, it reveals error.
     */
    public static void deviceInit() throws Exception {
        if (myDevice != 0) {
            throw new Exception("Please Close Device First");
        }
        if (FingerprintSensorErrorCode.ZKFP_ERR_OK != FingerprintSensorEx.Init()){
            throw new Exception("Device Init Fail");
        }
        if (FingerprintSensorEx.GetDeviceCount() < 0){
            throw new Exception("No Device Connect");
        }
        if ((myDevice = FingerprintSensorEx.OpenDevice(0)) == 0){
            throw new Exception("Open Device Fail");
        }
        if ((myAlgorithms = FingerprintSensorEx.DBInit()) == 0){
            throw new Exception("Alogorithms Base Init Fail");
        }
        byte[] paramValue = new byte[4];
        int[] size = new int[1];
        size[0] = 4;
        FingerprintSensorEx.GetParameters(myDevice, 1, paramValue, size);
        figureWidth = byteArrayToInt(paramValue);
        FingerprintSensorEx.GetParameters(myDevice, 2, paramValue, size);
        figureHeight = byteArrayToInt(paramValue);
        fingerLoad("C:\\Users\\10254\\Documents\\GraduateDesign\\最新的 ZKFinger SDK 5.0.0.29\\ZKFinger SDK 5.0.0.29\\Java\\sample\\ZKFinger Demo2\\DbBase");
    }
    /**
     * Close device
     *
     * param
     * return
     * throws Exception, it reveals error.
     */
    public static void deviceClose() throws Exception{
        Thread.sleep(1000);
        if (myAlgorithms != 0){
            FingerprintSensorEx.DBFree(myAlgorithms);
            myAlgorithms = 0;
        }
        if (myDevice != 0){
            FingerprintSensorEx.CloseDevice(myDevice);
            myDevice = 0;
        }
        FingerprintSensorEx.Terminate();
    }
    /**
     * Acquire finger picture
     *
     * param Devicehandle, imgbBuff, template
     * return 0 reveals  successful, -1 reveals failed.
     * throws Exception, it reveals error.
     */
    public static void figureAcquire(byte[] imgBuf, byte[] template) throws Exception{
        int[] len = {templateLen};
        if ((FingerprintSensorEx.AcquireFingerprint(myDevice, imgBuf, template, len)) != 0){
            throw new IOException();
        }
        else {
            byte[] paramValue = new byte[4];
            int[] size = new int[1];
            size[0] = 4;
            int ret = FingerprintSensorEx.GetParameters(myDevice, 2004, paramValue, size);
            int isFake = byteArrayToInt(paramValue);
            if (0 != ret || (byte) (isFake & 31) != 31) {
                throw new Exception();
            }
        }
    }
    /**
     * Match the template of two fingerprints
     *
     * param Algorithms base, the source  template, the destination template
     * return
     * throws it receals error
     */
    public static void fingerMatch(byte[] srcTemplate, byte[] desTemplate) throws Exception{
        int result = FingerprintSensorEx.DBMatch(myAlgorithms, srcTemplate, desTemplate);
        if (result < ACCURACY_GATE){
            throw new Exception("Fail to Match, Please try again");
        }
    }
    /**
     * Match the template of two fingerprints
     *
     * param Algorithms base, the fingerprint template, the rank of the fingerprint in memory
     * return
     * throws it receals error
     */
    public static void fingerIdentity(byte[] template, int[] fid) throws Exception{
        int[] score = new int[1];
        if (FingerprintSensorEx.DBIdentify(myAlgorithms, template, fid, score) < 0){
            throw new Exception("Fail To Find a Fingerprint In memory, Please Register First");
        }
    }
    /**
     * Store the fingerprint to disk
     *
     * param the byte array of img, the img path
     * return
     * throws it receals error
     */
    public static void fingerFileWrite(byte[] imgbuf, String path) throws Exception{
        java.io.FileOutputStream fos = new java.io.FileOutputStream(path);
        java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);

        int w = (((figureWidth+3)/4)*4);
        int bfType = 0x424d;
        int bfSize = 54 + 1024 + w * figureHeight;
        int bfReserved1 = 0;
        int bfReserved2 = 0;
        int bfOffBits = 54 + 1024;

        dos.writeShort(bfType);
        dos.write(intToByteArray(bfSize), 0, 4);
        dos.write(intToByteArray(bfReserved1), 0, 2);
        dos.write(intToByteArray(bfReserved2), 0, 2);
        dos.write(intToByteArray(bfOffBits), 0, 4);

        int biSize = 40;
        int biWidth = figureWidth;
        int biHeight = figureHeight;
        int biPlanes = 1;
        int biBitcount = 8;
        int biCompression = 0;
        int biSizeImage = w * figureHeight;
        int biXPelsPerMeter = 0;
        int biYPelsPerMeter = 0;
        int biClrUsed = 0;
        int biClrImportant = 0;

        dos.write(intToByteArray(biSize), 0, 4);
        dos.write(intToByteArray(biWidth), 0, 4);
        dos.write(intToByteArray(biHeight), 0, 4);
        dos.write(intToByteArray(biPlanes), 0, 2);
        dos.write(intToByteArray(biBitcount), 0, 2);
        dos.write(intToByteArray(biCompression), 0, 4);
        dos.write(intToByteArray(biSizeImage), 0, 4);
        dos.write(intToByteArray(biXPelsPerMeter), 0, 4);
        dos.write(intToByteArray(biYPelsPerMeter), 0, 4);
        dos.write(intToByteArray(biClrUsed), 0, 4);
        dos.write(intToByteArray(biClrImportant), 0, 4);

        for (int i = 0; i < 256; i++) {
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(i);
            dos.writeByte(0);
        }

        byte[] filter = null;
        if (w > figureWidth)
        {
            filter = new byte[w-figureWidth];
        }

        for(int i=0;i<figureHeight;i++)
        {
            dos.write(imgbuf, (figureHeight-1-i)*figureWidth, figureWidth);
            if (w > figureWidth)
                dos.write(filter, 0, w-figureWidth);
        }
        dos.flush();
        dos.close();
        fos.close();
    }
    /**
     * Read the fingerprint from disk
     *
     * param the img path, the return of the fingerprint picture file
     * return
     * throws it receals error
     */
    public static void fingerFileRead(String path, byte[] returnTemplate) throws Exception{
        int[] len = {templateLen};
        path = "C:\\\\Users\\\\10254\\\\Documents\\\\GraduateDesign\\\\最新的 ZKFinger SDK 5.0.0.29\\\\ZKFinger SDK 5.0.0.29\\\\Java\\\\sample\\\\ZKFinger Demo2\\\\DbBase\\\\" + path;
        if (FingerprintSensorEx.ExtractFromImage(myAlgorithms, path, DPI, returnTemplate, len) != 0){
            throw new Exception("Fail To Extract imagine");
        }
    }
    /**
     * Begin Enrolling fingerprint function
     *
     * param  the person information, the id reveals the primary key  of the person, the Template need to conbine, img to store
     * return
     * throws it receals error
     */
    public void fingerEnroll(int[] id, byte[][] preRegTemplate, byte[] img) throws Exception{
        if (myDevice ==0 || myAlgorithms == 0){
            throw new Exception("Please Open Device Or Algorithms Or Open Enroll Function First");
        }
        byte[] template = new byte[templateLen];
        int[] regLen = {templateLen};
        int ret;
        try{
             if(FingerprintSensorEx.DBMerge(myAlgorithms, preRegTemplate[0], preRegTemplate[1], preRegTemplate[2], template, regLen) != 0){
             throw new Exception("DB Merege Failed");
           }
            id[0] = FingerprintSensorEx.DBCount(myAlgorithms) + 1;
            fingerFileWrite(img, "C:\\\\Users\\\\10254\\\\Documents\\\\GraduateDesign\\\\最新的 ZKFinger SDK 5.0.0.29\\\\ZKFinger SDK 5.0.0.29\\\\Java\\\\sample\\\\ZKFinger Demo2\\\\DbBase\\\\"+id[0]+".bmp");
            if(FingerprintSensorEx.DBAdd(myAlgorithms, id[0], template) != 0){
                throw new Exception("DB Add Failed!");
            }
        }catch (Exception e){
                throw new Exception(e.getMessage()+"\tEnroll Fail! Please Open Enroll And Press Finger 3 Times Again");
        }
    }
    /**
     * Merge the 3 finger figures' template to generate the merged template
     *
     * param  prestored 3 figures, the template will be generated
     * return
     * throws it receals error
     */
    public void fingerMerge(byte[][] preRegTemplate, byte[] template) throws Exception{
        int[] regLen = {templateLen};
        if (FingerprintSensorEx.DBMerge(myAlgorithms, preRegTemplate[0], preRegTemplate[1], preRegTemplate[2], template, regLen) != 0){
            throw new Exception("DB Template Merge Failed");
        }
    }
    /**
     * Add finger template in to memory
     *
     * finger id, template
     * return
     * throws it receals error
     */
    public void fingerAdd(int fid, byte[] template) throws Exception{
        if (FingerprintSensorEx.DBAdd(myAlgorithms, fid, template) != 0){
            throw new Exception("Fail To Add User Template In To Memory");
        }
    }
    /**
     * Add finger template in to memory
     *
     *
     * return the finger number in memroy
     * throws it receals error
     */
    public int fingerCount() throws Exception{
        int count = FingerprintSensorEx.DBCount(myAlgorithms);
        if (count <  0){
            throw new Exception("Fail To Get Count Of Finger In Memory");
        }
        return count;
    }
    /**
     * delete finger template from memory
     *
     *the finger id in memory
     *
     * throws it receals error
     */
    public void fingerDelete(int fid) throws Exception{
        if (FingerprintSensorEx.DBDel(myAlgorithms, fid) != 0){
            throw new Exception("Fail To Delete Finger " + fid +" In memory");
        }
    }
    /**
     * Set thE color of fingersensor
     *
     * param the color
     * return
     * throws it receals error
     */
    public static void lightControl(String Color) throws Exception{
        int ret = -1;
        int len = 4;
        byte[] value = new byte[4];
        value = intToByteArray(1);
        switch (Color) {
            case "green":
                ret = ZKFPService.SetParameter(myDevice, 102, value, len);
                break;
            case "red":
                ret = ZKFPService.SetParameter(myDevice, 103, value, len);
                break;
            case "white":
                ret = ZKFPService.SetParameter(myDevice, 101, value, len);
                break;
        }
        if (ret == -1){
            throw new Exception("Device Failed!");
        }
    }
    /**
     * Translate int to byte array type
     *
     * param int number
     * return byte array
     * throws
     */
    public static byte[] intToByteArray (final int number) {
        byte[] abyte = new byte[4];
        abyte[0] = (byte) (0xff & number);
        abyte[1] = (byte) ((0xff00 & number) >> 8);
        abyte[2] = (byte) ((0xff0000 & number) >> 16);
        abyte[3] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }
    /**
     * Load users' finger print from directory
     *
     * param directory path
     * return
     * throws
     */
    private static void fingerLoad(String path) throws Exception {
        File dir = new File(path);
        if (!dir.exists()){
            throw new Exception(path+"Director Not Exists");
        }
        String[] finger = dir.list();
        for (String index: finger){
            byte[] template = new byte[templateLen];
            fingerFileRead(index, template);
            FingerprintSensorEx.DBAdd(myAlgorithms, index.toCharArray()[0], template);
        }
    }
    /**
     * Translate int to byte array type
     *
     * param byte array
     * return int number
     * throws
     */
    public static int byteArrayToInt(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }
}
