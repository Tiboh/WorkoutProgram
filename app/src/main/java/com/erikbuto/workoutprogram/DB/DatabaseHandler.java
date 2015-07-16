package com.erikbuto.workoutprogram.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utilisateur on 26/06/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "workoutDB";

    // Table names
    private static final String TABLE_PROGRAM = "program";
    private static final String TABLE_EXERCISE = "exercise";
    private static final String TABLE_SET = "sett";

    // Program table columns names
    private static final String PROGRAM_ID = "id";
    private static final String PROGRAM_NAME = "name";

    // Exercise table columns names
    private static final String EXERCISE_ID = "id";
    private static final String EXERCISE_NAME = "name";
    private static final String EXERCISE_IMAGE_URL = "image_url";
    private static final String EXERCISE_PROGRAM_ID = "program_id";
    private static final String EXERCISE_MODE = "mode";
    private static final String EXERCISE_POSITION = "position";
    private static final String EXERCISE_DESCRIPTION = "description";

    // Set table columns names
    private static final String SET_ID = "id";
    private static final String SET_NB_REP = "nb_rep";
    private static final String SET_WEIGHT = "weight";
    private static final String SET_REST_MINUTE = "rest_minute";
    private static final String SET_REST_SECONDS = "rest_seconds";
    private static final String SET_EXERCISE_ID = "exercise_id";
    private static final String SET_POSITION = "position";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROGRAM_TABLE = "CREATE TABLE " + TABLE_PROGRAM + "("
                + PROGRAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PROGRAM_NAME + " TEXT NOT NULL UNIQUE" + ")";
        db.execSQL(CREATE_PROGRAM_TABLE);

        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
                + EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EXERCISE_NAME + " TEXT NOT NULL,"
                + EXERCISE_IMAGE_URL + " TEXT,"
                + EXERCISE_PROGRAM_ID + " INTEGER NOT NULL,"
                + EXERCISE_POSITION + " INTEGER NOT NULL,"
                + EXERCISE_MODE + " TEXT NOT NULL DEFAULT 'simple' CHECK(mode in ('simple','advanced')),"
                + EXERCISE_DESCRIPTION + " TEXT,"
                + "FOREIGN KEY (" + EXERCISE_PROGRAM_ID + ") REFERENCES " + TABLE_PROGRAM + "(" + PROGRAM_ID + ")" + ")";
        db.execSQL(CREATE_EXERCISE_TABLE);

        String CREATE_SET_TABLE = "CREATE TABLE " + TABLE_SET + "("
                + SET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SET_NB_REP + " INTEGER NOT NULL,"
                + SET_WEIGHT + " INTEGER,"
                + SET_REST_MINUTE + " INTEGER NOT NULL,"
                + SET_REST_SECONDS + " INTEGER NOT NULL,"
                + SET_EXERCISE_ID + " INTEGER NOT NULL,"
                + SET_POSITION + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + SET_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISE + "(" + EXERCISE_ID + ")" + ")";
        db.execSQL(CREATE_SET_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SET);

        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new
    public long addProgram(Program program) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROGRAM_NAME, program.getName());

        // Inserting Row
        long id = db.insert(TABLE_PROGRAM, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public long addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EXERCISE_NAME, exercise.getName());
        values.put(EXERCISE_IMAGE_URL, exercise.getImageUrl());
        values.put(EXERCISE_PROGRAM_ID, exercise.getProgramId());
        values.put(EXERCISE_MODE, exercise.getMode());
        values.put(EXERCISE_POSITION, exercise.getPosition());
        values.put(EXERCISE_DESCRIPTION, exercise.getDescription());

        // Inserting Row
        long id = db.insert(TABLE_EXERCISE, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public long addSet(Set set) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SET_NB_REP, set.getNbRep());
        values.put(SET_WEIGHT, set.getWeight());
        values.put(SET_REST_MINUTE, set.getRestTimeMinute());
        values.put(SET_REST_SECONDS, set.getRestTimeSecond());
        values.put(SET_EXERCISE_ID, set.getExerciseId());
        values.put(SET_POSITION, set.getPosition());

        // Inserting Row
        long id = db.insert(TABLE_SET, null, values);
        db.close(); // Closing database connection
        return id;
    }

    // Getting single
    public Program getProgram(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROGRAM, new String[]{PROGRAM_ID,
                        PROGRAM_NAME}, PROGRAM_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Program program = new Program(Long.parseLong(cursor.getString(0)),
                cursor.getString(1));
        return program;
    }

    public Exercise getExercise(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISE, new String[]{EXERCISE_ID,
                        EXERCISE_NAME, EXERCISE_IMAGE_URL, EXERCISE_PROGRAM_ID, EXERCISE_POSITION, EXERCISE_MODE, EXERCISE_DESCRIPTION}, EXERCISE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Exercise exercise = new Exercise(Long.parseLong(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Long.parseLong(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)), cursor.getString(5), cursor.getString(6));
        return exercise;
    }

    public Set getSet(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SET, new String[]{SET_ID,
                        SET_NB_REP, SET_WEIGHT, SET_REST_MINUTE, SET_REST_SECONDS, SET_EXERCISE_ID, SET_POSITION}, SET_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Set set = new Set(Long.parseLong(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)), Long.parseLong(cursor.getString(5)), Integer.parseInt(cursor.getString(6)));
        return set;
    }

    // Getting All
    public ArrayList<Program> getAllPrograms() {
        ArrayList<Program> programList = new ArrayList<Program>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROGRAM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Program program = new Program();
                program.setId(Integer.parseInt(cursor.getString(0)));
                program.setName(cursor.getString(1));
                // Adding contact to list
                programList.add(program);
            } while (cursor.moveToNext());
        }

        // return contact list
        return programList;
    }

    public ArrayList<Exercise> getAllExercises() {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setId(Integer.parseInt(cursor.getString(0)));
                exercise.setName(cursor.getString(1));
                exercise.setImageUrl(cursor.getString(2));
                exercise.setProgramId(Long.parseLong(cursor.getString(3)));
                exercise.setPosition(Integer.parseInt(cursor.getString(4)));
                exercise.setMode(cursor.getString(5));
                exercise.setDescription(cursor.getString(6));
                // Adding contact to list
                exerciseList.add(exercise);
            } while (cursor.moveToNext());
        }

        // return contact list
        return exerciseList;
    }

    public ArrayList<Set> getAllSets() {
        ArrayList<Set> setList = new ArrayList<Set>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SET;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Set set = new Set();
                set.setId(Integer.parseInt(cursor.getString(0)));
                set.setNbRep(Integer.parseInt(cursor.getString(1)));
                set.setWeight(Integer.parseInt(cursor.getString(2)));
                set.setRestTimeMinute(Integer.parseInt(cursor.getString(3)));
                set.setRestTimeSecond(Integer.parseInt(cursor.getString(4)));
                set.setExerciseId(Long.parseLong(cursor.getString(5)));
                set.setPosition(Integer.parseInt(cursor.getString(6)));
                // Adding contact to list
                setList.add(set);
            } while (cursor.moveToNext());
        }

        // return contact list
        return setList;
    }

    public ArrayList<Exercise> getAllExercisesProgram(long programId) {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXERCISE + " WHERE " + EXERCISE_PROGRAM_ID + "=" + programId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setId(Integer.parseInt(cursor.getString(0)));
                exercise.setName(cursor.getString(1));
                exercise.setImageUrl(cursor.getString(2));
                exercise.setProgramId(Long.parseLong(cursor.getString(3)));
                exercise.setPosition(Integer.parseInt(cursor.getString(4)));
                exercise.setMode(cursor.getString(5));
                exercise.setDescription(cursor.getString(6));
                // Adding contact to list
                exerciseList.add(exercise);
            } while (cursor.moveToNext());
        }

        // return contact list
        return exerciseList;
    }

    public ArrayList<Set> getAllSetsExercise(long exerciseId) {
        ArrayList<Set> setList = new ArrayList<Set>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SET + " WHERE " + SET_EXERCISE_ID + "=" + exerciseId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Set set = new Set();
                set.setId(Integer.parseInt(cursor.getString(0)));
                set.setNbRep(Integer.parseInt(cursor.getString(1)));
                set.setWeight(Integer.parseInt(cursor.getString(2)));
                set.setRestTimeMinute(Integer.parseInt(cursor.getString(3)));
                set.setRestTimeSecond(Integer.parseInt(cursor.getString(4)));
                set.setExerciseId(Long.parseLong(cursor.getString(5)));
                set.setPosition(Integer.parseInt(cursor.getString(6)));
                // Adding contact to list
                setList.add(set);
            } while (cursor.moveToNext());
        }

        // return contact list
        return setList;
    }

    // Getting Count
    public int getProgramsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PROGRAM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int result = cursor.getCount();
        cursor.close();

        // return count
        return result;
    }

    public int getExercisesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EXERCISE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int result = cursor.getCount();
        cursor.close();

        // return count
        return result;
    }

    public int getExercisesCount(long programId) {
        String countQuery = "SELECT  * FROM " + TABLE_EXERCISE + " WHERE " + EXERCISE_PROGRAM_ID + "=" + programId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int result = cursor.getCount();
        cursor.close();

        // return count
        return result;
    }

    public int getSetsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SET;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int result = cursor.getCount();
        cursor.close();

        // return count
        return result;
    }

    // Updating single
    public int updateProgram(Program program) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROGRAM_NAME, program.getName());

        // updating row
        return db.update(TABLE_PROGRAM, values, PROGRAM_ID + " = ?",
                new String[]{String.valueOf(program.getId())});
    }

    public int updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EXERCISE_NAME, exercise.getName());
        values.put(EXERCISE_IMAGE_URL, exercise.getImageUrl());
        values.put(EXERCISE_PROGRAM_ID, exercise.getProgramId());
        values.put(EXERCISE_MODE, exercise.getMode());
        values.put(EXERCISE_POSITION, exercise.getPosition());
        values.put(EXERCISE_DESCRIPTION, exercise.getDescription());

        // updating row
        return db.update(TABLE_EXERCISE, values, EXERCISE_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
    }

    public int updateSet(Set set) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SET_NB_REP, set.getNbRep());
        values.put(SET_WEIGHT, set.getWeight());
        values.put(SET_REST_MINUTE, set.getRestTimeMinute());
        values.put(SET_REST_SECONDS, set.getRestTimeSecond());
        values.put(SET_EXERCISE_ID, set.getExerciseId());
        values.put(SET_POSITION, set.getPosition());

        // updating row
        return db.update(TABLE_SET, values, SET_ID + " = ?",
                new String[]{String.valueOf(set.getId())});
    }

    // Deleting single
    public void deleteProgram(Program program) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROGRAM, PROGRAM_ID + " = ?",
                new String[]{String.valueOf(program.getId())});
        db.close();
    }

    public void deleteExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, EXERCISE_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
        db.close();
    }

    public void deleteSet(Set set) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SET, SET_ID + " = ?",
                new String[]{String.valueOf(set.getId())});
        db.close();
    }

    public void deleteAllPrograms(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PROGRAM);
        db.close();
    }

    public void deleteAllExercises(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE);
        db.close();
    }

    public void deleteAllSets(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SET);
        db.close();
    }
}
