package com.test.jsonparserutil.main;

import com.test.jsonparserutil.dto.Address;
import com.test.jsonparserutil.dto.Customer;
import com.test.jsonparserutil.utils.GsonJsonParserUtil;
import com.test.jsonparserutil.utils.JsonParserUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestMain {
    public static void main(String[] args) throws IOException {

        final List<Customer> customerList = new ArrayList<>();
        final Customer customer1 = new Customer(1L, "fname-1", "lName-1", new Address("addressLine-1", "addressLine-11"));
        final Customer customer2 = new Customer(2L, "fname-2", "lName-2", new Address("addressLine-2", "addressLine-22"));
        customerList.add(customer1);
        customerList.add(customer2);
        log.info("customerList : {} ", customerList);

        String customerJsonStr = GsonJsonParserUtil.toJsonString(customerList);
        log.info("customerJsonStr : {}", customerJsonStr);

        List<Customer> list = GsonJsonParserUtil.getList(customerJsonStr, Customer.class);
        log.info("list : {}", list);

        Customer customerObj = GsonJsonParserUtil.getObjectByJsonFile("sample-json/customer-single-data.json", Customer.class);

        log.info("customerObj : " + customerObj);

        System.out.println("--------------------------#####################3-----------------------------------------");

        String customerJsonStrUsingOBM = JsonParserUtil.toJson(customerList);
        log.info("customerJsonStrUsingOBM : {}", customerJsonStrUsingOBM);


        List<Customer> list1 = JsonParserUtil.fromJsonArray(customerJsonStrUsingOBM, Customer.class);
        log.info("list1 : {}", list1);

        String singleCustomerJsonStrUsingOBM = JsonParserUtil.toJson(customer1);
        log.info("singleCustomerJsonStrUsingOBM : {}", singleCustomerJsonStrUsingOBM);

        final Customer customer = JsonParserUtil.fromJson(singleCustomerJsonStrUsingOBM, Customer.class);
        log.info("customer : {}", customer);

        Customer customerObj2 = JsonParserUtil.getObjectByJsonFile("sample-json/customer-single-data.json", Customer.class);

        log.info("customerObj2 : " + customerObj2);



    }
}
