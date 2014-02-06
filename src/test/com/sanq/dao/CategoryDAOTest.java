package test.com.sanq.dao;

import android.content.Context;
import android.test.AndroidTestCase;
import com.sanq.dao.CategoryDAO;
import com.sanq.entity.Category;
import com.sanq.utils.SLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 30.05.13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class CategoryDAOTest extends AndroidTestCase {

    Context context;
    @Before
    public void setUp() throws Exception {
       context  = this.getContext();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetById(){
        int id = 0 ;
        Category category =null;
        while(category == null){
            id++;
            category = new CategoryDAO(context).getById(id);
        }
        SLog.d(category.toString());
        assertTrue(category.getId()==id);
    }
    @Test
    public void testAdd() throws Exception {
        Category cat = new Category("Тестовая категория" ,0, false);
        Category cat_added = new CategoryDAO(context).add(cat);
        assertEquals(cat.getName(), cat_added.getName());
    }

    @Test
    public void testGetAll() throws Exception {
        List<Category> cats = new CategoryDAO(context).getAll();
        assertTrue(cats.size()>5);
       SLog.d(cats.toString());
    }

    @Test
    public void testDeleteById() throws Exception {
        int id = 0 ;
        Category category =null;
        while(category == null & id <10 ){
            id++;
            category = new CategoryDAO(context).getById(id);
        }
        assertTrue(category.getId()==id);
        new CategoryDAO(context).deleteById(id);

    }

    @Test
    public void testUpdate() throws Exception {
        int id = 10 ;
        Category category  = new CategoryDAO(context).getById(id );
        category.setName("test_update");
        category.setId_parent(1);
        category.setIsgroup(true);
        new CategoryDAO(context).update(category) ;

    }















}
