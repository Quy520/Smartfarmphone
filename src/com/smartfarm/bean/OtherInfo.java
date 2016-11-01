package com.smartfarm.bean;

import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.msg.Protocol;
/**
 * 其他信息，co2，湿度，光照，大棚id 时间。
 * @author QSD
 *
 */
public class OtherInfo {

    private int CO2;
    private float Humidity;
    private int Light;
    private int id;
    private long time;
    private String sourceData;

    public OtherInfo() {}

    public OtherInfo(Protocol protocol) {
    	
        String[] infoStrs = protocol.getData().split(";");
        
        id = protocol.getPadId();
        time = protocol.getTime();
        this.CO2 = Integer.parseInt(getValue(infoStrs[0]));
        Humidity = Float.parseFloat(getValue(infoStrs[1]));
        Light = Integer.parseInt(getValue(infoStrs[2]));
        sourceData = protocol.getData();
    }

    public OtherInfo(InfoRecord record) {
    	
        String[] infoStrs = record.getData().split(";");
        
        id = record.getPengId();
        time = record.getTime();
        this.CO2 = Integer.parseInt(getValue(infoStrs[0]));
        Humidity = Float.parseFloat(getValue(infoStrs[1]));
        Light = Integer.parseInt(getValue(infoStrs[2]));
        sourceData = record.getData();
    }

    public String getSourceData() {
		return sourceData;
	}

	public int getId() {
		return id;
	}

	public long getTime() {
		return time;
	}

	private String getValue(String str) {
        return str.split("=")[1];
    }
    
    public int getCO2() {
        return CO2;
    }

    public void setCO2(int CO2) {
        this.CO2 = CO2;
    }

    public float getHumidity() {
        return Humidity;
    }

    public void setHumidity(int humidity) {
        Humidity = humidity;
    }

    public int getLight() {
        return Light;
    }

    public void setLight(int light) {
        Light = light;
    }
}
