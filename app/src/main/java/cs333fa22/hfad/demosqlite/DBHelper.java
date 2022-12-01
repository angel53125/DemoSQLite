package cs333fa22.hfad.demosqlite;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "employees.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(DBContract.EmployeeEntry.CREATE_EMP_TABLE_CMD);

        db.execSQL(DBContract.EmployeeEntry.CREATE_EMP_TABLE_CMD);
    }


    //SQL QUERIES GO HERE
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DBContract.EmployeeEntry.DROP_EMP_TABLE_CMD);
        onCreate(db);
    }

    public void saveEmployee(String name, String des, long dobMS)
    {
         //INSERT INTO EMPLOYEE (NAME,DOB,designation) VALUES('ANGEL',10000000000,'BED');
        String insert = String.format("INSERT INTO %s (%s,%s,%s) " +
                "VALUES('%s',%d,'%s')",
                DBContract.EmployeeEntry.TABLE_NAME,
                DBContract.EmployeeEntry.COLUMN_NAME,
                DBContract.EmployeeEntry.COLUMN_DOB,
                DBContract.EmployeeEntry.COLUMN_DESIGNATION,
                name,dobMS,des);

        System.out.println(DBContract.EmployeeEntry.CREATE_EMP_TABLE_CMD);
        System.out.println("SAVING " + insert);



        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insert);

        db.close(); //closes database

    }

    public ArrayList<Employee> fetchAllEmployees()
    {

        ArrayList<Employee> allEmps = new ArrayList<>();
        String selectAllString = "SELECT * FROM " + DBContract.EmployeeEntry.TABLE_NAME;

        System.out.println("SAVING " + selectAllString);
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor starts at -1
        Cursor cursor = db.rawQuery(selectAllString,null);

        //get the positions of your columns
        int idPos = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_ID);
        int namePos = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_NAME);
        int dobPos = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_DOB);
        int desPos = cursor.getColumnIndex(DBContract.EmployeeEntry.COLUMN_DESIGNATION);

        //Use position to request the values in the columns
        while(cursor.moveToNext())
        {
            //Gets info from current record
            long id = cursor.getLong(idPos);
            long dob = cursor.getLong(dobPos);
            String name = cursor.getString(namePos);
            String desig = cursor.getString(desPos);

            allEmps.add(new Employee(id,name,dob,desig));
        }

        cursor.close();
        db.close();
        return allEmps;
    }

    public void updateEmployee(Employee emp)
    {
        //UPDATE EMPLOYEE SET designation = 'PRESIDENT EXTRAORDINAIRE' WHERE _ID = 1;
        String updateString = String.format("UPDATE %s SET %s = '%s', " +
                                            " %s = '%s'," +
                                            " %s = %d" +
                                            " WHERE %s = %d;",
                                            DBContract.EmployeeEntry.TABLE_NAME,
                DBContract.EmployeeEntry.COLUMN_NAME,
                emp.getName(),
                DBContract.EmployeeEntry.COLUMN_DESIGNATION,
                emp.getDesignation(),
                DBContract.EmployeeEntry.COLUMN_DOB,
                emp.getDob(),
                DBContract.EmployeeEntry.COLUMN_ID,
                emp.getId());

        System.out.println("Updating "+ updateString);

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(updateString);
        db.close();

    }


}
