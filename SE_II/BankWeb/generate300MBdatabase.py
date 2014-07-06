import re, os, csv, hashlib

totalbranches = 0
totalaccounts = 25
totalcustomers = 7

creditcardnumber = 8888888888888887 # first add 1 then use

def getUniqueBranchRoutingNumber():
	routingNo = 271070801
	index = 0
	while(index < totalbranches):
		index += 1
		routingNo += 1
		yield(str(routingNo))

def main():
	output = open('extra-data.sql', 'wb')

	# Insert values 

	authentication = "INSERT INTO Authentication VALUES('{0}','{1}');\n"
	branch = "INSERT INTO Branch VALUES({0},'{2}','{1}');\n"
	customer = "INSERT INTO Customer VALUES({0},'{1}',{2});\n"
	account = "INSERT INTO Account VALUES({0}, {1}, {2}, {3}, {4});\n"
	credit = "INSERT INTO Credit VALUES('{0}', '{1}', {2}, {3});\n"
	
	# Create 10,000 branches
	global totalbranches	
	totalbranches = 111000
	#totalbranches = 10

	for branchID, routingNo in enumerate(getUniqueBranchRoutingNumber()):
		if branchID > totalbranches - 2: break
		output.write(branch.format(str(branchID + 2), 'IL', routingNo))
	

	# Make 6 customers in each branch
	global totalcustomers
	global totalaccounts
	global creditcardnumber

	for branchID in xrange(2, totalbranches + 1):
		listCustomerID = []
		customers = 0
		while customers < 6:
			listCustomerID.append(totalcustomers + 1)
			customers += 1
			totalcustomers += 1

		# Create Authentication & Customer Record
		for customerID in listCustomerID:		
			output.write(authentication.format("user"+str(customerID), "5f4dcc3b5aa765d61d8327deb882cf99"))

		# Create Authentication & Customer Record
		for customerID in listCustomerID:
			output.write(customer.format(customerID, "user" + str(customerID), branchID))

		# Create 4 accounts for each customer
		
		for customerID in listCustomerID:

			totalaccounts += 1
			output.write(account.format(totalaccounts, customerID, 1, 8888, 100000.00))	
			totalaccounts += 1
			output.write(account.format(totalaccounts, customerID, 2, 8888, 100000.00))	
			totalaccounts += 1
			output.write(account.format(totalaccounts, customerID, 3, 8888, 100000.00))	
			totalaccounts += 1
			output.write(account.format(totalaccounts, customerID, 4, 8888, 100000.00))	

			# Insert values in Credit
			creditcardnumber += 1
			cardNumber = creditcardnumber
			output.write(credit.format(cardNumber, '888',totalaccounts-1,1500.00))

			
	# authentication_values = [('Akshay', 'password')]
	# for i in authentication_values:
	# 	print i[0]

	# for i in authentication_values:
	# 	m = hashlib.md5()
	# 	m.update(i[1])
	# 	print str(m.hexdigest())

	
		
	
	# Stop Inserting Value

	output.close()

if __name__ == '__main__':
	main()