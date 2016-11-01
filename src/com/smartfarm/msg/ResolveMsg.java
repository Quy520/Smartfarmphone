package com.smartfarm.msg;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.smartfarm.bean.LocalEvent;
import com.smartfarm.bean.OtherInfo;
import com.smartfarm.bean.TempConfigModel;
import com.smartfarm.bean.WaterInfoBean;
import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.db.bean.LightInfo;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.db.bean.WaterInfo;
import com.smartfarm.db.util.InfoRecordDao;
import com.smartfarm.db.util.LightInfoDao;
import com.smartfarm.db.util.PengInfoDao;
import com.smartfarm.db.util.TempConfigDao;
import com.smartfarm.db.util.WaterInfoDao;
import com.smartfarm.tools.AssistThreadWork;
import com.smartfarm.tools.CommonTool;
import com.smartfarm.tools.Constants;
import com.smartfarm.tools.EventBus;
import com.smartfarm.tools.HelperThread;
import com.smartfarm.tools.ShowUtil;
import com.smartfarm.tools.ToastTool;
import com.smartfarm.tools.XmlUtils;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.LockScreenShowActivity;

public class ResolveMsg {

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	public static void resolve(final Protocol protocol) {
		
		final PengInfo info = protocol.getInfo();
		Context context = AppContext.context();

		if (protocol.getCmdType() == Constants.MOTOR_CONTROL_TYPE_READ) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);  

			if (!pm.isScreenOn()) {

				ShowUtil.LightScreen(context);
				
				Intent it = new Intent();
				it.setClass(context, LockScreenShowActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				bundle.putString("msg", protocol.getData());
				bundle.putString("pad", protocol.getSender());
				bundle.putString("name", info.getName());
				it.putExtras(bundle);
				context.startActivity(it);
			}

			EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_TEMP, 0, "", 
					CommonTool.resolveTemp(protocol.getData(), protocol.getPadId(), protocol.getTime())));

			String title = "收到新的来自(" + info.getName() + ")的温度信息";
			ShowUtil.showNotice(context, title, CommonTool.getTempString(
					context, protocol.getSender(), protocol.getData()), 
					null, title, info.getId());
			
			InfoRecordDao.add(info, protocol.getData(), 
					InfoRecord.RECORD_TYPE_TEMP, protocol.getTime());
			
			return;
		}

		switch (protocol.getProtocolType()) {
		case Constants.PROTOCOL_TYPE_REQUEST:

			switch (protocol.getCmdType()) {
			case Constants.MOTOR_CONTROL_TYPE_ALARM:

				Log.d("mmsg", " receive alarm msg! context -> " + context);
				ShowUtil.showAlarm(context, "来自(" + info.getName() + ")的警告",
						protocol.getData(), info.getId());

				protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				protocol.setReceiver(info.getPhoneNum());
				protocol.setSender(AppContext.context().getUser().getPhone());
				protocol.setPwd(info.getPwd());
				InfoRecordDao.add(info, protocol.getData(), 
						InfoRecord.RECORD_TYPE_ALARM, protocol.getTime());
				
				protocol.send();
				break;
			case Constants.MOTOR_CONTROL_TYPE_TEST:

				protocol.setProtocolType(Constants.PROTOCOL_TYPE_RESPONSE);
				protocol.setReceiver(info.getPhoneNum());
				protocol.setSender(AppContext.context().getUser().getPhone());
				protocol.setPwd(info.getPwd());
				protocol.send();
				
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_NET_OK));
				break;
			}

			break;
		case Constants.PROTOCOL_TYPE_RESPONSE:
			switch (protocol.getCmdType()) {
			case Constants.MOTOR_CONTROL_TYPE_TEST:
				
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_NET_OK));
				Log.d("mmsg", "receive test msg from -> " + protocol.getSender());
				return;
			case Constants.MOTOR_CONTROL_TYPE_NOTICE:
				
				if(protocol.getData().equals("autochange")) {
					
					ShowUtil.showNotice(context, "来自(" + info.getName() + ")的高温提示", "大棚内温度已经超过高温界限，"
							+ "温控机已自动切换到自动模式进行调节，请注意查看大棚状态，以免给您造成不必要的损失，谢谢！", null);
				}
				return;
				
			case Constants.MOTOR_CONTROL_TYPE_WATER_READ:
				
				InfoRecordDao.add(info, protocol.getData(), InfoRecord.RECORD_TYPE_WATER, protocol.getTime());
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_READ, 0, "", new WaterInfoBean(protocol)));
				return;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_READ:
				
				InfoRecordDao.add(info, protocol.getData(), InfoRecord.RECORD_TYPE_LIGHT, protocol.getTime());
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_LIGHT_READ, 0, "", protocol));
				return;
			case Constants.MOTOR_CONTROL_TYPE_READ_OTHERS:
				
				InfoRecordDao.add(info, protocol.getData(), InfoRecord.RECORD_TYPE_OTHER, protocol.getTime());
				EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(
						LocalEvent.EVENT_TYPE_OTHER_READ, 0, "", new OtherInfo(protocol)));
				return;
			case Constants.MOTOR_CONTROL_TYPE_WATER_STATE:
				
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_STATE, protocol.getData()));
				return;
			case Constants.MOTOR_CONTROL_TYPE_WATER_MODE:
				
				if(ShowUtil.isEmpty(protocol.getData()))
					return;
				
				WaterInfo water = WaterInfoDao.findByPengId(info.getId());
				water.setWaterMode(protocol.getData().contains("auto"));
				WaterInfoDao.update(water);
				
				if(info.getId() == AppContext.context().getCurrPengInfo().getId()) {

					AppContext.context().setWaterInfo(water);
					EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_MODE, protocol.getData()));
				}
				
				return;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_MODE:
				
				if(ShowUtil.isEmpty(protocol.getData()))
					return;
				
				LightInfo lightMode = LightInfoDao.findByPengId(info.getId());
				lightMode.setMode(protocol.getData().contains("auto"));
				LightInfoDao.update(lightMode);
				
				if(info.getId() == AppContext.context().getCurrPengInfo().getId()) {

					AppContext.context().setLightInfo(lightMode);
					EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_LIGHT_MODE, protocol.getData()));
				}
				
				return;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_CONFIG:
				
				if(protocol.getWindowId().contains("get")) {
					
					String[] synData = protocol.getData().split("\\*");
					LightInfo light = LightInfoDao.findByPengId(info.getId());

					try {
						for (String s : synData) {

							if (s.contains("setMax")) {
								
								light.setMax(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setMin")) {
								
								light.setMin(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setNeed")) {
								
								light.setNeed(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setDiy")) {
								
								light.setDiy(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setStart")) {
								
								light.setStart(s.split(":", 2)[1]);
							}
						}
						
						LightInfoDao.update(light);

						if(AppContext.context().getCurrPengInfo().getId() == light.getPengId()) {

							AppContext.context().setLightInfo(light);
							EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_LIGHT_CONFIG));
						}

					} catch(Exception e) {}
				}
				
				break;
			case Constants.MOTOR_CONTROL_TYPE_WATER_CONFIG:
				
				if(protocol.getWindowId().contains("get")) {
					
					String[] synData = protocol.getData().split("\\*");
					WaterInfo waterinfo = WaterInfoDao.findByPengId(info.getId());

					try {
						for (String s : synData) {

							if (s.contains("setMin")) {
								
								waterinfo.setWaterMin(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setAlarmMaxEnable")) {
								
								waterinfo.setWaterMaxEnable(s.split(":", 2)[1].equals("true"));
							} else if (s.contains("setAlarmMinEnable")) {
								
								waterinfo.setWaterMinEnable(s.split(":", 2)[1].equals("true"));
							} else if (s.contains("setAlarmMax")) {
								
								waterinfo.setWaterAlarmMax(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setAlarmMin")) {
								
								waterinfo.setWaterAlarmMin(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setTime")) {
								
								waterinfo.setWaterTime(Integer.valueOf(s.split(":", 2)[1]));
							}
						}
						
						WaterInfoDao.update(waterinfo);
						
						if(info.getId() == AppContext.context().getCurrPengInfo().getId()) {
							
							AppContext.context().setWaterInfo(waterinfo);
							EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_WATER_CONFIG));
						}

					} catch(Exception e) {}
				}
			
				break;
			case Constants.MOTOR_CONTROL_TYPE_SYN_TEMP:
				
				if(!ShowUtil.isEmpty(protocol.getData())) {
					
					HelperThread.getInstance().setThreadWork(new AssistThreadWork() {
						
						@Override
						public void working() {
							try {
								
								TempConfigModel model = XmlUtils.toBean(TempConfigModel.class, protocol.getData().getBytes());

								info.setMoreMode(model.isMoreMode());
								
								if(model.isMoreMode()) {
									
									TempConfigDao.update(model.getTempConfigs(), info.getId());
									
								} else {
									
									TempConfig config = model.getTempConfig();
									
									info.setTempMax(config.getMax());
									info.setTempMin(config.getMin());
									info.setTempDiffMax(config.getNorMax());
									info.setTempDiffMin(config.getNorMin());
								}
								PengInfoDao.update(info);
								if(AppContext.context().getCurrPengInfo().getId() == info.getId())
									AppContext.context().refreshPengInfo();
							} catch(Exception e) {
								Log.d(Constants.TAG, " temp config data error ! ");
							}
						}
						
						@Override
						public void ok() {}
					});
				}
				
				return;
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_OPEN:
			case Constants.MOTOR_CONTROL_TYPE_LIGHT_CLOSE:
			case Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE:
			case Constants.MOTOR_CONTROL_TYPE_WATER_OPEN:
			case Constants.MOTOR_CONTROL_TYPE_CLOSE:
			case Constants.MOTOR_CONTROL_TYPE_STOP:
			case Constants.MOTOR_CONTROL_TYPE_OPEN:

				String controlRes = null;
				if(protocol.getData().contains("night"))
					controlRes = "当前正处于夜间模式，无法进行控制。如需控制请变更夜间模式时间。";
				else
					controlRes = CommonTool.GetResponseString(protocol);

				ToastTool.showToast(controlRes);
				
				if(protocol.getCmdType() == Constants.MOTOR_CONTROL_TYPE_WATER_CLOSE || 
						protocol.getCmdType() == Constants.MOTOR_CONTROL_TYPE_WATER_OPEN)
					ProtocolFactory.getWaterInfoProtocol(false).send();
				else if(protocol.getCmdType() == Constants.MOTOR_CONTROL_TYPE_LIGHT_OPEN || 
						protocol.getCmdType() == Constants.MOTOR_CONTROL_TYPE_LIGHT_CLOSE)
					ProtocolFactory.getReadLightInfoProtocol().send();
					
				return;
			case Constants.MOTOR_CONTROL_TYPE_MODE:

				if (protocol.getData().contains("error")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}

				if (protocol.getData().equals("")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}

				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_MODE_CHANGE, info.getId(), protocol.getData()));

				return;
			case Constants.MOTOR_CONTROL_TYPE_HIGH_MODE:
				
				if (protocol.getData().contains("error")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}

				if (protocol.getData().equals("")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_AUTO_OPEN_ENABLE, info.getId(), protocol.getData()));
				break;
			case Constants.MOTOR_CONTROL_TYPE_RAIN_MODE:
				
				if (protocol.getData().contains("error")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}

				if (protocol.getData().equals("")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_IS_RAIN, info.getId(), protocol.getData()));
				break;
				
			case Constants.MOTOR_CONTROL_TYPE_OPEN_WINDOW_MODE:
				if (protocol.getData().contains("error")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}

				if (protocol.getData().equals("")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_OPEN_WINDOWS, info.getId(), protocol.getData()));
				break;
				
			case Constants.MOTOR_CONTROL_TYPE_OPEN_WIND_END:
				
				if (protocol.getData().contains("error")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}

				if (protocol.getData().equals("")) {
					ToastTool.showToast(CommonTool.GetResponseString(protocol));
					break;
				}
				EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_OPEN_WINDS_END, info.getId(), protocol.getData()));
				break;
				
			case Constants.MOTOR_CONTROL_TYPE_SYN:

				if (protocol.getWindowId().contains("get")) {
					String[] synData = protocol.getData().split("\\*");

					try {
						for (String s : synData) {

							if (s.contains("setTempMin")) {
								
								info.setTempMin(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setTempMax")) {
								
								info.setTempMax(Integer.valueOf(s.split(":", 2)[1]));
							}else if (s.contains("isAlarmEnable")){
								
								info.setAlarmMsgEnable(s.split(":", 2)[1].contains("true"));
							}else if (s.contains("setThresholdTempMax")) {
								
								info.setTempDiffMax(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setThresholdTempMin")) {
								
								info.setTempDiffMin(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("setOpenLen")) {
								
								info.setLenMax(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("MorningTimePeriod")) {
								
								info.setVenTime(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("MorningOpenTime")) {
								
								info.setNightStart(s.split(":", 2)[1]);
							} else if (s.contains("NightCloseTime")) {
								
								info.setNightEnd(s.split(":", 2)[1]);
							} else if (s.contains("alarmMax")) {
								
								info.setAlarmTempMax(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("alarmMin")) {
								
								info.setAlarmTempMin(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("pushTime")) {
								
								info.setPushTime(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("o1")) {

								info.setLenFirst(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("o2")) {

								info.setOpenSecond(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("o3")) {

								info.setOpenThird(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("o4")) {

								info.setOpenFourth(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("o5")) {

								info.setOpenFifth(Integer.valueOf(s.split(":", 2)[1]));
							} else if (s.contains("ha")) {

								info.setAlarmMaxEnable(s.split(":", 2)[1].contains("true"));
							} else if (s.contains("la")) {

								info.setAlarmMinEnable(s.split(":", 2)[1].contains("true"));
							} 
						}

						PengInfoDao.update(info);
						AppContext.context().refreshPengInfo();

						EventBus.getDefault().post(LocalEvent.getEvent(LocalEvent.EVENT_TYPE_CONFIG_CHANGE));

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}

			ToastTool.showToast(CommonTool.GetResponseString(protocol));
		}
	}
}
