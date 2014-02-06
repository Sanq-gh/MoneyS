package test.com.sanq.ds;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.sanq.ds.DbHelper;
import com.sanq.utils.Cnt;
import com.sanq.utils.SLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 31.05.13
 * Time: 0:18
 * To change this template use File | Settings | File Templates.
 */
public class DbHelperTest extends AndroidTestCase {
    private DbHelper dbh ;
    private SQLiteDatabase db ;

//    public DbHelperTest() {
//        super();
//    }

    @Before
    public void setUp() throws Exception {
        this.getContext().deleteDatabase(Cnt.DB_NAME);   //todo: d't forget delete this
        dbh = new DbHelper(this.getContext());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDbHelperCreated() throws Exception {
        assertNotNull(dbh);
    }

    @Test
    public void testInuitDb() {
        db = dbh.getReadableDatabase();
        assertTrue(db.getVersion() == Cnt.DB_VERSION);
        SLog.d(" --- Staff db v." + db.getVersion() + " --- ");
        db.close();
    }

    @Test
    public void testIsDataFilling() throws Exception {
        db = dbh.getReadableDatabase();
        assertTrue(db.getVersion() == Cnt.DB_VERSION);
        SLog.d(" --- Staff db v." + db.getVersion() + " --- ");
        db.close();
    }
}
