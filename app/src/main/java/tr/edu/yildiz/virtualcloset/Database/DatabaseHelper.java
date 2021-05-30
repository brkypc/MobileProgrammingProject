package tr.edu.yildiz.virtualcloset.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.Model.Drawer;
import tr.edu.yildiz.virtualcloset.Model.Event;
import tr.edu.yildiz.virtualcloset.Model.Outfit;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "virtualCloset.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DRAWERS_TABLE = "drawers";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COUNT = "count";

    private static final String CLOTHES_TABLE = "clothes";
    private static final String COLUMN_DRAWER = "drawerNo";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_PATTERN = "pattern";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_PHOTO = "photo";

    private static final String OUTFITS_TABLE = "outfit";
    private static final String COLUMN_OVERHEAD = "overhead";
    private static final String COLUMN_FACE = "face";
    private static final String COLUMN_UPPER = "upper";
    private static final String COLUMN_LOWER = "lower";
    private static final String COLUMN_FOOT = "foot";

    private static final String EVENTS_TABLE = "events";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_OUTFIT = "outfitNo";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDrawersTable = "CREATE TABLE " + DRAWERS_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COUNT + " INTEGER, " +
                COLUMN_NAME + " TEXT);";

        String createClothesTable = "CREATE TABLE " + CLOTHES_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DRAWER + " INTEGER, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_COLOR + " TEXT, " +
                COLUMN_PATTERN + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_PHOTO + " BLOB);";

        String createOutfitsTable = "CREATE TABLE " + OUTFITS_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OVERHEAD + " INTEGER, " +
                COLUMN_FACE + " INTEGER, " +
                COLUMN_UPPER + " INTEGER, " +
                COLUMN_LOWER + " INTEGER, " +
                COLUMN_FOOT + " INTEGER);";

        String createEventsTable = "CREATE TABLE " + EVENTS_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OUTFIT + " INTEGER, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_LOCATION + " TEXT);";

        db.execSQL(createDrawersTable);
        db.execSQL(createClothesTable);
        db.execSQL(createOutfitsTable);
        db.execSQL(createEventsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addDrawer(String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_COUNT, 0);
        database.insert(DRAWERS_TABLE, null, contentValues);
    }

    public void addClothes(Clothes clothes) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DRAWER, clothes.getDrawerNo());
        contentValues.put(COLUMN_TYPE, clothes.getType());
        contentValues.put(COLUMN_COLOR, clothes.getColor());
        contentValues.put(COLUMN_PATTERN, clothes.getPattern());
        contentValues.put(COLUMN_DATE, clothes.getDate());
        contentValues.put(COLUMN_PRICE, clothes.getPrice());
        contentValues.put(COLUMN_PHOTO, clothes.getPhoto());
        database.insert(CLOTHES_TABLE, null, contentValues);
    }

    public void addOutfit(int[] clothes) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_OVERHEAD, clothes[0]);
        contentValues.put(COLUMN_FACE, clothes[1]);
        contentValues.put(COLUMN_UPPER, clothes[2]);
        contentValues.put(COLUMN_LOWER, clothes[3]);
        contentValues.put(COLUMN_FOOT, clothes[4]);

        database.insert(OUTFITS_TABLE, null, contentValues);
    }

    public void addEvent(Event event) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_OUTFIT, event.getOutfitNo());
        contentValues.put(COLUMN_NAME, event.getName());
        contentValues.put(COLUMN_TYPE, event.getType());
        contentValues.put(COLUMN_DATE, event.getDate());
        contentValues.put(COLUMN_LOCATION, event.getLocation());

        database.insert(EVENTS_TABLE, null, contentValues);
    }

    public void deleteDrawer(int Id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DRAWERS_TABLE, "id = ?", new String[]{String.valueOf(Id)});
    }

    public void updateClothes(Clothes clothes, int Id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DRAWER, clothes.getDrawerNo());
        contentValues.put(COLUMN_TYPE, clothes.getType());
        contentValues.put(COLUMN_COLOR, clothes.getColor());
        contentValues.put(COLUMN_PATTERN, clothes.getPattern());
        contentValues.put(COLUMN_DATE, clothes.getDate());
        contentValues.put(COLUMN_PRICE, clothes.getPrice());
        contentValues.put(COLUMN_PHOTO, clothes.getPhoto());

        database.update(CLOTHES_TABLE, contentValues, "id = ?", new String[]{String.valueOf(Id)});
    }

    public void deleteClothes(int Id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(CLOTHES_TABLE, "id = ?", new String[]{String.valueOf(Id)});
    }

    public void deleteOutfit(int Id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(OUTFITS_TABLE, "id = ?", new String[]{String.valueOf(Id)});
    }

    public void deleteEvent(int Id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(EVENTS_TABLE, "id = ?", new String[]{String.valueOf(Id)});
    }

    public ArrayList<Drawer> getDrawers() {
        ArrayList<Drawer> drawers = new ArrayList<>();
        String name; int id, count;

        SQLiteDatabase database = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + DRAWERS_TABLE;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(rawQuery, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);            // id
                count = cursor.getInt(1);        // count
                name = cursor.getString(2);     // name

                drawers.add(new Drawer(id, count, name));
            }while (cursor.moveToNext());

            return drawers;
        }
        return null;
    }

    public Clothes getClothes(int Id) {
        int id, drawerNo;
        String type, color, pattern, date, price;
        byte[] photo;

        SQLiteDatabase database = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + CLOTHES_TABLE + " WHERE id = " + Id;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(rawQuery, null);
        if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
                drawerNo = cursor.getInt(1);
                type = cursor.getString(2);
                color = cursor.getString(3);
                pattern = cursor.getString(4);
                date = cursor.getString(5);
                price = cursor.getString(6);
                photo = cursor.getBlob(7);

            return new Clothes(id, drawerNo, type, color, pattern, date, price, photo);
        }
        return null;
    }

    public ArrayList<Clothes> getDrawerClothes(String no) {
        ArrayList<Clothes> clothes = new ArrayList<>();
        int id, drawerNo;
        String type, color, pattern, date, price;
        byte[] photo;

        SQLiteDatabase database = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + CLOTHES_TABLE + " WHERE drawerNo = " + no;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(rawQuery, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                drawerNo = cursor.getInt(1);
                type = cursor.getString(2);
                color = cursor.getString(3);
                pattern = cursor.getString(4);
                date = cursor.getString(5);
                price = cursor.getString(6);
                photo = cursor.getBlob(7);

                clothes.add(new Clothes(id, drawerNo, type, color, pattern, date, price, photo));
            }while (cursor.moveToNext());

            return clothes;
        }
        return null;
    }

    public ArrayList<Clothes> getAllClothes() {
        ArrayList<Clothes> clothes = new ArrayList<>();
        int id, drawerNo;
        String type, color, pattern, date, price;
        byte[] photo;

        SQLiteDatabase database = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + CLOTHES_TABLE;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(rawQuery, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                drawerNo = cursor.getInt(1);
                type = cursor.getString(2);
                color = cursor.getString(3);
                pattern = cursor.getString(4);
                date = cursor.getString(5);
                price = cursor.getString(6);
                photo = cursor.getBlob(7);

                clothes.add(new Clothes(id, drawerNo, type, color, pattern, date, price, photo));
            }while (cursor.moveToNext());

            return clothes;
        }
        return null;
    }

    public ArrayList<Outfit> getOutfits() {
        ArrayList<Outfit> outfits = new ArrayList<>();
        int id, overhead, face, upper, lower, foot;

        SQLiteDatabase database = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + OUTFITS_TABLE;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(rawQuery, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                overhead = cursor.getInt(1);
                face = cursor.getInt(2);
                upper = cursor.getInt(3);
                lower = cursor.getInt(4);
                foot = cursor.getInt(5);

                outfits.add(new Outfit(id, overhead, face, upper, lower, foot));
            }while (cursor.moveToNext());

            return outfits;
        }
        return null;
    }

    public Event getEvent(int Id) {
        int id, outfitNo;
        String name, type, date, location;

        SQLiteDatabase database = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + EVENTS_TABLE + " WHERE id = " + Id;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(rawQuery, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
            outfitNo = cursor.getInt(1);
            name = cursor.getString(2);
            type = cursor.getString(3);
            date = cursor.getString(4);
            location = cursor.getString(5);

            return new Event(id, outfitNo, name, type, date, location);
        }
        return null;
    }

    public ArrayList<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<>();
        int id, outfitNo;
        String name, type, date, location;

        SQLiteDatabase database = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + EVENTS_TABLE;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(rawQuery, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0);
                outfitNo = cursor.getInt(1);
                name = cursor.getString(2);
                type = cursor.getString(3);
                date = cursor.getString(4);
                location = cursor.getString(5);

                events.add(new Event(id, outfitNo, name, type, date, location));
            }while (cursor.moveToNext());

            return events;
        }
        return null;
    }
}
