package net.noahgao.freeiot.util;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
/**
 * WIFI管理工具类
 * @author ZHF / Ziheng Gao Refactory on 2017.2
 *
 */
public class WifiAdmin {
    private static  WifiAdmin wifiAdmin = null;

    private List<WifiConfiguration> mWifiConfiguration; //无线网络配置信息类集合(网络连接列表)
    private List<ScanResult> mWifiList; //检测到接入点信息类 集合

    //描述任何Wifi连接状态
    private WifiInfo mWifiInfo;

    WifiManager.WifiLock mWifilock; //能够阻止wifi进入睡眠状态，使wifi一直处于活跃状态
    public WifiManager mWifiManager;

    /**
     * 获取该类的实例
     * @param context
     * @return
     */
    public static WifiAdmin getInstance(Context context) {
        if(wifiAdmin == null) {
            wifiAdmin = new WifiAdmin(context);
            return wifiAdmin;
        } else return wifiAdmin;
    }
    private WifiAdmin(Context context) {
        //获取系统Wifi服务   WIFI_SERVICE
        this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //获取连接信息
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    /**
     * 是否存在网络信息
     * @param str  热点名称
     * @return
     */
    public WifiConfiguration isExsits(String str) {
        Iterator<WifiConfiguration> localIterator = this.mWifiManager.getConfiguredNetworks().iterator();
        WifiConfiguration localWifiConfiguration;
        do {
            if(!localIterator.hasNext()) return null;
            localWifiConfiguration = localIterator.next();
        }while(!localWifiConfiguration.SSID.equals("\"" + str + "\""));
        return localWifiConfiguration;
    }

    /**锁定WifiLock，当下载大文件时需要锁定 **/
    public void AcquireWifiLock() {
        this.mWifilock.acquire();
    }
    /**创建一个WifiLock**/
    public void CreateWifiLock() {
        this.mWifilock = this.mWifiManager.createWifiLock("Test");
    }
    /**解锁WifiLock**/
    public void ReleaseWifilock() {
        if(mWifilock.isHeld()) { //判断时候锁定
            mWifilock.acquire();
        }
    }

    /**打开Wifi**/
    public void OpenWifi() {
        if(!this.mWifiManager.isWifiEnabled()){ //当前wifi不可用
            this.mWifiManager.setWifiEnabled(true);
        }
    }
    /**关闭Wifi**/
    public void closeWifi() {
        if(mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 添加到指定Wifi网络 /切换到指定Wifi网络
     * @param wf
     * @return
     */
    public void addNetwork(WifiConfiguration wf){

        //连接新的连接
        int netId = mWifiManager.addNetwork(wf);
        mWifiManager.enableNetwork(netId, true);
        mWifiManager.saveConfiguration();
        mWifiManager.reconnect();
    }

    /**
     * 关闭当前的Wifi网络
     * @return
     */
    public boolean disconnectCurrentNetwork(){
        if(mWifiManager != null && mWifiManager.isWifiEnabled()){
            int netId = mWifiManager.getConnectionInfo().getNetworkId();
            mWifiManager.disableNetwork(netId);
            return mWifiManager.disconnect();
        }
        return false;
    }

    /**
     * 创建WifiConfiguration
     *
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    public WifiConfiguration createWifiCfg(String SSID, String Password, int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = isExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) //WIFICIPHER_NOPASS
        {
          /*  config.wepKeys[0] = "";//连接无密码热点时加上这两句会出错
            config.wepTxKeyIndex = 0;*/
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        if (Type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    /**获取ip地址**/
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }
    /**获取物理地址(Mac)**/
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    /**获取网络id**/
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }
    /**获取热点创建状态**/
    public int getWifiApState() {
        try {
            return (int) (Integer) this.mWifiManager.getClass()
                    .getMethod("getWifiApState", new Class[0])
                    .invoke(this.mWifiManager);
        } catch (Exception ignored) {
        }
        return 4;   //未知wifi网卡状态
    }
    /**获取wifi连接信息**/
    public WifiInfo getWifiInfo() {
        return this.mWifiManager.getConnectionInfo();
    }
    /** 得到网络列表**/
    public List<ScanResult> getWifiList() {
        this.mWifiList = this.mWifiManager.getScanResults();
        return this.mWifiList;
    }

    /**查看扫描结果**/
    public StringBuilder lookUpScan() {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++)
        {
            localStringBuilder.append("Index_").append(Integer.toString(i + 1)).append(":");
            //将ScanResult信息转换成一个字符串包
            //其中把包括：BSSID、SSID、capabilities、frequency、level
            localStringBuilder.append((mWifiList.get(i)).toString());
            localStringBuilder.append("\n");
        }
        return localStringBuilder;
    }

    /**开始搜索wifi**/
    public boolean startScan() {
        return this.mWifiManager.startScan();
    }
}