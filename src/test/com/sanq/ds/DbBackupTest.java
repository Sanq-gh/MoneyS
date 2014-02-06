package test.com.sanq.ds;

import android.test.AndroidTestCase;
import com.sanq.ds.DbUtils;
import com.sanq.exception.DBException;
import com.sanq.exception.SavingException;
import com.sanq.utils.Cnt;
import com.sanq.utils.SLog;
import com.sanq.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 31.05.13
 * Time: 0:18
 * To change this template use File | Settings | File Templates.
 */
public class DbBackupTest extends AndroidTestCase {

//    public DbHelperTest() {
//        super();
//    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testCycleBackupDb() {
        for (int i = 0 ;i<7;i++){
            try {
                Thread.sleep(60000);
                DbUtils.cycleBackupDb(getContext());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void _testCheckDbIsValid() throws SavingException {
        assertTrue(DbUtils.checkDbIsValid(getContext(), new File(Utils.getAppPath(getContext(), Utils.TypePath.BackUp) + "//" + Cnt.DB_NAME)));
    }

    @Test
    public void _testRestoreDb() {
        try {
            DbUtils.restoreDb(getContext());
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _testBackUp() throws DBException {

        try {
            DbUtils.backupDb(getContext());
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _testGetDbName() {
        SLog.d("data base name  - " + getContext().getDatabasePath(Cnt.DB_NAME));
    }
}
