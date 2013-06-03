package de.farkas.beispiel.dbunitTools;

import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.operation.MicrosoftSqlDatabaseOperationLookup;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class }) //DbUnitTestExecutionListener statt beider!
@DbUnitConfiguration(databaseConnection="dbUnitDatabaseConnection", dataSetLoader=CsvDataSetLoader.class, databaseOperationLookup=MicrosoftSqlDatabaseOperationLookup.class)
@DatabaseSetup(value="default")//classpath:de/farkas/beispiel/dao
@Transactional
public abstract class DBUnitTransactionalTest {

}
