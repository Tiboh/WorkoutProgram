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

    private static final String TABLE_MUSCLE = "muscle";
    private static final String TABLE_IMAGE = "image";

    // Program table columns names
    private static final String PROGRAM_ID = "id";
    private static final String PROGRAM_NAME = "name";

    // Exercise table columns names
    private static final String EXERCISE_ID = "id";
    private static final String EXERCISE_NAME = "name";
    private static final String EXERCISE_PROGRAM_ID = "program_id";
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

    // Muscle table columns names
    private static final String MUSCLE_ID = "id";
    private static final String MUSCLE_NAME = "name";
    private static final String MUSCLE_TYPE = "type";
    private static final String MUSCLE_EXERCISE_ID = "exercise_id";

    // Muscle table columns names
    private static final String IMAGE_ID = "id";
    private static final String IMAGE_URL = "url";
    private static final String IMAGE_POSITION = "position";
    private static final String IMAGE_EXERCISE_ID = "exercise_id";


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
                + EXERCISE_PROGRAM_ID + " INTEGER NOT NULL,"
                + EXERCISE_POSITION + " INTEGER NOT NULL,"
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

        String CREATE_MUSCLE_TABLE = "CREATE TABLE " + TABLE_MUSCLE + "("
                + MUSCLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MUSCLE_NAME + " TEXT NOT NULL,"
                + MUSCLE_TYPE + " TEXT NOT NULL DEFAULT '" + Muscle.MUSCLE_TYPE_PRIMARY + "' CHECK("+ MUSCLE_TYPE +" in ('"+ Muscle.MUSCLE_TYPE_PRIMARY + "','" + Muscle.MUSCLE_TYPE_SECONDARY + "')),"
                + MUSCLE_EXERCISE_ID + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + MUSCLE_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISE + "(" + EXERCISE_ID + ")" + ")";
        db.execSQL(CREATE_MUSCLE_TABLE);

        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
                + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + IMAGE_URL + " TEXT NOT NULL,"
                + IMAGE_POSITION + " INTEGER NOT NULL,"
                + IMAGE_EXERCISE_ID + " INTEGER NOT NULL,"
                + "FOREIGN KEY (" + IMAGE_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISE + "(" + EXERCISE_ID + ")" + ")";
        db.execSQL(CREATE_IMAGE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSCLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);

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
        values.put(EXERCISE_PROGRAM_ID, exercise.getProgramId());
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

    public long addImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IMAGE_URL, image.getUrl());
        values.put(IMAGE_POSITION, image.getPosition());
        values.put(IMAGE_EXERCISE_ID, image.getExerciseId());

        // Inserting Row
        long id = db.insert(TABLE_IMAGE, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public long addMuscle(Muscle muscle) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MUSCLE_NAME, muscle.getName());
        values.put(MUSCLE_TYPE, muscle.getType());
        values.put(MUSCLE_EXERCISE_ID, muscle.getExerciseId());

        // Inserting Row
        long id = db.insert(TABLE_MUSCLE, null, values);
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
                        EXERCISE_NAME, EXERCISE_PROGRAM_ID, EXERCISE_POSITION, EXERCISE_DESCRIPTION}, EXERCISE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Exercise exercise = new Exercise(Long.parseLong(cursor.getString(0)),
                cursor.getString(1), Long.parseLong(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)), cursor.getString(4));
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

    public Image getImage(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGE, new String[]{IMAGE_ID,
                        IMAGE_URL, IMAGE_POSITION, IMAGE_EXERCISE_ID}, IMAGE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Image image = new Image(Long.parseLong(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), Long.parseLong(cursor.getString(3)));
        return image;
    }

    public Muscle getMuscle(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MUSCLE, new String[]{MUSCLE_ID,
                        MUSCLE_NAME, MUSCLE_TYPE, MUSCLE_EXERCISE_ID}, MUSCLE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Muscle muscle = new Muscle(Long.parseLong(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Long.parseLong(cursor.getString(3)));
        return muscle;
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
                program.setId(Long.parseLong(cursor.getString(0)));
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
                exercise.setId(Long.parseLong(cursor.getString(0)));
                exercise.setName(cursor.getString(1));
                exercise.setProgramId(Long.parseLong(cursor.getString(2)));
                exercise.setPosition(Integer.parseInt(cursor.getString(3)));
                exercise.setDescription(cursor.getString(4));
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
                set.setId(Long.parseLong(cursor.getString(0)));
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

    public ArrayList<Muscle> getAllMuscles() {
        ArrayList<Muscle> muscleList = new ArrayList<Muscle>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MUSCLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Muscle muscle = new Muscle();
                muscle.setId(Long.parseLong(cursor.getString(0)));
                muscle.setName(cursor.getString(1));
                muscle.setType(cursor.getString(2));
                muscle.setExerciseId(Long.parseLong(cursor.getString(3)));
                // Adding contact to list
                muscleList.add(muscle);
            } while (cursor.moveToNext());
        }

        // return contact list
        return muscleList;
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
                exercise.setId(Long.parseLong(cursor.getString(0)));
                exercise.setName(cursor.getString(1));
                exercise.setProgramId(Long.parseLong(cursor.getString(2)));
                exercise.setPosition(Integer.parseInt(cursor.getString(3)));
                exercise.setDescription(cursor.getString(4));
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
                set.setId(Long.parseLong(cursor.getString(0)));
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

    public ArrayList<Image> getAllImagesExercise(long exerciseId) {
        ArrayList<Image> imageList = new ArrayList<Image>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGE + " WHERE " + IMAGE_EXERCISE_ID + "=" + exerciseId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Image image = new Image();
                image.setId(Long.parseLong(cursor.getString(0)));
                image.setUrl(cursor.getString(1));
                image.setPosition(Integer.parseInt(cursor.getString(2)));
                image.setExerciseId(Long.parseLong(cursor.getString(3)));
                // Adding contact to list
                imageList.add(image);
            } while (cursor.moveToNext());
        }

        // return contact list
        return imageList;
    }

    public ArrayList<Muscle> getAllMuscleExercise(long exerciseId) {
        ArrayList<Muscle> muscleList = new ArrayList<Muscle>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MUSCLE + " WHERE " + MUSCLE_EXERCISE_ID + "=" + exerciseId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Muscle muscle = new Muscle();
                muscle.setId(Long.parseLong(cursor.getString(0)));
                muscle.setName(cursor.getString(1));
                muscle.setType(cursor.getString(2));
                muscle.setExerciseId(Long.parseLong(cursor.getString(3)));
                // Adding contact to list
                muscleList.add(muscle);
            } while (cursor.moveToNext());
        }

        // return contact list
        return muscleList;
    }

    public ArrayList<Muscle> getAllPrimaryMuscleExercise(long exerciseId) {
        ArrayList<Muscle> muscleList = new ArrayList<Muscle>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MUSCLE + " WHERE " + MUSCLE_EXERCISE_ID + "=" + exerciseId + " AND " + MUSCLE_TYPE + "='" + Muscle.MUSCLE_TYPE_PRIMARY+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Muscle muscle = new Muscle();
                muscle.setId(Long.parseLong(cursor.getString(0)));
                muscle.setName(cursor.getString(1));
                muscle.setType(cursor.getString(2));
                muscle.setExerciseId(Long.parseLong(cursor.getString(3)));
                // Adding contact to list
                muscleList.add(muscle);
            } while (cursor.moveToNext());
        }

        // return contact list
        return muscleList;
    }

    public ArrayList<Muscle> getAllSecondaryMuscleExercise(long exerciseId) {
        ArrayList<Muscle> muscleList = new ArrayList<Muscle>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MUSCLE + " WHERE " + MUSCLE_EXERCISE_ID + "=" + exerciseId + " AND " + MUSCLE_TYPE + "='" + Muscle.MUSCLE_TYPE_SECONDARY+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Muscle muscle = new Muscle();
                muscle.setId(Long.parseLong(cursor.getString(0)));
                muscle.setName(cursor.getString(1));
                muscle.setType(cursor.getString(2));
                muscle.setExerciseId(Long.parseLong(cursor.getString(3)));
                // Adding contact to list
                muscleList.add(muscle);
            } while (cursor.moveToNext());
        }

        // return contact list
        return muscleList;
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
        values.put(EXERCISE_PROGRAM_ID, exercise.getProgramId());
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

    public int updateImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IMAGE_URL, image.getUrl());
        values.put(IMAGE_POSITION, image.getPosition());
        values.put(IMAGE_EXERCISE_ID, image.getExerciseId());

        // updating row
        return db.update(TABLE_IMAGE, values, IMAGE_ID + " = ?",
                new String[]{String.valueOf(image.getId())});
    }

    public int updateMuscle(Muscle muscle) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MUSCLE_NAME, muscle.getName());
        values.put(MUSCLE_TYPE, muscle.getType());
        values.put(MUSCLE_EXERCISE_ID, muscle.getExerciseId());

        // updating row
        return db.update(TABLE_MUSCLE, values, MUSCLE_ID + " = ?",
                new String[]{String.valueOf(muscle.getId())});
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

    public void deleteMuscle(Muscle muscle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MUSCLE, MUSCLE_ID + " = ?",
                new String[]{String.valueOf(muscle.getId())});
        db.close();
    }

    public void deleteImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGE, IMAGE_ID + " = ?",
                new String[]{String.valueOf(image.getId())});
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
