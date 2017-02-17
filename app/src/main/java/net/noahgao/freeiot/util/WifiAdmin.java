package net.noahgao.freeiot.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
/**
 * WIFI管理类
 * @author ZHF
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
     * 获取该类的实例（懒汉）
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
    private WifiConfiguration isExsits(String str) {
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
    /**端口指定id的wifi**/
    public void disconnectWifi(int paramInt) {
        this.mWifiManager.disableNetwork(paramInt);
    }

    /**添加指定网络**/
    public void addNetwork(WifiConfiguration paramWifiConfiguration) {
        int i = mWifiManager.addNetwork(paramWifiConfiguration);
        mWifiManager.enableNetwork(i, true);
    }

    /**
     * 连接指定配置好的网络
     * @param index 配置好网络的ID
     */
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        //连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
    }

    /**
     * 根据wifi信息创建或关闭一个热点
     * @param paramWifiConfiguration
     * @param paramBoolean 关闭标志
     */
    @SuppressWarnings({"unchecked"})
    public void createWifiAP(WifiConfiguration paramWifiConfiguration,boolean paramBoolean) {
        try {
            Class localClass = this.mWifiManager.getClass();
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = WifiConfiguration.class;
            arrayOfClass[1] = Boolean.TYPE;
            Method localMethod = localClass.getMethod("setWifiApEnabled",arrayOfClass);
            WifiManager localWifiManager = this.mWifiManager;
            Object[] arrayOfObject = new Object[2];
            arrayOfObject[0] = paramWifiConfiguration;
            arrayOfObject[1] = paramBoolean;
            localMethod.invoke(localWifiManager, arrayOfObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建一个wifi信息
     * @param ssid 名称
     * @param passawrd 密码
     * @param paramInt 有3个参数，1是无密码，2是简单密码，3是wap加密
     * @param type 是"ap"还是"wifi"
     * @return
     */
    public WifiConfiguration createWifiInfo(String ssid, String passawrd,int paramInt, String type) {
        //配置网络信息类
        WifiConfiguration localWifiConfiguration1 = new WifiConfiguration();
        //设置配置网络属性
        localWifiConfiguration1.allowedAuthAlgorithms.clear();
        localWifiConfiguration1.allowedGroupCiphers.clear();
        localWifiConfiguration1.allowedKeyManagement.clear();
        localWifiConfiguration1.allowedPairwiseCiphers.clear();
        localWifiConfiguration1.allowedProtocols.clear();

        if(type.equals("wt")) { //wifi连接
            localWifiConfiguration1.SSID = ("\"" + ssid + "\"");
            WifiConfiguration localWifiConfiguration2 = isExsits(ssid);
            if(localWifiConfiguration2 != null) {
                mWifiManager.removeNetwork(localWifiConfiguration2.networkId); //从列表中删除指定的网络配置网络
            }
            if(paramInt == 1) { //没有密码
                localWifiConfiguration1.wepKeys[0] = "";
                localWifiConfiguration1.allowedKeyManagement.set(0);
                localWifiConfiguration1.wepTxKeyIndex = 0;
            } else if(paramInt == 2) { //简单密码
                localWifiConfiguration1.hiddenSSID = true;
                localWifiConfiguration1.wepKeys[0] = ("\"" + passawrd + "\"");
            } else { //wap加密
                localWifiConfiguration1.preSharedKey = ("\"" + passawrd + "\"");
                localWifiConfiguration1.hiddenSSID = true;
                localWifiConfiguration1.allowedAuthAlgorithms.set(0);
                localWifiConfiguration1.allowedGroupCiphers.set(2);
                localWifiConfiguration1.allowedKeyManagement.set(1);
                localWifiConfiguration1.allowedPairwiseCiphers.set(1);
                localWifiConfiguration1.allowedGroupCiphers.set(3);
                localWifiConfiguration1.allowedPairwiseCiphers.set(2);
            }
        }else {//"ap" wifi热点
            localWifiConfiguration1.SSID = ssid;
            localWifiConfiguration1.allowedAuthAlgorithms.set(1);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            localWifiConfiguration1.allowedKeyManagement.set(0);
            localWifiConfiguration1.wepTxKeyIndex = 0;
            if (paramInt == 1) {  //没有密码
                localWifiConfiguration1.wepKeys[0] = "";
                localWifiConfiguration1.allowedKeyManagement.set(0);
                localWifiConfiguration1.wepTxKeyIndex = 0;
            } else if (paramInt == 2) { //简单密码
                localWifiConfiguration1.hiddenSSID = true;//网络上不广播ssid
                localWifiConfiguration1.wepKeys[0] = passawrd;
            } else if (paramInt == 3) {//wap加密
                localWifiConfiguration1.preSharedKey = passawrd;
                localWifiConfiguration1.allowedAuthAlgorithms.set(0);
                localWifiConfiguration1.allowedProtocols.set(1);
                localWifiConfiguration1.allowedProtocols.set(0);
                localWifiConfiguration1.allowedKeyManagement.set(1);
                localWifiConfiguration1.allowedPairwiseCiphers.set(2);
                localWifiConfiguration1.allowedPairwiseCiphers.set(1);
            }
        }
        return localWifiConfiguration1;
    }

    /**获取热点名**/
    public String getApSSID() {
        try {
            Method localMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration");
            if (localMethod == null) return null;
            Object localObject1 = localMethod.invoke(this.mWifiManager);
            if (localObject1 == null) return null;
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
            if (localWifiConfiguration.SSID != null) return localWifiConfiguration.SSID;
            Field localField1 = WifiConfiguration.class .getDeclaredField("mWifiApProfile");
            if (localField1 == null) return null;
            localField1.setAccessible(true);
            Object localObject2 = localField1.get(localWifiConfiguration);
            localField1.setAccessible(false);
            if (localObject2 == null)  return null;
            Field localField2 = localObject2.getClass().getDeclaredField("SSID");
            localField2.setAccessible(true);
            Object localObject3 = localField2.get(localObject2);
            if (localObject3 == null) return null;
            localField2.setAccessible(false);
            return (String) localObject3;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**获取wifi名**/
    public String getBSSID() {
        if (this.mWifiInfo == null)
            return "NULL";
        return this.mWifiInfo.getBSSID();
    }

    /**得到配置好的网络 **/
    public List<WifiConfiguration> getConfiguration() {
        return this.mWifiConfiguration;
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

    /** 设置wifi搜索结果 **/
    public void setWifiList() {
        this.mWifiList = this.mWifiManager.getScanResults();
    }
    /**开始搜索wifi**/
    public boolean startScan() {
        return this.mWifiManager.startScan();
    }
    /**得到接入点的BSSID**/
    public String GetBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }
}