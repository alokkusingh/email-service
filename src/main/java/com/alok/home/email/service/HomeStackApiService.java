package com.alok.home.email.service;

import com.alok.home.commons.dto.EmailRequest;
import com.alok.home.commons.dto.api.response.ExpensesMonthSumByCategoryResponse;
import com.alok.home.commons.dto.api.response.ExpensesResponseAggByDay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@Service
public class HomeStackApiService {

    private final RestTemplate homeApiRestTemplate;
    private final String apiBaseUrl;

    public HomeStackApiService(RestTemplate homeApiRestTemplate, @Value("${home-api.url}") String apiBaseUrl) {
        this.homeApiRestTemplate = homeApiRestTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    // URL: https://hdash.alok.digital/home/api/expense/sum_by_category_month
    // This Month Expense by category
    // Previous Month Expense By Category
    // This Month Total Expense
    // Previous Month Total Expense
    // This Year Total Expense
    // Previous Year Total Expense

    // URL: https://hdash.alok.digital/home/api/expense/current_month_by_day
    // Yeterday's Expense

    public Map<String, Object> getDailySummaryReportData() {

        NumberFormat df = new DecimalFormat("##,##,###");

        Map<String, Object> response = new HashMap<>();
        try {
            ExpensesMonthSumByCategoryResponse expensesMonthSumByCategoryResponse = homeApiRestTemplate
                    .getForEntity(apiBaseUrl + "/expense/sum_by_category_month", ExpensesMonthSumByCategoryResponse.class)
                    .getBody();

            LocalDate today = LocalDate.now();
            response.put("thisMonthSpendCategories", expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(today.getMonth().getValue()))
                    .map(expenseCategoryMonthSum -> {
                        Map<String, Object> expenseCategoryMap = new HashMap<>();
                        expenseCategoryMap.put("category", expenseCategoryMonthSum.getCategory());
                        expenseCategoryMap.put("amount", df.format(Math.round(expenseCategoryMonthSum.getSum())));
                        return expenseCategoryMap;
                    })
                    .toList());

            response.put("lastMonthSpendCategories", expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(today.getMonth().getValue()-1))
                    .map(expenseCategoryMonthSum -> {
                        Map<String, Object> expenseCategoryMap = new HashMap<>();
                        expenseCategoryMap.put("category", expenseCategoryMonthSum.getCategory());
                        expenseCategoryMap.put("amount", df.format(Math.round(expenseCategoryMonthSum.getSum())));
                        return expenseCategoryMap;
                    })
                    .toList());

            response.put("thisMonthTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(today.getMonth().getValue()))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("previousMonthTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .filter(expenseCategoryMonthSum -> expenseCategoryMonthSum.getMonth().equals(today.getMonth().getValue()-1))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("thisYearTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear()))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            response.put("previousYearTotalExpense", df.format(Math.round(expensesMonthSumByCategoryResponse.getExpenseCategorySums().stream()
                    .filter(expenseCategorySum -> expenseCategorySum.getYear().equals(today.getYear() - 1))
                    .map(ExpensesMonthSumByCategoryResponse.ExpenseCategoryMonthSum::getSum)
                    .reduce(0d, Double::sum))));

            ExpensesResponseAggByDay expensesResponseAggByDay = homeApiRestTemplate
                    .getForEntity(apiBaseUrl + "/expense/current_month_by_day", ExpensesResponseAggByDay.class)
                    .getBody();

            response.put("yesterdayExpense", df.format(Math.round(expensesResponseAggByDay.getExpenses().stream()
                    .filter(expenseAggByDay -> expenseAggByDay.getDate().equals(today.minusDays(1)))
                    .map(ExpensesResponseAggByDay.DayExpense::getAmount)
                    .reduce(0d, Double::sum))));
        } catch (RuntimeException rte) {
            rte.printStackTrace();
            System.out.println("Failed to send email, error: "  + rte.getMessage());
        }

        return response;
    }
}
