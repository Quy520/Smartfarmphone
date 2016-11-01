package com.smartfarm.db.util;

import java.io.File;
import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.smartfarm.db.bean.InfoRecord;
import com.smartfarm.db.bean.LightInfo;
import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.db.bean.TempConfig;
import com.smartfarm.db.bean.User;
import com.smartfarm.db.bean.WaterInfo;
import com.smartfarm.tools.ExceptionUtils;
import com.smartfarm.view.AppContext;

@SuppressLint("SdCardPath")
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public static final String DATABASE_TEMP = "/data/data/com.smartfarm.view/databases/main";
	private static final String DATABASE_NAME = "smartfarm_db";

	private static DatabaseHelper dbHelper;
	private boolean mainTmpDirSet = false;

	public static DatabaseHelper getDbHelper() {

		if (dbHelper == null) {

			synchronized (DatabaseHelper.class) {

				if (dbHelper == null)
					dbHelper = new DatabaseHelper(AppContext.context(),
							DATABASE_NAME, null, 11);
			}
		}

		return dbHelper;
	}

	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {

		super(context, databaseName, factory, databaseVersion);

		Log.d("mmsg", " create DatabaseHelper !");
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {

		try {

			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, LightInfo.class);
			TableUtils.createTable(connectionSource, PengInfo.class);
			TableUtils.createTable(connectionSource, WaterInfo.class);
			TableUtils.createTable(connectionSource, InfoRecord.class);
			TableUtils.createTable(connectionSource, TempConfig.class);
		} catch (SQLException e) {
			ExceptionUtils.report(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.createTable(connectionSource, User.class);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			if (oldVersion == 8) {
				database.execSQL("ALTER TABLE `peng_info` ADD COLUMN 'alarm_msg_enable' BOOLEAN DEFAULT 0;");
			} else if (oldVersion < 8) {
				database.execSQL("ALTER TABLE `peng_info` ADD COLUMN 'high_auto' BOOLEAN DEFAULT 0;");
				database.execSQL("ALTER TABLE `peng_info` ADD COLUMN 'open_all' BOOLEAN DEFAULT 0;");
				database.execSQL("ALTER TABLE `peng_info` ADD COLUMN 'close_all' BOOLEAN DEFAULT 0;");
				database.execSQL("ALTER TABLE `peng_info` ADD COLUMN 'alarm_msg_enable' BOOLEAN DEFAULT 0;");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*	*/

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {

		SQLiteDatabase dbInstance = super.getWritableDatabase();

		if (!mainTmpDirSet) {

			new File(DATABASE_TEMP).mkdir();

			dbInstance.execSQL("PRAGMA temp_store_directory = '"
					+ DATABASE_TEMP + "'");
			mainTmpDirSet = true;
		}

		return dbInstance;
	}

	// @Override
	// public void close() {
	//
	// dbHelper = null;
	// super.close();
	// }
	//
	// public void clearDb() {
	//
	// File db = new File(DATABASE_DIR + DATABASE_NAME);
	// db.delete();
	// db = new File(DATABASE_DIR + DATABASE_JOURNAL);
	// db.delete();
	// }
}
