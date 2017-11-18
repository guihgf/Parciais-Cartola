package org.fermino.parciaisdocartola.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.fermino.parciaisdocartola.R;
import org.fermino.parciaisdocartola.pojos.LigaFavorita;
import org.fermino.parciaisdocartola.pojos.TimeFavorito;
import org.fermino.parciaisdocartola.pojos.TimeLiga;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guihgf on 12/07/2017.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/

    private static final String DATABASE_NAME = "parcial_cartola.db";
    private static final int DATABASE_VERSION = 9;

    private Dao<TimeFavorito, Integer> timeFavoritoDao;
    private Dao<LigaFavorita, Integer> ligaFavoritaDao;
    private Dao<TimeLiga, Integer> timeLigaDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, TimeFavorito.class);
            TableUtils.createTable(connectionSource, LigaFavorita.class);
            TableUtils.createTable(connectionSource, TimeLiga.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, TimeFavorito.class, true);
            TableUtils.dropTable(connectionSource, LigaFavorita.class, true);
            TableUtils.dropTable(connectionSource, TimeLiga.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<TimeFavorito, Integer> getTimeFavoritoDao() throws SQLException {
        if (timeFavoritoDao == null) {
            timeFavoritoDao = getDao(TimeFavorito.class);
        }
        return timeFavoritoDao;
    }

    public Dao<LigaFavorita, Integer> getLigaFavoritaDao() throws SQLException {
        if (ligaFavoritaDao == null) {
            ligaFavoritaDao = getDao(LigaFavorita.class);
        }
        return ligaFavoritaDao;
    }

    public Dao<TimeLiga, Integer> getTimeLigaDao() throws SQLException {
        if (timeLigaDao == null) {
            timeLigaDao = getDao(TimeLiga.class);
        }
        return timeLigaDao;
    }
}