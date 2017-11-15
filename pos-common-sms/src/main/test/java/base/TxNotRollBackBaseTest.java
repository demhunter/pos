package base;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 单元测试基础类，数据库不会回滚
 *
 * author chi.chen
 * date 16-2-2
 * time 下午3:32
 */
@Transactional("txManager")
@Rollback(false)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TxNotRollBackBaseTest {
}
