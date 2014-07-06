package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	GetCreditAccountTest.class, 
	GetOrderHistoryTest.class,
	GetTransactionHistoryTest.class, 
	ListAllAccountsTest.class,
	ListAccountsTest.class,
	LoginTest.class, 
	
	MakeOrderTest.class,
	FailOrderLessMoneyTest.class,
	FailOrderPortalUserTest.class,
	FailOrderItemNotAvailableTest.class,
	FailOrderInsufficientBalanceTest.class,
	FailOrderWrongRewardsCustomerTest.class,
	FailOrderWrongRewardsCustomerTest.class,

	TransferFromCheckToSaveTest.class,
	FailTransferInsufficientBalanceTest.class,
	
	}
)
public class Coverage {

}
